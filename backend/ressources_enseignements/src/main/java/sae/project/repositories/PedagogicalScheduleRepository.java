package sae.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sae.project.model.Resource;

import java.util.List;

@Repository
public interface PedagogicalScheduleRepository extends JpaRepository<Resource, Integer> {

        // Rechercher par année et classe (via program)
        @Query("SELECT DISTINCT r FROM Resource r JOIN r.programs f " +
                        "WHERE f.year = :year AND f.className = :className")
        List<Resource> findByYearAndClass(
                        @Param("year") String year,
                        @Param("className") String className);

        // Rechercher par année, classe et semestre (via program)
        @Query("SELECT DISTINCT r FROM Resource r JOIN r.programs f " +
                        "WHERE f.year = :year AND f.className = :className AND r.semester = :semester")
        List<Resource> findByYearAndClassAndSemester(
                        @Param("year") String year,
                        @Param("className") String className,
                        @Param("semester") Integer semester);

        // Rechercher par annee et classe et nom de program (sans semestre)
        @Query("SELECT DISTINCT r FROM Resource r JOIN r.programs f " +
                        "WHERE f.year = :year AND f.className = :className AND f.name = :program")
        List<Resource> findByYearAndClassAndFormation(
                        @Param("year") String year,
                        @Param("className") String className,
                        @Param("program") String program);

        // Rechercher par annee, classe, semestre et nom de program
        @Query("SELECT DISTINCT r FROM Resource r JOIN r.programs f " +
                        "WHERE f.year = :year AND f.className = :className AND r.semester = :semester AND f.name = :program")
        List<Resource> findByYearAndClassAndSemesterAndFormation(
                        @Param("year") String year,
                        @Param("className") String className,
                        @Param("semester") Integer semester,
                        @Param("program") String program);

        // Rechercher par année seulement (toutes les classes)
        @Query("SELECT DISTINCT r FROM Resource r JOIN r.programs f WHERE f.year = :year")
        List<Resource> findByYear(@Param("year") String year);

        // Rechercher par année et semestre (toutes les classes)
        @Query("SELECT DISTINCT r FROM Resource r JOIN r.programs f WHERE f.year = :year AND r.semester = :semester")
        List<Resource> findByYearAndSemester(@Param("year") String year, @Param("semester") Integer semester);

        // Rechercher par année et program (toutes les classes)
        @Query("SELECT DISTINCT r FROM Resource r JOIN r.programs f WHERE f.year = :year AND f.name = :program")
        List<Resource> findByYearAndFormation(@Param("year") String year, @Param("program") String program);

        // Rechercher par année, program et semestre (toutes les classes)
        @Query("SELECT DISTINCT r FROM Resource r JOIN r.programs f WHERE f.year = :year AND f.name = :program AND r.semester = :semester")
        List<Resource> findByYearAndFormationAndSemester(@Param("year") String year, @Param("program") String program, @Param("semester") Integer semester);

        // Requête unifiée — tous les paramètres sont optionnels (passer null pour ignorer)
        @Query("SELECT DISTINCT r FROM Resource r JOIN r.programs f " +
               "WHERE (:year IS NULL OR f.year = :year) " +
               "AND (:className IS NULL OR f.className = :className) " +
               "AND (:program IS NULL OR f.name = :program) " +
               "AND (:semester IS NULL OR r.semester = :semester)")
        List<Resource> findWithFilters(
                @Param("year") String year,
                @Param("className") String className,
                @Param("program") String program,
                @Param("semester") Integer semester);

        // Rechercher avec titre contenant
        @Query("SELECT r FROM Resource r WHERE LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
        List<Resource> searchByTitleContaining(@Param("keyword") String keyword);
}
