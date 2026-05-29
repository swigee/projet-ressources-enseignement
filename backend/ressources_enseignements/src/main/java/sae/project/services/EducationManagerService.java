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
    private EducationManagerRepository formationRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    public List<Formation> getAll() {
        return formationRepository.findAllWithResources();
    }

    public Formation getById(Integer id) {
        Formation program = formationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation not found"));
        program.getUsers();
        program.getResources();
        return program;
    }

    public void delete(Integer id) {
        formationRepository.deleteById(id);
    }

    public void create(Formation f) {
        if (f.getSemesters() != null) {
            f.getSemesters().forEach(semester -> semester.setProgram(f));
        }
        formationRepository.save(f);
    }

    public List<Resource> getResourcesList() {
        return resourceRepository.findAll();
    }

    @Transactional
    public Formation update(Integer id, EducationDTO request) {
        Formation existing = formationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Formation not found: " + id));

        Formation programDto = request.getProgram();
        if (programDto != null) {
            existing.setName(programDto.getName());
            existing.setDescription(programDto.getDescription());
            if (programDto.getYear() != null) {
                existing.setYear(programDto.getYear());
            }
            if (programDto.getClassName() != null) {
                existing.setClassName(programDto.getClassName());
            }
            if (programDto.getPathway() != null) {
                existing.setPathway(programDto.getPathway());
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
                semester.setPathway(incomingSemester.getPathway());
                semester.setProgram(existing);

                List<Resource> resources = new ArrayList<>();
                if (incomingSemester.getResources() != null) {
                    for (Resource incomingResource : incomingSemester.getResources()) {
                        if (incomingResource.getId() != null) {
                            resourceRepository.findById(incomingResource.getId()).ifPresent(resource -> {
                                if (incomingSemester.getSemester_number() != null) {
                                    resource.setSemester(incomingSemester.getSemester_number());
                                }
                                resources.add(resource);
                                resourceRepository.save(resource);
                            });
                        }
                    }
                }
                semester.setResources(resources);
                existing.getSemesters().add(semester);
            }
        }

        if (request.getResources() != null) {
            List<Resource> resources = new ArrayList<>();
            for (Resource incomingResource : request.getResources()) {
                if (incomingResource.getId() != null) {
                    resourceRepository.findById(incomingResource.getId())
                            .ifPresent(resources::add);
                }
            }
            existing.setResources(resources);
        }

        return formationRepository.save(existing);
    }

    public Formation update(Formation f) {
        return formationRepository.save(f);
    }

    public Formation duplicate(Integer id, String newName) {
        Formation source = formationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation not found: " + id));

        Formation copy = Formation.builder()
                .name(newName)
                .year(source.getYear())
                .className(source.getClassName())
                .description(source.getDescription())
                .resources(new java.util.ArrayList<>(source.getResources()))
                .build();

        return formationRepository.save(copy);
    }

    public List<User> getUsersByFormation(Integer id) {
        Formation f = formationRepository.getById(id);
        return f.getUsers();
    }

    public List<String> getDistinctClasses(String year, String program) {
        String yearParam    = (year    == null || year.isBlank())    ? null : year;
        String programParam = (program == null || program.isBlank()) ? null : program;
        return formationRepository.findDistinctClassNames(yearParam, programParam);
    }
}
