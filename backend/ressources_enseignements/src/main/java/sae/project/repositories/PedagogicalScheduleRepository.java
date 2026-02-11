package sae.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sae.project.model.Resource;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedagogicalScheduleRepository extends JpaRepository<Resource, Integer> {

        // Rechercher par année et classe (via formation)
        @Query("SELECT DISTINCT r FROM Resource r JOIN r.formationList f " +
                        "WHERE f.year = :year AND f.className = :className")
        List<Resource> findByYearAndClass(
                        @Param("year") String year,
                        @Param("className") String className);

        // Rechercher par année, classe et semestre (via formation)
        @Query("SELECT DISTINCT r FROM Resource r JOIN r.formationList f " +
                        "WHERE f.year = :year AND f.className = :className AND r.semester = :semester")
        List<Resource> findByYearAndClassAndSemester(
                        @Param("year") String year,
                        @Param("className") String className,
                        @Param("semester") Integer semester);

        // Rechercher avec titre contenant
        @Query("SELECT r FROM Resource r WHERE LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
        List<Resource> searchByTitleContaining(@Param("keyword") String keyword);
}
