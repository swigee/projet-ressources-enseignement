package sae.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sae.project.model.Formation;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormationRepository extends JpaRepository<Formation, Integer> {

    // Trouver par nom
    List<Formation> findByName(String name);

    // Trouver par année
    List<Formation> findByYear(String year);

    // Trouver par classe
    List<Formation> findByClassName(String className);

    // Trouver par année et classe
    Optional<Formation> findByYearAndClassName(String year, String className);

    // Rechercher toutes les années distinctes
    @Query("SELECT DISTINCT f.year FROM Formation f ORDER BY f.year")
    List<String> findDistinctYears();

    // Rechercher toutes les classes pour une année
    @Query("SELECT DISTINCT f.className FROM Formation f WHERE f.year = :year ORDER BY f.className")
    List<String> findClassesByYear(@Param("year") String year);
}