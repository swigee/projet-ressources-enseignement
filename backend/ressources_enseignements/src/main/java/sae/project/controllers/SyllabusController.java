package sae.project.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sae.project.dtos.ressources.SyllabusResourceDTO;
import sae.project.services.SyllabusService;

import java.util.List;

@RestController
@RequestMapping("/api/syllabus")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Syllabus", description = "API for managing syllabus resources")
public class SyllabusController {

    @Autowired
    private SyllabusService syllabusService;

    @GetMapping
    @Operation(summary = "Get all syllabus resources")
    public ResponseEntity<List<SyllabusResourceDTO>> getAllSyllabusResources() {
        return ResponseEntity.ok(syllabusService.getAllSyllabusResources());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a syllabus resource by ID")
    public ResponseEntity<SyllabusResourceDTO> getSyllabusResourceById(@PathVariable Integer id) {
        SyllabusResourceDTO dto = syllabusService.getSyllabusResourceById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/personal-fields")
    @Operation(summary = "Update the personal fields of a syllabus resource")
    public ResponseEntity<Void> updatePersonalFields(@PathVariable Integer id, @RequestBody SyllabusResourceDTO dto) {
        try {
            syllabusService.updatePersonalFields(id, dto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/upload-csv")
    @Operation(summary = "Import syllabus data from a CSV file")
    public ResponseEntity<String> uploadSyllabusCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The file is empty.");
        }

        try {
            syllabusService.importSyllabusCsv(file);
            return ResponseEntity.ok("CSV file imported successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while importing the file: " + e.getMessage());
        }
    }
}
