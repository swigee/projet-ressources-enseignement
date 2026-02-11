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

        // Rechercher par titre
        Optional<Resource> findByTitle(String title);

        // Rechercher par catégorie
        List<Resource> findByCategory(String category);

        // Rechercher par formation
        @Query("SELECT r FROM Resource r JOIN r.formationList f WHERE f.id = :formationId")
        List<Resource> findByFormationId(@Param("formationId") Integer formationId);

        // Rechercher par syllabus
        @Query("SELECT r FROM Resource r JOIN r.syllabusList s WHERE s.id = :syllabusId")
        List<Resource> findBySyllabusId(@Param("syllabusId") Integer syllabusId);

        // Rechercher avec filtre sur les heures
        @Query("SELECT r FROM Resource r WHERE " +
                        "(r.tdStateHours > 0 OR r.tpStateHours > 0 OR r.cmStateHours > 0)")
        List<Resource> findWithHours();

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

        // Compter les ressources par catégorie
        @Query("SELECT r.category, COUNT(r) FROM Resource r " +
                        "GROUP BY r.category")
        List<Object[]> countByCategory();

        // Rechercher avec titre contenant
        @Query("SELECT r FROM Resource r WHERE LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
        List<Resource> searchByTitleContaining(@Param("keyword") String keyword);

        // Trouver toutes les ressources ordonnées par titre
        List<Resource> findAllByOrderByTitleAsc();

        // Trouver les ressources modifiées récemment (nécessite un champ lastModified)
        // @Query("SELECT r FROM Resource r WHERE r.lastModified >= :date")
        // List<Resource> findRecentlyModified(@Param("date") LocalDateTime date);
}
