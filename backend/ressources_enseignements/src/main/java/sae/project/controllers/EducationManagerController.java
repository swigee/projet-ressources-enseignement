package sae.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import sae.project.model.Formation;
import sae.project.model.Resource;
import sae.project.services.EducationManagerService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/education-manager")
public class EducationManagerController {
    @Autowired
    private EducationManagerService emrep;

    @GetMapping("/list")
    public Iterable<Formation> list() {
        return emrep.getAll();
    }

    @GetMapping("/{id}")
    public Formation get(@PathVariable String id) {
        return null;
    }

    @GetMapping("/lessons/list")
    public Iterable<Resource> getRessources() {
        return emrep.getRessourcesList();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable String id, @RequestBody Object input) {
        return null;
    }

    @PostMapping
    public void post(@RequestBody Formation f) {
        emrep.create(f);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        emrep.delete(id);
    }

}
