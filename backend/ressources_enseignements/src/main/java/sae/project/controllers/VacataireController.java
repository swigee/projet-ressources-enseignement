package sae.project.controllers;

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
public class VacataireController {

    @Autowired
    private VacataireService vacataireService;

    // ───────────────────────────────────────────────
    // CRUD endpoints
    // ───────────────────────────────────────────────

    /** GET /api/vacataires — list all contractor profiles */
    @GetMapping
    public ResponseEntity<List<VacataireDTO>> getAll() {
        return ResponseEntity.ok(vacataireService.getAll());
    }

    /** GET /api/vacataires/{id} — get a single contractor profile */
    @GetMapping("/{id}")
    public ResponseEntity<VacataireDTO> getById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(vacataireService.getById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** POST /api/vacataires — create a new contractor profile */
    @PostMapping
    public ResponseEntity<VacataireDTO> create(@RequestBody VacataireDTO dto) {
        VacataireDTO created = vacataireService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /** PUT /api/vacataires/{id} — update an existing contractor profile */
    @PutMapping("/{id}")
    public ResponseEntity<VacataireDTO> update(@PathVariable Integer id, @RequestBody VacataireDTO dto) {
        try {
            return ResponseEntity.ok(vacataireService.update(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** DELETE /api/vacataires/{id} — delete a contractor profile */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            vacataireService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** GET /api/vacataires/search?keyword=dupont — search by name */
    @GetMapping("/search")
    public ResponseEntity<List<VacataireDTO>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(vacataireService.search(keyword));
    }

    // ───────────────────────────────────────────────
    // Filtering endpoints
    // ───────────────────────────────────────────────

    /**
     * GET /api/vacataires/active
     * Returns contractor profiles that already have a linked user account.
     */
    @GetMapping("/active")
    public ResponseEntity<List<VacataireDTO>> getActive() {
        log.info("GET /api/vacataires/active");
        return ResponseEntity.ok(vacataireService.getActive());
    }

    /**
     * GET /api/vacataires/pending
     * Returns contractor profiles that are still in the recruitment pipeline
     * (no user account linked yet).
     */
    @GetMapping("/pending")
    public ResponseEntity<List<VacataireDTO>> getPending() {
        log.info("GET /api/vacataires/pending");
        return ResponseEntity.ok(vacataireService.getPending());
    }

    // ───────────────────────────────────────────────
    // Account conversion endpoint
    // ───────────────────────────────────────────────

    /**
     * POST /api/vacataires/{id}/convert
     * <p>
     * Converts a contractor recruitment profile into an active user account.
     * Expected request body:
     * <pre>
     * {
     *   "username": "P2600099",
     *   "password": "ChangeMe123"
     * }
     * </pre>
     * Returns the updated contractor DTO with {@code accountActive = true}
     * and the new {@code userId}.
     */
    @PostMapping("/{id}/convert")
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
