package sae.project.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sae.project.model.Formation;
import sae.project.model.Resource;
import sae.project.model.User;
import sae.project.repositories.EducationManagerRepository;
import sae.project.repositories.ResourceRepository;

@Service
public class EducationManagerService {
    @Autowired
    private EducationManagerRepository emrep;
    @Autowired
    private ResourceRepository rrep;

    public List<Formation> getAll() {
        return (List<Formation>) emrep.findAll();
    }
       
    public Optional<Formation> getById(Integer id){
        return emrep.findById(id);
    }
    
    public void delete(Integer id) {
        emrep.deleteById(id);
    }

    public void create(Formation f) {
        emrep.save(f);
    }

    public List<Resource> getRessourcesList() {
        return rrep.findAll();
    }
    
    public List<Resource> getRessourcesByFormation(Formation f) {
        return f.getResourceList();
    }
    
    public Formation update(Formation f){
        return emrep.save(f);
    }

    public Formation duplicate(Integer id, String newName) {
        Formation source = emrep.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation non trouvée : " + id));

        Formation copy = Formation.builder()
                .name(newName)
                .year(source.getYear())
                .className(source.getClassName())
                .description(source.getDescription())
                .resourceList(new java.util.ArrayList<>(source.getResourceList()))
                .build();

        return emrep.save(copy);
    }

    public List<User> getUsersByFormation(Integer id){
        Formation f = emrep.getById(id);
        return f.getUsersList();
    }
}
