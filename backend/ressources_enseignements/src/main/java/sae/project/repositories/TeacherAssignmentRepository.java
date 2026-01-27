package sae.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sae.project.model.Assignment;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherAssignmentRepository extends JpaRepository<Assignment, Integer> {

    // Trouver les affectations par enseignant
    @Query("SELECT a FROM Assignment a WHERE a.iduser.iduser = :userId")
    List<Assignment> findByUserId(@Param("userId") Integer userId);

    // Trouver les affectations par ressource
    @Query("SELECT a FROM Assignment a WHERE a.idressource.idressource = :ressourceId")
    List<Assignment> findByRessourceId(@Param("ressourceId") Integer ressourceId);

    // Trouver par enseignant et ressource
    @Query("SELECT a FROM Assignment a WHERE a.iduser.iduser = :userId AND a.idressource.idressource = :ressourceId")
    Optional<Assignment> findByUserIdAndRessourceId(
            @Param("userId") Integer userId,
            @Param("ressourceId") Integer ressourceId
    );

    // Trouver par enseignant, ressource ET type de cours
    @Query("SELECT a FROM Assignment a WHERE a.iduser.iduser = :userId " +
            "AND a.idressource.idressource = :ressourceId " +
            "AND a.lessontype = :lessonType")
    Optional<Assignment> findByUserIdAndRessourceIdAndLessonType(
            @Param("userId") Integer userId,
            @Param("ressourceId") Integer ressourceId,
            @Param("lessonType") String lessonType
    );

    // Trouver par type de cours
    List<Assignment> findByLessontype(String lessontype);

    // Trouver les affectations par formation (via ressource)
    @Query("SELECT a FROM Assignment a " +
            "JOIN a.idressource r " +
            "JOIN r.formationList f " +
            "WHERE f.year = :year AND f.className = :className")
    List<Assignment> findByFormation(
            @Param("year") String year,
            @Param("className") String className
    );

    // Calculer le total des heures pour un enseignant
    @Query("SELECT COALESCE(SUM(a.assignedtimes), 0) FROM Assignment a WHERE a.iduser.iduser = :userId")
    Integer getTotalHoursByUserId(@Param("userId") Integer userId);

    // Trouver les ressources non affectées pour une formation
    @Query("SELECT r FROM Ressources r " +
            "JOIN r.formationList f " +
            "WHERE f.year = :year AND f.className = :className " +
            "AND r.idressource NOT IN (SELECT a.idressource.idressource FROM Assignment a)")
    List<Object> findUnassignedRessources(
            @Param("year") String year,
            @Param("className") String className
    );

    // Statistiques par type de cours
    @Query("SELECT a.lessontype, COUNT(a), SUM(a.assignedtimes) " +
            "FROM Assignment a GROUP BY a.lessontype")
    List<Object[]> getStatisticsByLessonType();

    // Enseignants avec leurs heures
    @Query("SELECT u.iduser, u.firstname, u.lastname, COALESCE(SUM(a.assignedtimes), 0) " +
            "FROM Users u LEFT JOIN Assignment a ON u.iduser = a.iduser.iduser " +
            "GROUP BY u.iduser, u.firstname, u.lastname")
    List<Object[]> getTeachersWithHours();
}