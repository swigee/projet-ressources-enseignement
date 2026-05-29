package sae.project.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sae.project.dtos.schedule.PedagogicalScheduleDTO;
import sae.project.dtos.schedule.ResourceScheduleDTO;
import sae.project.dtos.schedule.UpdateHoursDTO;
import sae.project.dtos.schedule.ValidationRequestDTO;
import sae.project.dtos.schedule.ValidationResponseDTO;
import sae.project.model.Resource;
import sae.project.services.PedagogicalScheduleService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/pedagogical-schedule")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@Slf4j
@Tag(name = "Pedagogical Schedule", description = "API for managing the pedagogical schedule")
public class PedagogicalScheduleController {

    @Autowired
    private PedagogicalScheduleService pedagogicalScheduleService;

    /**
     * Récupérer toutes les ressources
     */
    @GetMapping("/ressources")
    @Operation(summary = "Get all resources")
    public ResponseEntity<List<Resource>> getAllRessources() {
        log.info("GET /api/pedagogical-schedule/ressources");
        try {
            List<Resource> ressources = pedagogicalScheduleService.getAll();
            return ResponseEntity.ok(ressources);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des ressources", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupérer toutes les ressources en DTO
     */
    @GetMapping("/ressources/dto")
    @Operation(summary = "Get all resources as DTO")
    public ResponseEntity<List<ResourceScheduleDTO>> getAllRessourcesDTO() {
        log.info("GET /api/pedagogical-schedule/ressources/dto");
        try {
            List<ResourceScheduleDTO> ressources = pedagogicalScheduleService.getAllAsDTO();
            return ResponseEntity.ok(ressources);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des ressources DTO", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupérer une ressource par ID
     */
    @GetMapping("/ressources/{id}")
    @Operation(summary = "Get a resource by ID")
    public ResponseEntity<Resource> getRessourceById(
            @Parameter(description = "Resource ID") @PathVariable Integer id) {
        log.info("GET /api/pedagogical-schedule/ressources/{}", id);
        try {
            return pedagogicalScheduleService.getById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Erreur lors de la récupération de la ressource {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupérer les ressources par année et classe
     */
    @GetMapping("/ressources/filter")
    @Operation(summary = "Get resources filtered by year, class and semester")
    public ResponseEntity<List<ResourceScheduleDTO>> getRessourcesByFilter(
            @Parameter(description = "Study year") @RequestParam(required = false) String year,
            @Parameter(description = "Class name") @RequestParam(required = false) String className,
            @Parameter(description = "Semester (1 or 2)") @RequestParam(required = false) Integer semester,
            @Parameter(description = "Formation name") @RequestParam(required = false) String formation) {
        log.info("GET /api/pedagogical-schedule/ressources/filter?year={}&className={}&semester={}", year, className, semester);
        try {
            List<ResourceScheduleDTO> ressources = pedagogicalScheduleService.getByYearAndClass(year, className, semester, formation);
            return ResponseEntity.ok(ressources);
        } catch (Exception e) {
            log.error("Erreur lors du filtrage des ressources", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupérer le planning complet
     */
    @GetMapping("/schedule")
    @Operation(summary = "Get the complete schedule with statistics")
    public ResponseEntity<PedagogicalScheduleDTO> getCompleteSchedule(
            @Parameter(description = "Study year") @RequestParam(required = false) String year,
            @Parameter(description = "Class name") @RequestParam(required = false) String className,
            @Parameter(description = "Semester (1 or 2)") @RequestParam(required = false) Integer semester,
            @Parameter(description = "Formation name") @RequestParam(required = false) String formation) {
        log.info("GET /api/pedagogical-schedule/schedule?year={}&className={}&semester={}", year, className, semester);
        try {
            PedagogicalScheduleDTO schedule = pedagogicalScheduleService.getCompleteSchedule(year, className, semester, formation);
            return ResponseEntity.ok(schedule);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération du planning complet", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Créer une nouvelle ressource
     */
    @PostMapping("/ressources")
    @Operation(summary = "Create a new resource")
    public ResponseEntity<Resource> createRessource(
            @Valid @RequestBody Resource ressource) {
        log.info("POST /api/pedagogical-schedule/ressources");
        try {
            Resource created = pedagogicalScheduleService.create(ressource);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            log.error("Données invalides pour la création de la ressource", e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Erreur lors de la création de la ressource", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Mettre à jour une ressource complète
     */
    @PutMapping("/ressources/{id}")
    @Operation(summary = "Fully update a resource")
    public ResponseEntity<Resource> updateRessource(
            @Parameter(description = "Resource ID") @PathVariable Integer id,
            @Valid @RequestBody Resource ressource) {
        log.info("PUT /api/pedagogical-schedule/ressources/{}", id);
        try {
            Resource updated = pedagogicalScheduleService.update(id, ressource);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            log.error("Ressource non trouvée: {}", id, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour de la ressource {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Mettre à jour les heures d'une ressource
     */
    @PatchMapping("/ressources/{id}/hours")
    @Operation(summary = "Update the hours of a resource")
    public ResponseEntity<Resource> updateRessourceHours(
            @Parameter(description = "Resource ID") @PathVariable Integer id,
            @Valid @RequestBody UpdateHoursDTO updateDTO) {
        log.info("PATCH /api/pedagogical-schedule/ressources/{}/hours", id);
        try {
            Resource updated = pedagogicalScheduleService.updateHours(id, updateDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            log.error("Ressource non trouvée: {}", id, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour des heures", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Valider et sauvegarder un planning complet
     */
    @PostMapping("/schedule/validate")
    @Operation(summary = "Validate and save a complete schedule")
    public ResponseEntity<ValidationResponseDTO> validateSchedule(
            @Valid @RequestBody ValidationRequestDTO validationRequest) {
        log.info("POST /api/pedagogical-schedule/schedule/validate");
        try {
            ValidationResponseDTO response = pedagogicalScheduleService.validateSchedule(validationRequest);

            if (response.getSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            log.error("Erreur lors de la validation du planning", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ValidationResponseDTO.builder()
                            .success(false)
                            .message("Erreur interne du serveur")
                            .errors(List.of(e.getMessage()))
                            .build());
        }
    }

    /**
     * Supprimer une ressource
     */
    @DeleteMapping("/ressources/{id}")
    @Operation(summary = "Delete a resource")
    public ResponseEntity<Void> deleteRessource(
            @Parameter(description = "Resource ID") @PathVariable Integer id) {
        log.info("DELETE /api/pedagogical-schedule/ressources/{}", id);
        try {
            pedagogicalScheduleService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Ressource non trouvée: {}", id, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Erreur lors de la suppression de la ressource {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Rechercher des ressources par mot-clé
     */
    @GetMapping("/ressources/search")
    @Operation(summary = "Search resources by keyword")
    public ResponseEntity<List<ResourceScheduleDTO>> searchRessources(
            @Parameter(description = "Search keyword") @RequestParam String keyword) {
        log.info("GET /api/pedagogical-schedule/ressources/search?keyword={}", keyword);
        try {
            List<ResourceScheduleDTO> ressources = pedagogicalScheduleService.searchByKeyword(keyword);
            return ResponseEntity.ok(ressources);
        } catch (Exception e) {
            log.error("Erreur lors de la recherche de ressources", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    @Operation(summary = "Check API health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Pedagogical Schedule API is running");
    }
}
