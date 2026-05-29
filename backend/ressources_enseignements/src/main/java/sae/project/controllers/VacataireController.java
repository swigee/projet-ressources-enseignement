package sae.project.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sae.project.dtos.vacataire.VacataireDTO;
import sae.project.services.VacataireService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vacataires")
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
@Tag(name = "Vacataires", description = "API for managing adjunct/contractor teacher profiles")
public class VacataireController {

    @Autowired
    private VacataireService vacataireService;

    // ───────────────────────────────────────────────
    // CRUD endpoints
    // ───────────────────────────────────────────────

    @GetMapping
    @Operation(summary = "Get all contractor profiles")
    public ResponseEntity<List<VacataireDTO>> getAll() {
        return ResponseEntity.ok(vacataireService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a contractor profile by ID")
    public ResponseEntity<VacataireDTO> getById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(vacataireService.getById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Create a new contractor profile")
    public ResponseEntity<VacataireDTO> create(@RequestBody VacataireDTO dto) {
        VacataireDTO created = vacataireService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing contractor profile")
    public ResponseEntity<VacataireDTO> update(@PathVariable Integer id, @RequestBody VacataireDTO dto) {
        try {
            return ResponseEntity.ok(vacataireService.update(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a contractor profile by ID")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            vacataireService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search contractor profiles by name keyword")
    public ResponseEntity<List<VacataireDTO>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(vacataireService.search(keyword));
    }

    // ───────────────────────────────────────────────
    // Filtering endpoints
    // ───────────────────────────────────────────────

    @GetMapping("/active")
    @Operation(summary = "Get contractor profiles that have a linked user account")
    public ResponseEntity<List<VacataireDTO>> getActive() {
        log.info("GET /api/vacataires/active");
        return ResponseEntity.ok(vacataireService.getActive());
    }

    @GetMapping("/pending")
    @Operation(summary = "Get contractor profiles still in the recruitment pipeline (no user account yet)")
    public ResponseEntity<List<VacataireDTO>> getPending() {
        log.info("GET /api/vacataires/pending");
        return ResponseEntity.ok(vacataireService.getPending());
    }

    // ───────────────────────────────────────────────
    // Account conversion endpoint
    // ───────────────────────────────────────────────

    @PostMapping("/{id}/convert")
    @Operation(summary = "Convert a contractor profile into an active user account")
    public ResponseEntity<?> convertToUser(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {

        log.info("POST /api/vacataires/{}/convert", id);

        String username = body.get("username");
        String password = body.get("password");

        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "username is required"));
        }
        if (password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "password is required"));
        }

        try {
            VacataireDTO result = vacataireService.convertToUser(id, username, password);
            return ResponseEntity.ok(result);
        } catch (IllegalStateException e) {
            // Already linked to a user account
            log.warn("Conflict on convertToUser: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        } catch (IllegalArgumentException e) {
            // Username already taken
            log.warn("Bad request on convertToUser: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (RuntimeException e) {
            log.error("Error on convertToUser for contractor {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }
}
