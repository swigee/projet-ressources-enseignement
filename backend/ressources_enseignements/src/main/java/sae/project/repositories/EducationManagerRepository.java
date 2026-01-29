
package sae.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sae.project.model.Formation;

public interface EducationManagerRepository extends JpaRepository<Formation, Integer> {
    
}
