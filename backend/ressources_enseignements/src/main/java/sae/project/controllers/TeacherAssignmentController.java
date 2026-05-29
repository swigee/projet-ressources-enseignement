package sae.project.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Teacher Assignment", description = "API for managing teacher assignments to resources")
public class TeacherAssignmentController {

    @Autowired
    private TeacherAssignmentService assignmentService;

    @GetMapping("/teachers")
    @Operation(summary = "Get all teachers")
    public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
        log.info("GET /api/teacher-assignment/teachers");
        List<TeacherDTO> teachers = assignmentService.getAllTeachers();
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/teachers/search")
    @Operation(summary = "Search teachers by keyword")
    public ResponseEntity<List<TeacherDTO>> searchTeachers(@RequestParam String keyword) {
        log.info("GET /api/teacher-assignment/teachers/search?keyword={}", keyword);
        List<TeacherDTO> teachers = assignmentService.searchTeachers(keyword);
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/grid")
    @Operation(summary = "Get the full assignment grid, optionally filtered by formation, year, class and semester")
    public ResponseEntity<AssignmentGridDTO> getAssignmentGrid(
            @RequestParam(required = false) String formation,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String className,
            @RequestParam(required = false) String semester) {
        log.info("GET /api/teacher-assignment/grid?formation={}&year={}&className={}&semester={}",
                formation, year, className, semester);

        AssignmentGridDTO grid = assignmentService.getAssignmentGrid(formation, year, className, semester);
        return ResponseEntity.ok(grid);
    }

    @PostMapping("/assignments")
    @Operation(summary = "Create a new teacher assignment")
    public ResponseEntity<Assignment> createAssignment(@RequestBody CreateAssignmentDTO dto) {
        log.info("POST /api/teacher-assignment/assignments - {}", dto);
        try {
            Assignment assignment = assignmentService.createAssignment(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(assignment);
        } catch (IllegalArgumentException e) {
            log.error("Invalid data for assignment creation", e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            log.error("Error while creating assignment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/assignments/{id}")
    @Operation(summary = "Update an existing assignment by ID")
    public ResponseEntity<Assignment> updateAssignment(
            @PathVariable Integer id,
            @RequestBody CreateAssignmentDTO dto) {
        log.info("PUT /api/teacher-assignment/assignments/{} - {}", id, dto);
        try {
            Assignment assignment = assignmentService.updateAssignment(id, dto);
            return ResponseEntity.ok(assignment);
        } catch (RuntimeException e) {
            log.error("Error while updating assignment", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/assignments/{id}")
    @Operation(summary = "Delete an assignment by ID")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Integer id) {
        log.info("DELETE /api/teacher-assignment/assignments/{}", id);
        try {
            assignmentService.deleteAssignment(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error while deleting assignment", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/assignments/teacher/{userId}/ressource/{ressourceId}")
    @Operation(summary = "Delete an assignment by teacher ID and resource ID")
    public ResponseEntity<Void> deleteAssignmentByTeacherAndRessource(
            @PathVariable Integer userId,
            @PathVariable Integer ressourceId) {
        log.info("DELETE /api/teacher-assignment/assignments/teacher/{}/ressource/{}", userId, ressourceId);
        try {
            assignmentService.deleteAssignmentByTeacherAndRessource(userId, ressourceId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error while deleting assignment", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/validate")
    @Operation(summary = "Validate assignments for a given year and class")
    public ResponseEntity<AssignmentValidationResponseDTO> validateAssignments(
            @RequestParam String year,
            @RequestParam String className) {
        log.info("POST /api/teacher-assignment/validate?year={}&className={}", year, className);

        AssignmentValidationResponseDTO response = assignmentService.validateAssignments(year, className);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    @Operation(summary = "Check API health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Teacher Assignment API is running");
    }
}
