package sae.project.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sae.project.dtos.education.EducationDTO;
import sae.project.model.Formation;
import sae.project.model.Resource;
import sae.project.model.User;
import sae.project.services.EducationManagerService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/education-manager")
@Tag(name = "Education Manager", description = "API for managing formations (study programs)")
public class EducationManagerController {
    @Autowired
    private EducationManagerService emrep;

    @GetMapping("/list")
    @Operation(summary = "Get all formations")
    public Iterable<Formation> list() {
        return emrep.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a formation by ID")
    public Formation get(@PathVariable Integer id) {
        return emrep.getById(id);
    }

    @GetMapping("/lessons/list")
    @Operation(summary = "Get all resources (lessons)")
    public Iterable<Resource> getRessources() {
        return emrep.getRessourcesList();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update a formation")
    public Formation patch(@PathVariable Integer id, @RequestBody EducationDTO request) {
        return emrep.update(id, request);
    }

    @PostMapping
    @Operation(summary = "Create a new formation")
    public void post(@RequestBody Formation f) {
        emrep.create(f);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a formation by ID")
    public void delete(@PathVariable Integer id) {
        emrep.delete(id);
    }

    @PostMapping("/{id}/duplicate")
    @Operation(summary = "Duplicate a formation with a new name")
    public Formation duplicate(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        String newName = body.getOrDefault("newName", "Copie");
        return emrep.duplicate(id, newName);
    }

    @GetMapping("/{id}/users")
    @Operation(summary = "Get all users enrolled in a formation")
    public Iterable<User> getUsers(@PathVariable Integer id) {
        return emrep.getUsersByFormation(id);
    }

    @GetMapping("/classes")
    @Operation(summary = "Get distinct class names, optionally filtered by year and formation")
    public List<String> getDistinctClasses(
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String formation) {
        return emrep.getDistinctClasses(year, formation);
    }
}
