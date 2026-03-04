package sae.project.controllers;

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
public class RessourcesController {

    @Autowired
    private RessourcesService ressourcesTableService;

    @GetMapping("/data")
    public ResponseEntity<RessourcesResponseDTO> getData(
            @RequestParam String year,
            @RequestParam String className,
            @RequestParam Integer semester) {
        log.info("GET /api/ressources-table/data?year={}&className={}&semester={}", year, className, semester);

        RessourcesResponseDTO response = ressourcesTableService.getRessourcesTableData(year, className, semester);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<TeacherBadgeDTO>> getAvailableTeachers() {
        log.info("GET /api/ressources-table/teachers");

        List<TeacherBadgeDTO> teachers = ressourcesTableService.getAvailableTeachers();
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/conflicts")
    public ResponseEntity<List<ScheduleConflictDTO>> detectConflicts(
            @RequestParam Integer teacherId) {
        log.info("GET /api/ressources-table/conflicts?teacherId={}", teacherId);

        List<ScheduleConflictDTO> conflicts = ressourcesTableService.detectConflicts(teacherId);
        return ResponseEntity.ok(conflicts);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RessourceRowDTO>> searchRessources(
            @RequestParam String keyword) {
        log.info("GET /api/ressources-table/search?keyword={}", keyword);

        List<RessourceRowDTO> ressources = ressourcesTableService.searchRessources(keyword);
        return ResponseEntity.ok(ressources);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Ressources Table API is running");
    }
}
