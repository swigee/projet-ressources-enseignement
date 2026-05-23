
package sae.project.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sae.project.model.Formation;

public interface EducationManagerRepository extends JpaRepository<Formation, Integer> {
    @Query("SELECT DISTINCT f FROM Formation f LEFT JOIN FETCH f.resourceList")
    List<Formation> findAllWithResources();
}
