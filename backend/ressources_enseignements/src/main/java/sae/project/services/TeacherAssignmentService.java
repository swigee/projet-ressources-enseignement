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
                List<AffectationRowDTO> affectationGrid = getAffectationRows(year, className, semesterInt);
                AssignmentStatisticsDTO statistics = calculateStatistics(year, className, semesterInt);

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
        private List<AffectationRowDTO> getAffectationRows(String year, String className, Integer semester) {
                List<Resource> ressources;
                if (semester != null) {
                        ressources = ressourcesRepository.findByYearAndClassAndSemester(year, className, semester);
                } else {
                        ressources = ressourcesRepository.findByYearAndClass(year, className);
                }

                return ressources.stream()
                                .map(ressource -> {
                                        // Récupérer toutes les affectations pour cette ressource
                                        List<Assignment> assignments = assignmentRepository
                                                        .findByResourceId(ressource.getId());

                                        // Filtrer par type de cours
                                        List<TeacherAssignmentDTO> tdTeachers = assignments.stream()
                                                        .filter(a -> "TD".equals(a.getLessonType()))
                                                        .map(this::mapToTeacherAssignmentDTO)
                                                        .collect(Collectors.toList());

                                        List<TeacherAssignmentDTO> tpTeachers = assignments.stream()
                                                        .filter(a -> "TP".equals(a.getLessonType()))
                                                        .map(this::mapToTeacherAssignmentDTO)
                                                        .collect(Collectors.toList());

                                        List<TeacherAssignmentDTO> cmTeachers = assignments.stream()
                                                        .filter(a -> "CM".equals(a.getLessonType()))
                                                        .map(this::mapToTeacherAssignmentDTO)
                                                        .collect(Collectors.toList());

                                        return AffectationRowDTO.builder()
                                                        .resourceId(ressource.getId())
                                                        .module(ressource.getTitle())
                                                        .tdHours(ressource.getTdIutHours())
                                                        .tpHours(ressource.getTpIutHours())
                                                        .cmHours(ressource.getCmIutHours())
                                                        .tdTeachers(tdTeachers)
                                                        .tpTeachers(tpTeachers)
                                                        .cmTeachers(cmTeachers)
                                                        .build();
                                })
                                .collect(Collectors.toList());
        }

        /**
         * Mapper une affectation en TeacherAssignmentDTO
         */
        private TeacherAssignmentDTO mapToTeacherAssignmentDTO(Assignment assignment) {
                User teacher = assignment.getUser();

                return TeacherAssignmentDTO.builder()
                                .assignmentId(assignment.getId())
                                .teacherId(teacher.getId())
                                .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                                .lessonType(assignment.getLessonType())
                                .assignedHours(assignment.getAssignedTimes())
                                .build();
        }

        /**
         * Calculer les statistiques
         */
        private AssignmentStatisticsDTO calculateStatistics(String year, String className, Integer semester) {
                List<Assignment> assignments = assignmentRepository.findByFormation(year, className);

                Integer totalHours = assignments.stream()
                                .mapToInt(a -> a.getAssignedTimes() != null ? a.getAssignedTimes() : 0)
                                .sum();

                Map<String, Integer> hoursByLessonType = assignments.stream()
                                .collect(Collectors.groupingBy(
                                                Assignment::getLessonType,
                                                Collectors.summingInt(
                                                                a -> a.getAssignedTimes() != null ? a.getAssignedTimes()
                                                                                : 0)));

                Map<String, Integer> hoursByTeacher = assignments.stream()
                                .collect(Collectors.groupingBy(
                                                a -> a.getUser().getFirstName() + " " + a.getUser().getLastName(),
                                                Collectors.summingInt(
                                                                a -> a.getAssignedTimes() != null ? a.getAssignedTimes()
                                                                                : 0)));

                List<Resource> ressources;
                if (semester != null) {
                        ressources = ressourcesRepository.findByYearAndClassAndSemester(year, className, semester);
                } else {
                        ressources = ressourcesRepository.findByYearAndClass(year, className);
                }
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
