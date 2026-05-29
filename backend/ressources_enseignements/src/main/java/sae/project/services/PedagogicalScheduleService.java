package sae.project.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sae.project.dtos.schedule.MonthDTO;
import sae.project.dtos.schedule.PedagogicalScheduleDTO;
import sae.project.dtos.schedule.ProjectScheduleDTO;
import sae.project.dtos.schedule.ResourceScheduleDTO;
import sae.project.dtos.schedule.ScheduleStatisticsDTO;
import sae.project.dtos.schedule.UpdateHoursDTO;
import sae.project.dtos.schedule.ValidationRequestDTO;
import sae.project.dtos.schedule.ValidationResponseDTO;
import sae.project.dtos.schedule.WeekDTO;
import sae.project.dtos.schedule.WeekHoursDTO;
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
     * Récupérer les ressources par année, classe et semestre
     */
    private String nullIfBlank(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }

    public List<ResourceScheduleDTO> getByYearAndClass(String year, String className, Integer semester, String program) {
        log.info("Récupération des ressources pour l'année {} la classe {} et le semestre {}", year, className, semester);
        List<Resource> resources = pedagogicalScheduleRepository.findWithFilters(
                nullIfBlank(year), nullIfBlank(className), nullIfBlank(program), semester);
        return resources.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer le planning complet
     */
    public PedagogicalScheduleDTO getCompleteSchedule(String year, String className, Integer semester, String program) {
        log.info("Récupération du planning complet pour {} - {} - semestre {}", year, className, semester);

        List<ResourceScheduleDTO> ressources = getByYearAndClass(year, className, semester, program);
        ProjectScheduleDTO project = getProjectData();
        List<MonthDTO> weeks = getWeeksForYearAndSemester(year, semester);
        ScheduleStatisticsDTO statistics = calculateStatistics(ressources, project, weeks);

        return PedagogicalScheduleDTO.builder()
                .selectedYear(year)
                .selectedClass(className)
                .selectedSemester(semester != null ? String.valueOf(semester) : null)
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
            for (UpdateHoursDTO ressourceDTO : validationRequest.getResources()) {
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
        Map<String, WeekHoursDTO> weekHours = ressource.getHoursPerWeek();
        int totalCM = weekHours.values().stream().mapToInt(w -> w.getCm() != null ? w.getCm() : 0).sum();
        int totalTD = weekHours.values().stream().mapToInt(w -> w.getTd() != null ? w.getTd() : 0).sum();
        int totalTP = weekHours.values().stream().mapToInt(w -> w.getTp() != null ? w.getTp() : 0).sum();

        return ResourceScheduleDTO.builder()
                .id(ressource.getId())
                .courseName(ressource.getTitle())
                .category(ressource.getCategory())
                .hoursPerWeek(weekHours)
                .hoursPerHalfGroup(ressource.getHoursPerHalfGroup())
                .totalHours(ressource.getTotalHours())
                .totalCM(totalCM)
                .totalTD(totalTD)
                .totalTP(totalTP)
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
            for (Map.Entry<String, WeekHoursDTO> entry : dto.getHoursPerWeek().entrySet()) {
                WeekHoursDTO w = entry.getValue();
                if (w.getCm() != null && w.getCm() < 0) {
                    errors.add("Les heures CM doivent être positives pour la semaine " + entry.getKey());
                }
                if (w.getTd() != null && w.getTd() < 0) {
                    errors.add("Les heures TD doivent être positives pour la semaine " + entry.getKey());
                }
                if (w.getTp() != null && w.getTp() < 0) {
                    errors.add("Les heures TP doivent être positives pour la semaine " + entry.getKey());
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

            // Sync CM/TD/TP totals
            Map<String, WeekHoursDTO> weekHours = dto.getHoursPerWeek();
            if (weekHours != null) {
                int totalCM = weekHours.values().stream().mapToInt(w -> w.getCm() != null ? w.getCm() : 0).sum();
                int totalTD = weekHours.values().stream().mapToInt(w -> w.getTd() != null ? w.getTd() : 0).sum();
                int totalTP = weekHours.values().stream().mapToInt(w -> w.getTp() != null ? w.getTp() : 0).sum();
                ressource.setCmStateHours(totalCM);
                ressource.setTdStateHours(totalTD);
                ressource.setTpStateHours(totalTP);
            }

            pedagogicalScheduleRepository.save(ressource);
        }
    }

    /**
     * Valider les données du projet
     */
    private void validateProjectData(UpdateHoursDTO projectDTO, List<String> errors) {
        // Logique de validation spécifique au projet
        if (projectDTO.getHoursPerWeek() != null) {
            for (Map.Entry<String, WeekHoursDTO> entry : projectDTO.getHoursPerWeek().entrySet()) {
                WeekHoursDTO w = entry.getValue();
                if (w.getCm() != null && w.getCm() < 0) {
                    errors.add("Les heures CM du projet doivent être positives pour la semaine " + entry.getKey());
                }
                if (w.getTd() != null && w.getTd() < 0) {
                    errors.add("Les heures TD du projet doivent être positives pour la semaine " + entry.getKey());
                }
                if (w.getTp() != null && w.getTp() < 0) {
                    errors.add("Les heures TP du projet doivent être positives pour la semaine " + entry.getKey());
                }
            }
        }
    }

    /**
     * Obtenir les données du projet SAE
     */
    private ProjectScheduleDTO getProjectData() {
        Map<String, WeekHoursDTO> defaultHours = new HashMap<>();
        defaultHours.put("1", WeekHoursDTO.builder().cm(2).td(3).tp(3).total(8).build());
        defaultHours.put("2", WeekHoursDTO.builder().cm(2).td(3).tp(3).total(8).build());
        defaultHours.put("3", WeekHoursDTO.builder().cm(2).td(3).tp(3).total(8).build());
        defaultHours.put("4", WeekHoursDTO.builder().cm(2).td(3).tp(3).total(8).build());

        return ProjectScheduleDTO.builder()
                .id("project-sae")
                .name("Projet SAE")
                .hoursPerWeek(defaultHours)
                .hoursPerHalfGroup(0)
                .totalHours(32)
                .build();
    }

    /**
     * Obtenir les semaines pour une année et un semestre donnés
     * Semestre 1 : semaines 1-20 (Sept-Jan)
     * Semestre 2 : semaines 21-40 (Fev-Jun)
     */
    private List<MonthDTO> getWeeksForYearAndSemester(String year, Integer semester) {
        List<MonthDTO> months = new ArrayList<>();
        boolean isAlternance = "3".equals(year);

        if (semester == null || semester == 1) {
            // Semestre 1 : Septembre - Janvier (semaines 1-20)
            months.add(createMonth("Septembre",
                    createWeeks(1, 4, isAlternance)));
            months.add(createMonth("Octobre",
                    createWeeks(5, 8, isAlternance)));
            months.add(createMonth("Novembre",
                    createWeeks(9, 12, isAlternance)));
            months.add(createMonth("Decembre",
                    createWeeks(13, 16, isAlternance)));
            months.add(createMonth("Janvier",
                    createWeeks(17, 20, isAlternance)));
        }

        if (semester == null || semester == 2) {
            // Semestre 2 : Février - Juin (semaines 21-40)
            months.add(createMonth("Fevrier",
                    createWeeks(21, 24, isAlternance)));
            months.add(createMonth("Mars",
                    createWeeks(25, 28, isAlternance)));
            months.add(createMonth("Avril",
                    createWeeks(29, 32, isAlternance)));
            months.add(createMonth("Mai",
                    createWeeks(33, 36, isAlternance)));
            months.add(createMonth("Juin",
                    createWeeks(37, 40, isAlternance)));
        }

        return months;
    }

    /**
     * Créer une liste de semaines entre start et end (inclus)
     * Pour l'alternance (année 3), les semaines paires sont en entreprise (S)
     */
    private List<WeekDTO> createWeeks(int start, int end, boolean isAlternance) {
        List<WeekDTO> weeks = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            String type = "E";
            if (isAlternance && i % 2 == 0) {
                type = "S";
            }
            weeks.add(new WeekDTO(i, String.valueOf(i), type));
        }
        return weeks;
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
        WeekHoursDTO defaultWeek = WeekHoursDTO.builder().cm(0).td(0).tp(0).total(0).build();
        weeks.stream()
                .flatMap(month -> month.getWeeks().stream())
                .forEach(week -> {
                    int weekTotal = ressources.stream()
                            .mapToInt(r -> {
                                WeekHoursDTO w = r.getHoursPerWeek().getOrDefault(String.valueOf(week.getNum()), defaultWeek);
                                return (w.getCm() != null ? w.getCm() : 0)
                                     + (w.getTd() != null ? w.getTd() : 0)
                                     + (w.getTp() != null ? w.getTp() : 0);
                            })
                            .sum();

                    weeklyTotals.put(week.getNum(), weekTotal);

                    WeekHoursDTO projectWeek = project.getHoursPerWeek().getOrDefault(String.valueOf(week.getNum()), defaultWeek);
                    int projectHours = (projectWeek.getCm() != null ? projectWeek.getCm() : 0)
                                     + (projectWeek.getTd() != null ? projectWeek.getTd() : 0)
                                     + (projectWeek.getTp() != null ? projectWeek.getTp() : 0);
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
