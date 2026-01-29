package sae.project.services;

import java.util.List;
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
        return (List<Formation>) emrep.findAll();
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
}
