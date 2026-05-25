package sae.project.services;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sae.project.dtos.education.EducationDTO;
import sae.project.model.Formation;
import sae.project.model.Resource;
import sae.project.model.Semester;
import sae.project.model.User;
import sae.project.repositories.EducationManagerRepository;
import sae.project.repositories.ResourceRepository;
import sae.project.repositories.SemesterRepository;

@Service
public class EducationManagerService {
    @Autowired
    private EducationManagerRepository emrep;
    @Autowired
    private ResourceRepository rrep;
    @Autowired
    private SemesterRepository semrep;

    public List<Formation> getAll() {
        return emrep.findAllWithResources();
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
        if (f.getSemesters() != null) {
            f.getSemesters().forEach(semester -> semester.setFormation(f));
        }
        emrep.save(f);
    }

    public List<Resource> getRessourcesList() {
       return rrep.findAll();
    }
    
   @Transactional
    public Formation update(Integer id, EducationDTO request) {
        Formation existing = emrep.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Formation introuvable : " + id));

        Formation formationDto = request.getFormation();
        if (formationDto != null) {
            existing.setName(formationDto.getName());
            existing.setDescription(formationDto.getDescription());
            if (formationDto.getYear() != null) {
                existing.setYear(formationDto.getYear());
            }
            if (formationDto.getClassName() != null) {
                existing.setClassName(formationDto.getClassName());
            }
            if (formationDto.getParcours() != null) {
                existing.setParcours(formationDto.getParcours());
            }
        }

        if (request.getSemesters() != null) {
            if (existing.getSemesters() == null) {
                existing.setSemesters(new ArrayList<>());
            }
            existing.getSemesters().clear();

            for (Semester incomingSemester : request.getSemesters()) {
                Semester semester = new Semester();
                if (existing.getYear() != null) {
                    try {
                        semester.setYear(Integer.valueOf(existing.getYear()));
                    } catch (NumberFormatException ignored) {
                        semester.setYear(null);
                    }
                }
                semester.setSemester_number(incomingSemester.getSemester_number());
                semester.setParcours(incomingSemester.getParcours());
                semester.setFormation(existing);

                List<Resource> resources = new ArrayList<>();
                if (incomingSemester.getResourceList() != null) {
                    for (Resource incomingResource : incomingSemester.getResourceList()) {
                        if (incomingResource.getId() != null) {
                            rrep.findById(incomingResource.getId()).ifPresent(resource -> {
                                if (incomingSemester.getSemester_number() != null) {
                                    resource.setSemester(incomingSemester.getSemester_number());
                                }
                                resources.add(resource);
                                rrep.save(resource);
                            });
                        }
                    }
                }
                semester.setResourceList(resources);
                existing.getSemesters().add(semester);
            }
        }

        if (request.getResources() != null) {
            List<Resource> resources = new ArrayList<>();
            for (Resource incomingResource : request.getResources()) {
                if (incomingResource.getId() != null) {
                    rrep.findById(incomingResource.getId())
                            .ifPresent(resources::add);
                }
            }
            existing.setResourceList(resources);
        }

        return emrep.save(existing);
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

//    public List<String> getDistinctClasses(String year, String formation) {
//        String yearParam = (year == null || year.isBlank()) ? null : year;
//        String formationParam = (formation == null || formation.isBlank()) ? null : formation;
//        return emrep.findDistinctClassNames(yearParam, formationParam);
//    }
}
