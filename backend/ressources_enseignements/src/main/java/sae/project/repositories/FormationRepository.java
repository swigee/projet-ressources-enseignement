package sae.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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

    /**
     * Returns distinct class names, with optional year and formation filters.
     * Passing null for either parameter disables that filter.
     */
    @Query("SELECT DISTINCT f.className FROM Formation f " +
           "WHERE (:year IS NULL OR f.year = :year) " +
           "AND (:program IS NULL OR f.name = :program) " +
           "ORDER BY f.className")
    List<String> findDistinctClassNames(@Param("year") String year,
                                        @Param("program") String program);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_formation uf WHERE uf.user_id = :userId", nativeQuery = true)
    void deleteByUserId(@Param("userId") int userId);
    
}