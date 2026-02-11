package sae.project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sae.project.dtos.ressources.RessourceRowDTO;
import sae.project.dtos.ressources.RessourcesResponseDTO;
import sae.project.dtos.ressources.ScheduleConflictDTO;
import sae.project.dtos.ressources.TeacherBadgeDTO;
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
@Transactional(readOnly = true)
public class RessourcesService {

    @Autowired
    private PedagogicalScheduleRepository ressourcesRepository;

    @Autowired
    private TeacherAssignmentRepository assignmentRepository;


    /**
     * Récupérer les données du tableau de ressources pour une année, classe et semestre
     */
    public RessourcesResponseDTO getRessourcesTableData(String year, String className, Integer semester) {
        log.info("Récupération des données du tableau pour year={} className={} semester={}", year, className, semester);

        List<Resource> resources = ressourcesRepository.findByYearAndClassAndSemester(year, className, semester);
        List<RessourceRowDTO> ressourceRows = resources.stream()
                .map(this::mapResourceToRowDTO)
                .collect(Collectors.toList());

        List<TeacherBadgeDTO> availableTeachers = getAvailableTeachers();
        List<ScheduleConflictDTO> conflicts = new ArrayList<>();

        return RessourcesResponseDTO.builder()
                .ressources(ressourceRows)
                .availableTeachers(availableTeachers)
                .conflicts(conflicts)
                .build();
    }

    /**
     * Récupérer tous les enseignants disponibles avec leurs heures
     */
    public List<TeacherBadgeDTO> getAvailableTeachers() {
        log.info("Récupération de tous les enseignants disponibles");

        List<Object[]> teachersWithHours = assignmentRepository.getTeachersWithHours();

        return teachersWithHours.stream()
                .map(row -> {
                    Integer teacherId = (Integer) row[0];
                    String firstName = (String) row[1];
                    String lastName = (String) row[2];
                    Integer totalHours = ((Number) row[3]).intValue();

                    String fullName = (lastName != null ? lastName.toUpperCase() : "") + " " +
                            (firstName != null ? firstName : "");

                    return TeacherBadgeDTO.builder()
                            .teacherId(teacherId)
                            .fullName(fullName.trim())
                            .assignedHours(totalHours)
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * Détecter les conflits d'emploi du temps pour un enseignant
     */
    public List<ScheduleConflictDTO> detectConflicts(Integer teacherId) {
        log.info("Détection des conflits pour l'enseignant {}", teacherId);

        List<Assignment> assignments = assignmentRepository.findByUserId(teacherId);

        if (assignments.isEmpty()) {
            return new ArrayList<>();
        }

        // Grouper les affectations par semaine
        Map<Integer, List<Assignment>> assignmentsByWeek = new HashMap<>();

        for (Assignment assignment : assignments) {
            Resource resource = assignment.getResource();
            if (resource != null) {
                Map<String, ?> hoursPerWeek = resource.getHoursPerWeek();
                for (String weekStr : hoursPerWeek.keySet()) {
                    try {
                        Integer week = Integer.parseInt(weekStr);
                        assignmentsByWeek.computeIfAbsent(week, k -> new ArrayList<>()).add(assignment);
                    } catch (NumberFormatException e) {
                        // Ignorer les clés non numériques
                    }
                }
            }
        }

        // Détecter les conflits (2+ ressources dans la même semaine)
        List<ScheduleConflictDTO> conflicts = new ArrayList<>();
        User teacher = assignments.get(0).getUser();
        String teacherName = (teacher.getLastName() != null ? teacher.getLastName().toUpperCase() : "") + " " +
                (teacher.getFirstName() != null ? teacher.getFirstName() : "");

        for (Map.Entry<Integer, List<Assignment>> entry : assignmentsByWeek.entrySet()) {
            if (entry.getValue().size() >= 2) {
                List<String> conflictingModules = entry.getValue().stream()
                        .map(a -> a.getResource().getTitle())
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                conflicts.add(ScheduleConflictDTO.builder()
                        .teacherId(teacherId)
                        .teacherName(teacherName.trim())
                        .conflictingModules(conflictingModules)
                        .weekNumber(entry.getKey())
                        .timeSlot("Semaine " + entry.getKey())
                        .build());
            }
        }

        return conflicts;
    }

    /**
     * Rechercher des ressources par mot-clé
     */
    public List<RessourceRowDTO> searchRessources(String keyword) {
        log.info("Recherche de ressources avec le mot-clé: {}", keyword);

        List<Resource> resources = ressourcesRepository.searchByTitleContaining(keyword);

        return resources.stream()
                .map(this::mapResourceToRowDTO)
                .collect(Collectors.toList());
    }

    // ============ Méthodes privées ============

    /**
     * Mapper une Resource vers RessourceRowDTO
     */
    private RessourceRowDTO mapResourceToRowDTO(Resource resource) {
        // Calculer les heures prévisionnelles (State = prévisionnel)
        Integer heuresPrevisionnelles = safeSum(
                resource.getTdStateHours(),
                resource.getTpStateHours(),
                resource.getCmStateHours()
        );

        // Calculer les heures réelles (IUT = réel)
        Integer heuresReelles = safeSum(
                resource.getTdIutHours(),
                resource.getTpIutHours(),
                resource.getCmIutHours()
        );

        // Récupérer les enseignants affectés à cette ressource
        List<Assignment> assignments = assignmentRepository.findByResourceId(resource.getId());
        List<TeacherBadgeDTO> assignedTeachers = assignments.stream()
                .map(this::mapAssignmentToTeacherBadge)
                .collect(Collectors.toList());

        return RessourceRowDTO.builder()
                .id(resource.getId())
                .moduleName(resource.getTitle())
                .category(resource.getCategory())
                .heuresPrevisionnelles(heuresPrevisionnelles)
                .heuresReelles(heuresReelles)
                .heuresTD(resource.getTdStateHours() != null ? resource.getTdStateHours() : 0)
                .heuresTP(resource.getTpStateHours() != null ? resource.getTpStateHours() : 0)
                .heuresCM(resource.getCmStateHours() != null ? resource.getCmStateHours() : 0)
                .assignedTeachers(assignedTeachers)
                .build();
    }

    /**
     * Mapper une Assignment vers TeacherBadgeDTO
     */
    private TeacherBadgeDTO mapAssignmentToTeacherBadge(Assignment assignment) {
        User user = assignment.getUser();

        String fullName = "";
        if (user != null) {
            String lastName = user.getLastName() != null ? user.getLastName().toUpperCase() : "";
            String firstName = user.getFirstName() != null ? user.getFirstName() : "";
            fullName = (lastName + " " + firstName).trim();
        }

        return TeacherBadgeDTO.builder()
                .teacherId(user != null ? user.getId() : null)
                .fullName(fullName)
                .assignedHours(assignment.getAssignedTimes() != null ? assignment.getAssignedTimes() : 0)
                .build();
    }

    /**
     * Addition sécurisée de valeurs nullables
     */
    private Integer safeSum(Integer... values) {
        int sum = 0;
        for (Integer value : values) {
            if (value != null) {
                sum += value;
            }
        }
        return sum;
    }
}
