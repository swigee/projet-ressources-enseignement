package sae.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sae.project.model.Ressources;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedagogicalScheduleRepository extends JpaRepository<Ressources, Integer> {

    // Rechercher par titre
    Optional<Ressources> findByTitle(String title);

    // Rechercher par catégorie
    List<Ressources> findByCategory(String category);

    // Rechercher les ressources en surbrillance
    List<Ressources> findByIsHighlighted(Boolean isHighlighted);

    // Rechercher par formation
    @Query("SELECT r FROM Ressources r JOIN r.formationList f WHERE f.idformation  = :formationId")
    List<Ressources> findByFormationId(@Param("formationId") Integer formationId);

    // Rechercher par syllabus
    @Query("SELECT r FROM Ressources r JOIN r.syllabusList s WHERE s.idsyllabus = :syllabusId")
    List<Ressources> findBySyllabusId(@Param("syllabusId") Integer syllabusId);

    // Rechercher avec filtre sur les heures
    @Query("SELECT r FROM Ressources r WHERE " +
            "(r.heureTdEtat > 0 OR r.heureTpEtat > 0 OR r.heureCmEtat > 0)")
    List<Ressources> findWithHours();

    // Rechercher par année et classe (via formation)
    @Query("SELECT DISTINCT r FROM Ressources r JOIN r.formationList f " +
            "WHERE f.year = :year AND f.className = :className")
    List<Ressources> findByYearAndClass(
            @Param("year") String year,
            @Param("className") String className
    );

    // Compter les ressources par catégorie
    @Query("SELECT r.category, COUNT(r) FROM Ressources r " +
            "GROUP BY r.category")
    List<Object[]> countByCategory();

    // Rechercher avec titre contenant
    @Query("SELECT r FROM Ressources r WHERE LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Ressources> searchByTitleContaining(@Param("keyword") String keyword);

    // Trouver toutes les ressources ordonnées par titre
    List<Ressources> findAllByOrderByTitleAsc();

    // Trouver les ressources modifiées récemment (nécessite un champ lastModified)
    // @Query("SELECT r FROM Ressources r WHERE r.lastModified >= :date")
    // List<Ressources> findRecentlyModified(@Param("date") LocalDateTime date);
}