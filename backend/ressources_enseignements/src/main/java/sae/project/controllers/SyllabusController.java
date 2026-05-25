package sae.project.controllers;

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
@CrossOrigin(origins = "http://localhost:4200") // Adjust according to frontend URL
public class SyllabusController {

    @Autowired
    private SyllabusService syllabusService;

    @GetMapping
    public ResponseEntity<List<SyllabusResourceDTO>> getAllSyllabusResources() {
        return ResponseEntity.ok(syllabusService.getAllSyllabusResources());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SyllabusResourceDTO> getSyllabusResourceById(@PathVariable Integer id) {
        SyllabusResourceDTO dto = syllabusService.getSyllabusResourceById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/personal-fields")
    public ResponseEntity<Void> updatePersonalFields(@PathVariable Integer id, @RequestBody SyllabusResourceDTO dto) {
        try {
            syllabusService.updatePersonalFields(id, dto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/upload-csv")
    public ResponseEntity<String> uploadSyllabusCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le fichier est vide.");
        }

        try {
            syllabusService.importSyllabusCsv(file);
            return ResponseEntity.ok("Fichier CSV importé avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'importation du fichier: " + e.getMessage());
        }
    }
}
