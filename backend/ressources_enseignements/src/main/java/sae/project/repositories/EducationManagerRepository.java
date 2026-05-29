
package sae.project.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sae.project.model.Formation;

import java.util.List;

public interface EducationManagerRepository extends JpaRepository<Formation, Integer> {
    @Query("SELECT DISTINCT f FROM Formation f LEFT JOIN FETCH f.resources")
    List<Formation> findAllWithResources();
    @Query("SELECT DISTINCT f.className FROM Formation f " +
           "WHERE f.className IS NOT NULL " +
           "AND (:year IS NULL OR f.year = :year) " +
           "AND (:program IS NULL OR f.name = :program) " +
           "ORDER BY f.className")
    List<String> findDistinctClassNames(
            @Param("year") String year,
            @Param("program") String program);
}
