package sae.project.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import sae.project.model.Formation;
import sae.project.model.Resource;
import sae.project.model.User;
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
    public Optional<Formation> get(@PathVariable Integer id) {
        return emrep.getById(id);
    }

    @GetMapping("/lessons/list")
    public Iterable<Resource> getRessources() {
        return emrep.getRessourcesList();
    }
    
    @GetMapping("/lessons/{id}")
    public Iterable<Resource> getRessourcesByFormation(@PathVariable Integer id) {
        Formation formation = emrep.getById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Formation non trouvée"
        ));
        return emrep.getRessourcesByFormation(formation);
    }
    
    @PutMapping("/{id}")
    public Formation put(@RequestBody Formation etu) {
        return emrep.update(etu);
    }

    @PostMapping
    public void post(@RequestBody Formation f) {
        emrep.create(f);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        emrep.delete(id);
    }
    
    @GetMapping("/{id}/users")
    public Iterable<User> getUsers(@PathVariable Integer id) {
        return emrep.getUsersByFormation(id);
    }

    @GetMapping("/classes")
    public List<String> getDistinctClasses(
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String formation) {
        return emrep.getDistinctClasses(year, formation);
    }
}
