package sae.project.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sae.project.dtos.assignment.AssignmentGridDTO;
import sae.project.dtos.assignment.AssignmentValidationResponseDTO;
import sae.project.dtos.assignment.CreateAssignmentDTO;
import sae.project.dtos.teacher.TeacherDTO;
import sae.project.model.Assignment;
import sae.project.services.TeacherAssignmentService;

import java.util.List;

@RestController
@RequestMapping("/api/teacher-assignment")
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class TeacherAssignmentController {

    @Autowired
    private TeacherAssignmentService assignmentService;

    /**
     * Récupérer tous les enseignants
     * GET /api/teacher-assignment/teachers
     */
    @GetMapping("/teachers")
    public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
        log.info("GET /api/teacher-assignment/teachers");
        List<TeacherDTO> teachers = assignmentService.getAllTeachers();
        return ResponseEntity.ok(teachers);
    }

    /**
     * Rechercher des enseignants
     * GET /api/teacher-assignment/teachers/search?keyword=martin
     */
    @GetMapping("/teachers/search")
    public ResponseEntity<List<TeacherDTO>> searchTeachers(@RequestParam String keyword) {
        log.info("GET /api/teacher-assignment/teachers/search?keyword={}", keyword);
        List<TeacherDTO> teachers = assignmentService.searchTeachers(keyword);
        return ResponseEntity.ok(teachers);
    }

    /**
     * Récupérer la grille d'affectation complète
     * GET /api/teacher-assignment/grid?formation=info&year=2&className=A
     */
    @GetMapping("/grid")
    public ResponseEntity<AssignmentGridDTO> getAssignmentGrid(
            @RequestParam(required = false) String formation,
            @RequestParam String year,
            @RequestParam String className,
            @RequestParam(required = false) String semester) {
        log.info("GET /api/teacher-assignment/grid?formation={}&year={}&className={}&semester={}",
                formation, year, className, semester);

        AssignmentGridDTO grid = assignmentService.getAssignmentGrid(formation, year, className, semester);
        return ResponseEntity.ok(grid);
    }

    /**
     * Créer une nouvelle affectation
     * POST /api/teacher-assignment/assignments
     */
    @PostMapping("/assignments")
    public ResponseEntity<Assignment> createAssignment(@RequestBody CreateAssignmentDTO dto) {
        log.info("POST /api/teacher-assignment/assignments - {}", dto);
        try {
            Assignment assignment = assignmentService.createAssignment(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(assignment);
        } catch (IllegalArgumentException e) {
            log.error("Erreur lors de la création de l'affectation", e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            log.error("Erreur lors de la création de l'affectation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Mettre à jour une affectation
     * PUT /api/teacher-assignment/assignments/{id}
     */
    @PutMapping("/assignments/{id}")
    public ResponseEntity<Assignment> updateAssignment(
            @PathVariable Integer id,
            @RequestBody CreateAssignmentDTO dto) {
        log.info("PUT /api/teacher-assignment/assignments/{} - {}", id, dto);
        try {
            Assignment assignment = assignmentService.updateAssignment(id, dto);
            return ResponseEntity.ok(assignment);
        } catch (RuntimeException e) {
            log.error("Erreur lors de la mise à jour de l'affectation", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Supprimer une affectation
     * DELETE /api/teacher-assignment/assignments/{id}
     */
    @DeleteMapping("/assignments/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Integer id) {
        log.info("DELETE /api/teacher-assignment/assignments/{}", id);
        try {
            assignmentService.deleteAssignment(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Erreur lors de la suppression de l'affectation", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Supprimer une affectation par enseignant et ressource
     * DELETE /api/teacher-assignment/assignments/teacher/{userId}/ressource/{ressourceId}
     */
    @DeleteMapping("/assignments/teacher/{userId}/ressource/{ressourceId}")
    public ResponseEntity<Void> deleteAssignmentByTeacherAndRessource(
            @PathVariable Integer userId,
            @PathVariable Integer ressourceId) {
        log.info("DELETE /api/teacher-assignment/assignments/teacher/{}/ressource/{}", userId, ressourceId);
        try {
            assignmentService.deleteAssignmentByTeacherAndRessource(userId, ressourceId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Erreur lors de la suppression de l'affectation", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Valider les affectations
     * POST /api/teacher-assignment/validate?year=2&className=A
     */
    @PostMapping("/validate")
    public ResponseEntity<AssignmentValidationResponseDTO> validateAssignments(
            @RequestParam String year,
            @RequestParam String className) {
        log.info("POST /api/teacher-assignment/validate?year={}&className={}", year, className);

        AssignmentValidationResponseDTO response = assignmentService.validateAssignments(year, className);
        return ResponseEntity.ok(response);
    }

    /**
     * Health check
     * GET /api/teacher-assignment/health
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Teacher Assignment API is running");
    }
}
