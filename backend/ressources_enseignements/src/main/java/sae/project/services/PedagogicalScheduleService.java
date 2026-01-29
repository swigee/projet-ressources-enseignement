package sae.project.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sae.project.dtos.*;
import sae.project.model.Resource;
import sae.project.repositories.PedagogicalScheduleRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class PedagogicalScheduleService {

    @Autowired
    private PedagogicalScheduleRepository pedagogicalScheduleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Récupérer toutes les ressources
     */
    public List<Resource> getAll() {
        log.info("Récupération de toutes les ressources");
        return pedagogicalScheduleRepository.findAll();
    }

    /**
     * Récupérer toutes les ressources avec mapping en DTO
     */
    public List<ResourceScheduleDTO> getAllAsDTO() {
        log.info("Récupération de toutes les ressources en DTO");
        return pedagogicalScheduleRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer une ressource par ID
     */
    public Optional<Resource> getById(Integer id) {
        log.info("Récupération de la ressource avec l'ID: {}", id);
        return pedagogicalScheduleRepository.findById(id);
    }

    /**
     * Récupérer les ressources par année et classe
     */
    public List<ResourceScheduleDTO> getByYearAndClass(String year, String className) {
        log.info("Récupération des ressources pour l'année {} et la classe {}", year, className);
        return pedagogicalScheduleRepository.findByYearAndClass(year, className).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer le planning complet
     */
    public PedagogicalScheduleDTO getCompleteSchedule(String year, String className) {
        log.info("Récupération du planning complet pour {} - {}", year, className);

        List<ResourceScheduleDTO> ressources = getByYearAndClass(year, className);
        ProjectScheduleDTO project = getProjectData();
        List<MonthDTO> weeks = getWeeksForYear(year);
        ScheduleStatisticsDTO statistics = calculateStatistics(ressources, project, weeks);

        return PedagogicalScheduleDTO.builder()
                .selectedYear(year)
                .selectedClass(className)
                .scheduleData(ressources)
                .projectData(project)
                .weeks(weeks)
                .statistics(statistics)
                .build();
    }

    /**
     * Créer une nouvelle ressource
     */
    public Resource create(Resource ressource) {
        log.info("Création d'une nouvelle ressource: {}", ressource.getTitle());

        // Validation
        if (ressource.getTitle() == null || ressource.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Le titre de la ressource est obligatoire");
        }

        return pedagogicalScheduleRepository.save(ressource);
    }

    /**
     * Mettre à jour une ressource
     */
    public Resource update(Integer id, Resource ressourceDetails) {
        log.info("Mise à jour de la ressource avec l'ID: {}", id);

        return pedagogicalScheduleRepository.findById(id)
                .map(ressource -> {
                    ressource.setTitle(ressourceDetails.getTitle());
                    ressource.setDescription(ressourceDetails.getDescription());
                    ressource.setCategory(ressourceDetails.getCategory());
                    ressource.setIsHighlighted(ressourceDetails.getIsHighlighted());
                    ressource.setHoursPerWeekJson(ressourceDetails.getHoursPerWeekJson());
                    ressource.setHoursPerHalfGroup(ressourceDetails.getHoursPerHalfGroup());

                    return pedagogicalScheduleRepository.save(ressource);
                })
                .orElseThrow(() -> new RuntimeException("Ressource non trouvée avec l'ID: " + id));
    }

    /**
     * Mettre à jour les heures d'une ressource
     */
    public Resource updateHours(Integer id, UpdateHoursDTO updateDTO) {
        log.info("Mise à jour des heures pour la ressource ID: {}", id);

        return pedagogicalScheduleRepository.findById(id)
                .map(ressource -> {
                    ressource.setHoursPerWeek(updateDTO.getHoursPerWeek());
                    ressource.setHoursPerHalfGroup(updateDTO.getHoursPerHalfGroup());

                    return pedagogicalScheduleRepository.save(ressource);
                })
                .orElseThrow(() -> new RuntimeException("Ressource non trouvée avec l'ID: " + id));
    }

    /**
     * Valider et sauvegarder un planning complet
     */
    public ValidationResponseDTO validateSchedule(ValidationRequestDTO validationRequest) {
        log.info("Validation du planning pour {} - {}",
                validationRequest.getSelectedYear(),
                validationRequest.getSelectedClass());

        List<String> errors = new ArrayList<>();

        try {
            // Validation des ressources
            for (UpdateHoursDTO ressourceDTO : validationRequest.getRessources()) {
                try {
                    validateAndUpdateRessource(ressourceDTO, errors);
                } catch (Exception e) {
                    errors.add("Erreur pour la ressource ID " + ressourceDTO.getResourceId() + ": " + e.getMessage());
                }
            }

            // Validation du projet
            if (validationRequest.getProject() != null) {
                try {
                    validateProjectData(validationRequest.getProject(), errors);
                } catch (Exception e) {
                    errors.add("Erreur pour le projet: " + e.getMessage());
                }
            }

            if (errors.isEmpty()) {
                return ValidationResponseDTO.builder()
                        .success(true)
                        .message("Planning validé et sauvegardé avec succès")
                        .build();
            } else {
                return ValidationResponseDTO.builder()
                        .success(false)
                        .message("Erreurs détectées lors de la validation")
                        .errors(errors)
                        .build();
            }

        } catch (Exception e) {
            log.error("Erreur lors de la validation du planning", e);
            return ValidationResponseDTO.builder()
                    .success(false)
                    .message("Erreur interne lors de la validation")
                    .errors(List.of(e.getMessage()))
                    .build();
        }
    }

    /**
     * Supprimer une ressource
     */
    public void delete(Integer id) {
        log.info("Suppression de la ressource avec l'ID: {}", id);

        if (!pedagogicalScheduleRepository.existsById(id)) {
            throw new RuntimeException("Ressource non trouvée avec l'ID: " + id);
        }

        pedagogicalScheduleRepository.deleteById(id);
    }

    /**
     * Rechercher des ressources par mot-clé
     */
    public List<ResourceScheduleDTO> searchByKeyword(String keyword) {
        log.info("Recherche de ressources avec le mot-clé: {}", keyword);
        return pedagogicalScheduleRepository.searchByTitleContaining(keyword).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ============ Méthodes privées ============

    /**
     * Mapper une entité Ressources en DTO
     */
    private ResourceScheduleDTO mapToDTO(Resource ressource) {
        return ResourceScheduleDTO.builder()
                .id(ressource.getId())
                .courseName(ressource.getTitle())
                .category(ressource.getCategory())
                .isHighlighted(ressource.getIsHighlighted())
                .hoursPerWeek(ressource.getHoursPerWeek())
                .hoursPerHalfGroup(ressource.getHoursPerHalfGroup())
                .totalHours(ressource.getTotalHours())
                .build();
    }

    /**
     * Valider et mettre à jour une ressource
     */
    private void validateAndUpdateRessource(UpdateHoursDTO dto, List<String> errors) {
        Resource ressource = pedagogicalScheduleRepository.findById(dto.getResourceId())
                .orElseThrow(() -> new RuntimeException("Ressource non trouvée"));

        // Validation des heures
        if (dto.getHoursPerWeek() != null) {
            for (Map.Entry<String, Integer> entry : dto.getHoursPerWeek().entrySet()) {
                if (entry.getValue() < 0) {
                    errors.add("Les heures doivent être positives pour la semaine " + entry.getKey());
                }
            }
        }

        if (dto.getHoursPerHalfGroup() != null && dto.getHoursPerHalfGroup() < 0) {
            errors.add("Les heures par demi-groupe doivent être positives");
        }

        // Mise à jour si pas d'erreurs
        if (errors.isEmpty()) {
            ressource.setHoursPerWeek(dto.getHoursPerWeek());
            ressource.setHoursPerHalfGroup(dto.getHoursPerHalfGroup());
            pedagogicalScheduleRepository.save(ressource);
        }
    }

    /**
     * Valider les données du projet
     */
    private void validateProjectData(UpdateHoursDTO projectDTO, List<String> errors) {
        // Logique de validation spécifique au projet
        if (projectDTO.getHoursPerWeek() != null) {
            for (Map.Entry<String, Integer> entry : projectDTO.getHoursPerWeek().entrySet()) {
                if (entry.getValue() < 0) {
                    errors.add("Les heures du projet doivent être positives pour la semaine " + entry.getKey());
                }
            }
        }
    }

    /**
     * Obtenir les données du projet SAE
     */
    private ProjectScheduleDTO getProjectData() {
        Map<String, Integer> defaultHours = new HashMap<>();
        defaultHours.put("1", 8);
        defaultHours.put("2", 8);
        defaultHours.put("3", 8);
        defaultHours.put("4", 8);

        return ProjectScheduleDTO.builder()
                .id("project-sae")
                .name("Projet SAE")
                .hoursPerWeek(defaultHours)
                .hoursPerHalfGroup(0)
                .totalHours(32)
                .build();
    }

    /**
     * Obtenir les semaines pour une année donnée
     */
    private List<MonthDTO> getWeeksForYear(String year) {
        List<MonthDTO> months = new ArrayList<>();

        // Exemple pour l'année 3 (avec alternance)
        if ("3".equals(year)) {
            months.add(createMonth("Septembre 2024",
                    List.of(
                            new WeekDTO(1, "02", "E"),
                            new WeekDTO(2, "09", "E"),
                            new WeekDTO(3, "16", "E"),
                            new WeekDTO(4, "23", "E"))));

            months.add(createMonth("Octobre 2024",
                    List.of(
                            new WeekDTO(5, "30", "E"),
                            new WeekDTO(6, "07", "S"),
                            new WeekDTO(7, "14", "E"),
                            new WeekDTO(8, "21", "S"))));
        } else {
            // Années 1 et 2 (sans alternance)
            months.add(createMonth("Septembre 2024",
                    List.of(
                            new WeekDTO(1, "02", "E"),
                            new WeekDTO(2, "09", "E"),
                            new WeekDTO(3, "16", "E"),
                            new WeekDTO(4, "23", "E"))));
        }

        return months;
    }

    /**
     * Créer un mois avec ses semaines
     */
    private MonthDTO createMonth(String monthName, List<WeekDTO> weeks) {
        return MonthDTO.builder()
                .month(monthName)
                .weeks(weeks)
                .build();
    }

    /**
     * Calculer les statistiques du planning
     */
    private ScheduleStatisticsDTO calculateStatistics(
            List<ResourceScheduleDTO> ressources,
            ProjectScheduleDTO project,
            List<MonthDTO> weeks) {

        Integer totalResources = ressources.stream()
                .mapToInt(ResourceScheduleDTO::getTotalHours)
                .sum();

        Integer totalWithProject = totalResources + project.getTotalHours();

        long companyWeeksCount = weeks.stream()
                .flatMap(month -> month.getWeeks().stream())
                .filter(week -> "S".equals(week.getType()))
                .count();

        Map<Integer, Integer> weeklyTotals = new HashMap<>();
        Map<Integer, Integer> weeklyTotalsWithProject = new HashMap<>();

        // Calculer les totaux par semaine
        weeks.stream()
                .flatMap(month -> month.getWeeks().stream())
                .forEach(week -> {
                    int weekTotal = ressources.stream()
                            .mapToInt(r -> r.getHoursPerWeek().getOrDefault(String.valueOf(week.getNum()), 0))
                            .sum();

                    weeklyTotals.put(week.getNum(), weekTotal);

                    int projectHours = project.getHoursPerWeek().getOrDefault(String.valueOf(week.getNum()), 0);
                    weeklyTotalsWithProject.put(week.getNum(), weekTotal + projectHours);
                });

        return ScheduleStatisticsDTO.builder()
                .totalResources(totalResources)
                .totalWithProject(totalWithProject)
                .companyWeeksCount((int) companyWeeksCount)
                .weeklyTotals(weeklyTotals)
                .weeklyTotalsWithProject(weeklyTotalsWithProject)
                .build();
    }
}