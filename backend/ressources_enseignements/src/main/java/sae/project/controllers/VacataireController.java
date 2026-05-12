package sae.project.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sae.project.dtos.vacataire.VacataireDTO;
import sae.project.services.VacataireService;

import java.util.List;

@RestController
@RequestMapping("/api/vacataires")
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class VacataireController {

    @Autowired
    private VacataireService vacataireService;

    @GetMapping
    public ResponseEntity<List<VacataireDTO>> getAll() {
        return ResponseEntity.ok(vacataireService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VacataireDTO> getById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(vacataireService.getById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<VacataireDTO> create(@RequestBody VacataireDTO dto) {
        VacataireDTO created = vacataireService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VacataireDTO> update(@PathVariable Integer id, @RequestBody VacataireDTO dto) {
        try {
            return ResponseEntity.ok(vacataireService.update(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            vacataireService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<VacataireDTO>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(vacataireService.search(keyword));
    }
}
