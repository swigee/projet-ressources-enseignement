package sae.project.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sae.project.model.Formation;
import sae.project.repositories.EducationManagerRepository;


@Service
public class EducationManagerService {
    @Autowired
    private EducationManagerRepository emrep;
    
    public List<Formation> getAll(){
        return (List<Formation>) emrep.findAll();
    }
}
