package sae.project.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sae.project.model.Formation;
import sae.project.model.Resource;
import sae.project.repositories.EducationManagerRepository;
import sae.project.repositories.ResourceRepository;

@Service
public class EducationManagerService {
    @Autowired
    private EducationManagerRepository emrep;
    @Autowired
    private ResourceRepository rrep;

    public List<Formation> getAll() {
        List<Formation> formationList = emrep.findAll();
        for(Formation formation: formationList){
           formation.getUsersList();
           formation.getResourceList();
        }
        return (List<Formation>) formationList;
    }
       
    public Formation getById(Integer id){
        Formation formation = emrep.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation non trouvée"));
        formation.getUsersList();
        formation.getResourceList();
        return formation;
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
    
    public Formation update(Formation f){
        return emrep.save(f);
    }
}
