package sae.project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sae.project.dtos.assignment.AffectationRowDTO;
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
        private PedagogicalScheduleRepository ressourcesRepository;

        /**
         * Récupérer tous les enseignants avec leurs informations
         */
        public List<TeacherDTO> getAllTeachers() {
                log.info("Récupération de tous les enseignants");

                List<Object[]> teachersWithHours = assignmentRepository.getTeachersWithHours();

                return teachersWithHours.stream()
                                .map(this::mapToTeacherDTO)
                                .collect(Collectors.toList());
        }

        /**
         * Récupérer la grille d'affectation complète
         */
        public AssignmentGridDTO getAssignmentGrid(String formation, String year, String className, String semester) {
                log.info("Récupération de la grille d'affectation pour {} - {} - {} - semester={}", formation, year, className, semester);

                Integer semesterInt = null;
                if (semester != null && !semester.isEmpty()) {
                        semesterInt = Integer.parseInt(semester);
                }

                List<TeacherDTO> teachers = getAllTeachers();
                List<AffectationRowDTO> affectationGrid = getAffectationRows(formation, year, className, semesterInt);
                AssignmentStatisticsDTO statistics = calculateStatistics(formation, year, className, semesterInt);

                return AssignmentGridDTO.builder()
                                .selectedFormation(formation)
                                .selectedYear(year)
                                .availableTeachers(teachers)
                                .affectationGrid(affectationGrid)
                                .statistics(statistics)
                                .build();
        }

        /**
         * Créer une nouvelle affectation
         */
        public Assignment createAssignment(CreateAssignmentDTO dto) {
                log.info("Création d'une affectation pour user {} et ressource {}",
                                dto.getUserId(), dto.getResourceId());

                // Vérifier si l'affectation existe déjà
                Optional<Assignment> existing = assignmentRepository.findByUserIdAndResourceIdAndLessonType(
                                dto.getUserId(),
                                dto.getResourceId(),
                                dto.getLessonType());

                if (existing.isPresent()) {
                        throw new IllegalArgumentException(
                                        "Cet enseignant est déjà affecté à ce type de cours (" + dto.getLessonType()
                                                        + ") pour cette ressource");
                }

                // Récupérer l'enseignant et la ressource
                User teacher = userRepository.findById(dto.getUserId())
                                .orElseThrow(() -> new RuntimeException("Enseignant non trouvé"));

                Resource ressource = ressourcesRepository.findById(dto.getResourceId())
                                .orElseThrow(() -> new RuntimeException("Ressource non trouvée"));

                // Créer l'affectation
                Assignment assignment = new Assignment();
                assignment.setUser(teacher);
                assignment.setResource(ressource);
                assignment.setLessonType(dto.getLessonType());
                assignment.setAssignedTimes(dto.getAssignedTimes());

                return assignmentRepository.save(assignment);
        }

        /**
         * Mettre à jour une affectation
         */
        public Assignment updateAssignment(Integer id, CreateAssignmentDTO dto) {
                log.info("Mise à jour de l'affectation {}", id);

                return assignmentRepository.findById(id)
                                .map(assignment -> {
                                        assignment.setLessonType(dto.getLessonType());
                                        assignment.setAssignedTimes(dto.getAssignedTimes());
                                        return assignmentRepository.save(assignment);
                                })
                                .orElseThrow(() -> new RuntimeException("Affectation non trouvée"));
        }

        /**
         * Supprimer une affectation
         */
        public void deleteAssignment(Integer id) {
                log.info("Suppression de l'affectation {}", id);

                if (!assignmentRepository.existsById(id)) {
                        throw new RuntimeException("Affectation non trouvée");
                }

                assignmentRepository.deleteById(id);
        }

        /**
         * Supprimer une affectation par enseignant et ressource
         */
        public void deleteAssignmentByTeacherAndRessource(Integer userId, Integer ressourceId) {
                log.info("Suppression de l'affectation pour l'enseignant {} et la ressource {}", userId, ressourceId);

                Assignment assignment = assignmentRepository.findByUserIdAndResourceId(userId, ressourceId)
                                .orElseThrow(() -> new RuntimeException("Affectation non trouvée"));

                assignmentRepository.delete(assignment);
        }

        /**
         * Rechercher des enseignants
         */
        public List<TeacherDTO> searchTeachers(String keyword) {
                log.info("Recherche d'enseignants avec le mot-clé: {}", keyword);

                return getAllTeachers().stream()
                                .filter(teacher -> teacher.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                                                (teacher.getSubject() != null && teacher.getSubject().toLowerCase()
                                                                .contains(keyword.toLowerCase())))
                                .collect(Collectors.toList());
        }

        /**
         * Valider les affectations
         */
        public AssignmentValidationResponseDTO validateAssignments(String year, String className) {
                log.info("Validation des affectations pour {} - {}", year, className);

                List<String> errors = new ArrayList<>();
                List<String> warnings = new ArrayList<>();

                // Récupérer toutes les ressources de la formation
                List<Resource> ressources = ressourcesRepository.findByYearAndClass(year, className);
                // Note: validateAssignments validates all semesters

                // Vérifier les ressources non affectées
                for (Resource ressource : ressources) {
                        // Note: Updated findByRessourceId logic in repo if needed, assuming okay.
                        List<Assignment> assignments = assignmentRepository.findByResourceId(ressource.getId());

                        if (assignments.isEmpty()) {
                                warnings.add("La ressource '" + ressource.getTitle()
                                                + "' n'a aucun enseignant affecté");
                        }
                }

                // Vérifier la charge horaire des enseignants
                List<TeacherDTO> teachers = getAllTeachers();
                for (TeacherDTO teacher : teachers) {
                        if (teacher.getTotalHours() > 192) { // Exemple: max 192h par an
                                warnings.add("L'enseignant " + teacher.getName()
                                                + " dépasse la charge horaire maximale");
                        }
                }

                boolean success = errors.isEmpty();
                String message = success ? "Validation réussie avec " + warnings.size() + " avertissement(s)"
                                : "Validation échouée avec " + errors.size() + " erreur(s)";

                return AssignmentValidationResponseDTO.builder()
                                .success(success)
                                .message(message)
                                .errors(errors)
                                .warnings(warnings)
                                .build();
        }

        // ============ Méthodes privées ============

        /**
         * Mapper les données brutes en TeacherDTO
         */
        private TeacherDTO mapToTeacherDTO(Object[] data) {
                Integer id = (Integer) data[0];
                String firstName = (String) data[1];
                String lastName = (String) data[2];
                Integer totalHours = ((Number) data[3]).intValue();

                // Déterminer le statut (à adapter selon votre logique)
                String status = totalHours < 100 ? "Vacataire" : "Permanent";
                String statusColor = status.equals("Permanent") ? "bg-green-100 text-green-800"
                                : "bg-blue-100 text-blue-800";

                return TeacherDTO.builder()
                                .id(id)
                                .firstName(firstName)
                                .lastName(lastName)
                                .name(firstName + " " + lastName)
                                .subject("À définir") // À récupérer depuis une autre source
                                .status(status)
                                .totalHours(totalHours)
                                .remainingHours(192 - totalHours) // Exemple: 192h max
                                .statusColor(statusColor)
                                .build();
        }

        /**
         * Récupérer les lignes d'affectation pour une formation
         */
        private String nullIfBlank(String s) {
                return (s == null || s.isBlank()) ? null : s;
        }

        private List<AffectationRowDTO> getAffectationRows(String formation, String year, String className, Integer semester) {
                String yearParam       = nullIfBlank(year);
                String classParam      = nullIfBlank(className);
                String formationParam  = nullIfBlank(formation);
                boolean allClasses     = (classParam == null);

                List<Resource> ressources = ressourcesRepository.findWithFilters(yearParam, classParam, formationParam, semester);

                return ressources.stream()
                                .map(ressource -> {
                                        List<Assignment> assignments = assignmentRepository.findByResourceId(ressource.getId());

                                        // Nombre total de groupes partageant cette ressource (même formation/année)
                                        List<String> allGroupsForResource = ressource.getFormationList().stream()
                                                        .filter(f -> yearParam == null || yearParam.equals(f.getYear()))
                                                        .filter(f -> formationParam == null || formationParam.equals(f.getName()))
                                                        .map(Formation::getClassName)
                                                        .filter(cn -> cn != null)
                                                        .distinct()
                                                        .sorted()
                                                        .collect(Collectors.toList());
                                        int totalGroupCount = Math.max(1, allGroupsForResource.size());

                                        // En vue groupe spécifique, on divise les heures affectées par le nombre total de groupes
                                        int hoursDivisor = allClasses ? 1 : totalGroupCount;
                                        int displayGroupCount = allClasses ? totalGroupCount : 1;
                                        List<String> groupes = allClasses ? allGroupsForResource : null;

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

                                        return AffectationRowDTO.builder()
                                                        .resourceId(ressource.getId())
                                                        .module(ressource.getTitle())
                                                        .groupes(groupes)
                                                        .tdHours((ressource.getTdStateHours() != null ? ressource.getTdStateHours() : 0) * displayGroupCount)
                                                        .tpHours((ressource.getTpStateHours() != null ? ressource.getTpStateHours() : 0) * displayGroupCount)
                                                        .cmHours((ressource.getCmStateHours() != null ? ressource.getCmStateHours() : 0) * displayGroupCount)
                                                        .tdTeachers(tdTeachers)
                                                        .tpTeachers(tpTeachers)
                                                        .cmTeachers(cmTeachers)
                                                        .build();
                                })
                                .collect(Collectors.toList());
        }

        /**
         * Mapper une affectation en TeacherAssignmentDTO
         * @param divisor nombre de groupes : divise les heures affectées quand on filtre par groupe spécifique
         */
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

        /**
         * Calculer les statistiques
         */
        private AssignmentStatisticsDTO calculateStatistics(String formation, String year, String className, Integer semester) {
                String yearParam      = nullIfBlank(year);
                String classParam     = nullIfBlank(className);
                String formationParam = nullIfBlank(formation);

                List<Assignment> assignments = assignmentRepository.findByFormationFlexible(yearParam, classParam, formationParam);
                List<Resource> ressources    = ressourcesRepository.findWithFilters(yearParam, classParam, formationParam, semester);

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

                long unassignedModules = ressources.stream()
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
