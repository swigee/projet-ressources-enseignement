package sae.project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sae.project.dtos.assignment.AssignmentRowDTO;
import sae.project.dtos.assignment.AssignmentGridDTO;
import sae.project.dtos.assignment.AssignmentStatisticsDTO;
import sae.project.dtos.assignment.AssignmentValidationResponseDTO;
import sae.project.dtos.assignment.CreateAssignmentDTO;
import sae.project.dtos.teacher.TeacherAssignmentDTO;
import sae.project.dtos.teacher.TeacherDTO;
import sae.project.model.Assignment;
import sae.project.model.Formation;
import sae.project.model.Resource;
import sae.project.model.User;
import sae.project.repositories.PedagogicalScheduleRepository;
import sae.project.repositories.TeacherAssignmentRepository;
import sae.project.repositories.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class TeacherAssignmentService {

        @Autowired
        private TeacherAssignmentRepository assignmentRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PedagogicalScheduleRepository resourcesRepository;

        public List<TeacherDTO> getAllTeachers() {
                log.info("Retrieving all teachers");

                List<Object[]> teachersWithHours = assignmentRepository.getTeachersWithHours();

                return teachersWithHours.stream()
                                .map(this::mapToTeacherDTO)
                                .collect(Collectors.toList());
        }

        public AssignmentGridDTO getAssignmentGrid(String program, String year, String className, String semester) {
                log.info("Retrieving assignment grid for program={} year={} className={} semester={}",
                                program, year, className, semester);

                Integer semesterInt = null;
                if (semester != null && !semester.isEmpty()) {
                        semesterInt = Integer.parseInt(semester);
                }

                List<TeacherDTO> teachers = getAllTeachers();
                List<AssignmentRowDTO> assignmentGrid = getAssignmentRows(program, year, className, semesterInt);
                AssignmentStatisticsDTO statistics = calculateStatistics(program, year, className, semesterInt);

                return AssignmentGridDTO.builder()
                                .selectedProgram(program)
                                .selectedYear(year)
                                .availableTeachers(teachers)
                                .assignmentGrid(assignmentGrid)
                                .statistics(statistics)
                                .build();
        }

        public Assignment createAssignment(CreateAssignmentDTO dto) {
                log.info("Creating assignment for user={} resource={}", dto.getUserId(), dto.getResourceId());

                Optional<Assignment> existing = assignmentRepository.findByUserIdAndResourceIdAndLessonType(
                                dto.getUserId(),
                                dto.getResourceId(),
                                dto.getLessonType());

                if (existing.isPresent()) {
                        throw new IllegalArgumentException(
                                        "This teacher is already assigned to this lesson type ("
                                                        + dto.getLessonType() + ") for this resource");
                }

                User teacher = userRepository.findById(dto.getUserId())
                                .orElseThrow(() -> new RuntimeException("Teacher not found"));

                Resource resource = resourcesRepository.findById(dto.getResourceId())
                                .orElseThrow(() -> new RuntimeException("Resource not found"));

                Assignment assignment = new Assignment();
                assignment.setUser(teacher);
                assignment.setResource(resource);
                assignment.setLessonType(dto.getLessonType());
                assignment.setAssignedTimes(dto.getAssignedTimes());

                return assignmentRepository.save(assignment);
        }

        public Assignment updateAssignment(Integer id, CreateAssignmentDTO dto) {
                log.info("Updating assignment {}", id);

                return assignmentRepository.findById(id)
                                .map(assignment -> {
                                        assignment.setLessonType(dto.getLessonType());
                                        assignment.setAssignedTimes(dto.getAssignedTimes());
                                        return assignmentRepository.save(assignment);
                                })
                                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        }

        public void deleteAssignment(Integer id) {
                log.info("Deleting assignment {}", id);

                if (!assignmentRepository.existsById(id)) {
                        throw new RuntimeException("Assignment not found");
                }

                assignmentRepository.deleteById(id);
        }

        public void deleteAssignmentByTeacherAndResource(Integer userId, Integer resourceId) {
                log.info("Deleting assignment for teacher={} resource={}", userId, resourceId);

                Assignment assignment = assignmentRepository.findByUserIdAndResourceId(userId, resourceId)
                                .orElseThrow(() -> new RuntimeException("Assignment not found"));

                assignmentRepository.delete(assignment);
        }

        public List<TeacherDTO> searchTeachers(String keyword) {
                log.info("Searching teachers with keyword: {}", keyword);

                return getAllTeachers().stream()
                                .filter(teacher -> teacher.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                                                (teacher.getSubject() != null && teacher.getSubject().toLowerCase()
                                                                .contains(keyword.toLowerCase())))
                                .collect(Collectors.toList());
        }

        public AssignmentValidationResponseDTO validateAssignments(String year, String className) {
                log.info("Validating assignments for year={} className={}", year, className);

                List<String> errors = new ArrayList<>();
                List<String> warnings = new ArrayList<>();

                List<Resource> resources = resourcesRepository.findByYearAndClass(year, className);

                for (Resource resource : resources) {
                        List<Assignment> assignments = assignmentRepository.findByResourceId(resource.getId());

                        if (assignments.isEmpty()) {
                                warnings.add("Resource '" + resource.getTitle() + "' has no assigned teacher");
                        }
                }

                List<TeacherDTO> teachers = getAllTeachers();
                for (TeacherDTO teacher : teachers) {
                        if (teacher.getTotalHours() > 192) {
                                warnings.add("Teacher " + teacher.getName() + " exceeds maximum workload");
                        }
                }

                boolean success = errors.isEmpty();
                String message = success
                                ? "Validation successful with " + warnings.size() + " warning(s)"
                                : "Validation failed with " + errors.size() + " error(s)";

                return AssignmentValidationResponseDTO.builder()
                                .success(success)
                                .message(message)
                                .errors(errors)
                                .warnings(warnings)
                                .build();
        }

        // ── Private helpers ────────────────────────────────────────────────────

        private TeacherDTO mapToTeacherDTO(Object[] data) {
                Integer id = (Integer) data[0];
                String firstName = (String) data[1];
                String lastName = (String) data[2];
                Integer totalHours = ((Number) data[3]).intValue();
                String type = data[4] != null ? (String) data[4] : "VACATAIRE";

                String status = "PERMANENT".equals(type) ? "Permanent" : "Vacataire";
                String statusColor = "PERMANENT".equals(type) ? "bg-green-100 text-green-800"
                                : "bg-blue-100 text-blue-800";

                return TeacherDTO.builder()
                                .id(id)
                                .firstName(firstName)
                                .lastName(lastName)
                                .name(firstName + " " + lastName)
                                .subject("Non renseigné")
                                .status(status)
                                .totalHours(totalHours)
                                .remainingHours(192 - totalHours)
                                .statusColor(statusColor)
                                .build();
        }

        private String nullIfBlank(String s) {
                return (s == null || s.isBlank()) ? null : s;
        }

        private List<AssignmentRowDTO> getAssignmentRows(String program, String year, String className, Integer semester) {
                String yearParam      = nullIfBlank(year);
                String classParam     = nullIfBlank(className);
                String programParam = nullIfBlank(program);
                boolean allClasses    = (classParam == null);

                List<Resource> resources = resourcesRepository.findWithFilters(yearParam, classParam, programParam, semester);

                return resources.stream()
                                .map(resource -> {
                                        List<Assignment> assignments = assignmentRepository.findByResourceId(resource.getId());

                                        List<String> allGroupsForResource = resource.getPrograms().stream()
                                                        .filter(f -> yearParam == null || yearParam.equals(f.getYear()))
                                                        .filter(f -> programParam == null || programParam.equals(f.getName()))
                                                        .map(Formation::getClassName)
                                                        .filter(cn -> cn != null)
                                                        .distinct()
                                                        .sorted()
                                                        .collect(Collectors.toList());
                                        int totalGroupCount = Math.max(1, allGroupsForResource.size());

                                        int hoursDivisor = allClasses ? 1 : totalGroupCount;
                                        int displayGroupCount = allClasses ? totalGroupCount : 1;
                                        List<String> groups = allClasses ? allGroupsForResource : null;

                                        List<TeacherAssignmentDTO> tdTeachers = assignments.stream()
                                                        .filter(a -> "TD".equals(a.getLessonType()))
                                                        .map(a -> mapToTeacherAssignmentDTO(a, hoursDivisor))
                                                        .collect(Collectors.toList());

                                        List<TeacherAssignmentDTO> tpTeachers = assignments.stream()
                                                        .filter(a -> "TP".equals(a.getLessonType()))
                                                        .map(a -> mapToTeacherAssignmentDTO(a, hoursDivisor))
                                                        .collect(Collectors.toList());

                                        List<TeacherAssignmentDTO> cmTeachers = assignments.stream()
                                                        .filter(a -> "CM".equals(a.getLessonType()))
                                                        .map(a -> mapToTeacherAssignmentDTO(a, hoursDivisor))
                                                        .collect(Collectors.toList());

                                        return AssignmentRowDTO.builder()
                                                        .resourceId(resource.getId())
                                                        .module(resource.getTitle())
                                                        .groups(groups)
                                                        .tdHours((resource.getTdStateHours() != null ? resource.getTdStateHours() : 0) * displayGroupCount)
                                                        .tpHours((resource.getTpStateHours() != null ? resource.getTpStateHours() : 0) * displayGroupCount)
                                                        .cmHours((resource.getCmStateHours() != null ? resource.getCmStateHours() : 0) * displayGroupCount)
                                                        .tdTeachers(tdTeachers)
                                                        .tpTeachers(tpTeachers)
                                                        .cmTeachers(cmTeachers)
                                                        .build();
                                })
                                .collect(Collectors.toList());
        }

        /** @param divisor number of groups — divides assigned hours when filtering by a specific group */
        private TeacherAssignmentDTO mapToTeacherAssignmentDTO(Assignment assignment, int divisor) {
                User teacher = assignment.getUser();
                int hours = assignment.getAssignedTimes() != null ? assignment.getAssignedTimes() : 0;

                return TeacherAssignmentDTO.builder()
                                .assignmentId(assignment.getId())
                                .teacherId(teacher.getId())
                                .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                                .lessonType(assignment.getLessonType())
                                .assignedHours(hours / divisor)
                                .build();
        }

        private AssignmentStatisticsDTO calculateStatistics(String program, String year, String className, Integer semester) {
                String yearParam      = nullIfBlank(year);
                String classParam     = nullIfBlank(className);
                String programParam = nullIfBlank(program);

                List<Assignment> assignments = assignmentRepository.findByFormationFlexible(yearParam, classParam, programParam);
                List<Resource> resources     = resourcesRepository.findWithFilters(yearParam, classParam, programParam, semester);

                Integer totalHours = assignments.stream()
                                .mapToInt(a -> a.getAssignedTimes() != null ? a.getAssignedTimes() : 0)
                                .sum();

                Map<String, Integer> hoursByLessonType = assignments.stream()
                                .collect(Collectors.groupingBy(
                                                Assignment::getLessonType,
                                                Collectors.summingInt(a -> a.getAssignedTimes() != null ? a.getAssignedTimes() : 0)));

                Map<String, Integer> hoursByTeacher = assignments.stream()
                                .collect(Collectors.groupingBy(
                                                a -> a.getUser().getFirstName() + " " + a.getUser().getLastName(),
                                                Collectors.summingInt(a -> a.getAssignedTimes() != null ? a.getAssignedTimes() : 0)));

                long unassignedModules = resources.stream()
                                .filter(r -> assignmentRepository.findByResourceId(r.getId()).isEmpty())
                                .count();

                return AssignmentStatisticsDTO.builder()
                                .totalTeachers((int) hoursByTeacher.size())
                                .totalAssignments(assignments.size())
                                .totalHoursAssigned(totalHours)
                                .unassignedModules((int) unassignedModules)
                                .hoursByLessonType(hoursByLessonType)
                                .hoursByTeacher(hoursByTeacher)
                                .build();
        }
}
