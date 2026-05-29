package sae.project.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sae.project.dtos.ressources.RessourceRowDTO;
import sae.project.dtos.ressources.RessourcesResponseDTO;
import sae.project.dtos.ressources.ScheduleConflictDTO;
import sae.project.dtos.ressources.TeacherBadgeDTO;
import sae.project.services.RessourcesService;

import java.util.List;

@RestController
@RequestMapping("/api/ressources-table")
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
@Tag(name = "Resources Table", description = "API for the educational resources table view")
public class RessourcesController {

    @Autowired
    private RessourcesService ressourcesTableService;

    @GetMapping("/data")
    @Operation(summary = "Get resources table data, optionally filtered by year, class, semester and formation")
    public ResponseEntity<RessourcesResponseDTO> getData(
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String className,
            @RequestParam(required = false) Integer semester,
            @RequestParam(required = false) String formation) {
        log.info("GET /api/ressources-table/data?year={}&className={}&semester={}&formation={}", year, className, semester, formation);

        RessourcesResponseDTO response = ressourcesTableService.getRessourcesTableData(year, className, semester, formation);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/teachers")
    @Operation(summary = "Get all available teachers")
    public ResponseEntity<List<TeacherBadgeDTO>> getAvailableTeachers() {
        log.info("GET /api/ressources-table/teachers");

        List<TeacherBadgeDTO> teachers = ressourcesTableService.getAvailableTeachers();
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/conflicts")
    @Operation(summary = "Detect scheduling conflicts for a given teacher")
    public ResponseEntity<List<ScheduleConflictDTO>> detectConflicts(
            @RequestParam Integer teacherId) {
        log.info("GET /api/ressources-table/conflicts?teacherId={}", teacherId);

        List<ScheduleConflictDTO> conflicts = ressourcesTableService.detectConflicts(teacherId);
        return ResponseEntity.ok(conflicts);
    }

    @GetMapping("/search")
    @Operation(summary = "Search resources by keyword")
    public ResponseEntity<List<RessourceRowDTO>> searchRessources(
            @RequestParam String keyword) {
        log.info("GET /api/ressources-table/search?keyword={}", keyword);

        List<RessourceRowDTO> ressources = ressourcesTableService.searchRessources(keyword);
        return ResponseEntity.ok(ressources);
    }

    @GetMapping("/health")
    @Operation(summary = "Check API health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Ressources Table API is running");
    }
}
