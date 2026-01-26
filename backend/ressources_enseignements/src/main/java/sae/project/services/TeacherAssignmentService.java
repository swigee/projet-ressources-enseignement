package sae.project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sae.project.dtos.*;
import sae.project.model.Assignment;
import sae.project.model.Ressources;
import sae.project.model.Users;
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
    public AssignmentGridDTO getAssignmentGrid(String formation, String year, String className) {
        log.info("Récupération de la grille d'affectation pour {} - {} - {}", formation, year, className);

        List<TeacherDTO> teachers = getAllTeachers();
        List<AffectationRowDTO> affectationGrid = getAffectationRows(year, className);
        AssignmentStatisticsDTO statistics = calculateStatistics(year, className);

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
        log.info("Création d'une affectation pour l'enseignant {} sur la ressource {}",
                dto.getUserId(), dto.getRessourceId());

        // Vérifier si l'affectation existe déjà
        Optional<Assignment> existing = assignmentRepository.findByUserIdAndRessourceIdAndLessonType(
                dto.getUserId(),
                dto.getRessourceId(),
                dto.getLessonType()
        );

        if (existing.isPresent()) {
            throw new IllegalArgumentException(
                    "Cet enseignant est déjà affecté à ce type de cours (" + dto.getLessonType() + ") pour cette ressource"
            );
        }

        // Récupérer l'enseignant et la ressource
        Users teacher = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Enseignant non trouvé"));

        Ressources ressource = ressourcesRepository.findById(dto.getRessourceId())
                .orElseThrow(() -> new RuntimeException("Ressource non trouvée"));

        // Créer l'affectation
        Assignment assignment = new Assignment();
        assignment.setIduser(teacher);
        assignment.setIdressource(ressource);
        assignment.setLessontype(dto.getLessonType());
        assignment.setAssignedtimes(dto.getAssignedTimes());

        return assignmentRepository.save(assignment);
    }

    /**
     * Mettre à jour une affectation
     */
    public Assignment updateAssignment(Integer id, CreateAssignmentDTO dto) {
        log.info("Mise à jour de l'affectation {}", id);

        return assignmentRepository.findById(id)
                .map(assignment -> {
                    assignment.setLessontype(dto.getLessonType());
                    assignment.setAssignedtimes(dto.getAssignedTimes());
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

        Assignment assignment = assignmentRepository.findByUserIdAndRessourceId(userId, ressourceId)
                .orElseThrow(() -> new RuntimeException("Affectation non trouvée"));

        assignmentRepository.delete(assignment);
    }

    /**
     * Rechercher des enseignants
     */
    public List<TeacherDTO> searchTeachers(String keyword) {
        log.info("Recherche d'enseignants avec le mot-clé: {}", keyword);

        return getAllTeachers().stream()
                .filter(teacher ->
                        teacher.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                                (teacher.getSubject() != null && teacher.getSubject().toLowerCase().contains(keyword.toLowerCase()))
                )
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
        List<Ressources> ressources = ressourcesRepository.findByYearAndClass(year, className);

        // Vérifier les ressources non affectées
        for (Ressources ressource : ressources) {
            List<Assignment> assignments = assignmentRepository.findByRessourceId(ressource.getIdressource());

            if (assignments.isEmpty()) {
                warnings.add("La ressource '" + ressource.getTitle() + "' n'a aucun enseignant affecté");
            }
        }

        // Vérifier la charge horaire des enseignants
        List<TeacherDTO> teachers = getAllTeachers();
        for (TeacherDTO teacher : teachers) {
            if (teacher.getTotalHours() > 192) { // Exemple: max 192h par an
                warnings.add("L'enseignant " + teacher.getName() + " dépasse la charge horaire maximale");
            }
        }

        boolean success = errors.isEmpty();
        String message = success ?
                "Validation réussie avec " + warnings.size() + " avertissement(s)" :
                "Validation échouée avec " + errors.size() + " erreur(s)";

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
        String statusColor = status.equals("Permanent") ?
                "bg-green-100 text-green-800" : "bg-blue-100 text-blue-800";

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
    private List<AffectationRowDTO> getAffectationRows(String year, String className) {
        List<Ressources> ressources = ressourcesRepository.findByYearAndClass(year, className);

        return ressources.stream()
                .map(ressource -> {
                    // Récupérer toutes les affectations pour cette ressource
                    List<Assignment> assignments = assignmentRepository.findByRessourceId(ressource.getIdressource());

                    // Filtrer par type de cours
                    List<TeacherAssignmentDTO> tdTeachers = assignments.stream()
                            .filter(a -> "TD".equals(a.getLessontype()))
                            .map(this::mapToTeacherAssignmentDTO)
                            .collect(Collectors.toList());

                    List<TeacherAssignmentDTO> tpTeachers = assignments.stream()
                            .filter(a -> "TP".equals(a.getLessontype()))
                            .map(this::mapToTeacherAssignmentDTO)
                            .collect(Collectors.toList());

                    List<TeacherAssignmentDTO> cmTeachers = assignments.stream()
                            .filter(a -> "CM".equals(a.getLessontype()))
                            .map(this::mapToTeacherAssignmentDTO)
                            .collect(Collectors.toList());

                    return AffectationRowDTO.builder()
                            .ressourceId(ressource.getIdressource())
                            .module(ressource.getTitle())
                            .tdHours(ressource.getHeureTdIut())
                            .tpHours(ressource.getHeureTpIut())
                            .cmHours(ressource.getHeureCmIut())
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
        Users teacher = assignment.getIduser();

        return TeacherAssignmentDTO.builder()
                .assignmentId(assignment.getIdAssignment())
                .teacherId(teacher.getIduser())
                .teacherName(teacher.getFirstname() + " " + teacher.getLastname())
                .lessonType(assignment.getLessontype())
                .assignedHours(assignment.getAssignedtimes())
                .build();
    }

    /**
     * Calculer les statistiques
     */
    private AssignmentStatisticsDTO calculateStatistics(String year, String className) {
        List<Assignment> assignments = assignmentRepository.findByFormation(year, className);

        Integer totalHours = assignments.stream()
                .mapToInt(a -> a.getAssignedtimes() != null ? a.getAssignedtimes() : 0)
                .sum();

        Map<String, Integer> hoursByLessonType = assignments.stream()
                .collect(Collectors.groupingBy(
                        Assignment::getLessontype,
                        Collectors.summingInt(a -> a.getAssignedtimes() != null ? a.getAssignedtimes() : 0)
                ));

        Map<String, Integer> hoursByTeacher = assignments.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getIduser().getFirstname() + " " + a.getIduser().getLastname(),
                        Collectors.summingInt(a -> a.getAssignedtimes() != null ? a.getAssignedtimes() : 0)
                ));

        List<Ressources> ressources = ressourcesRepository.findByYearAndClass(year, className);
        long unassignedModules = ressources.stream()
                .filter(r -> assignmentRepository.findByRessourceId(r.getIdressource()).isEmpty())
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