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
    @Query("SELECT a FROM Assignment a WHERE a.user.id = :userId")
    List<Assignment> findByUserId(@Param("userId") Integer userId);

    // Trouver les affectations par ressource
    @Query("SELECT a FROM Assignment a WHERE a.resource.id = :resourceId")
    List<Assignment> findByResourceId(@Param("resourceId") Integer resourceId);

    // Trouver par enseignant et ressource
    @Query("SELECT a FROM Assignment a WHERE a.user.id = :userId AND a.resource.id = :resourceId")
    Optional<Assignment> findByUserIdAndResourceId(
            @Param("userId") Integer userId,
            @Param("resourceId") Integer resourceId);

    // Trouver par enseignant, ressource ET type de cours
    @Query("SELECT a FROM Assignment a WHERE a.user.id = :userId " +
            "AND a.resource.id = :resourceId " +
            "AND a.lessonType = :lessonType")
    Optional<Assignment> findByUserIdAndResourceIdAndLessonType(
            @Param("userId") Integer userId,
            @Param("resourceId") Integer resourceId,
            @Param("lessonType") String lessonType);

    // Trouver par type de cours
    List<Assignment> findByLessonType(String lessonType);

    // Trouver les affectations par formation (via ressource)
    @Query("SELECT a FROM Assignment a " +
            "JOIN a.resource r " +
            "JOIN r.formationList f " +
            "WHERE f.year = :year AND f.className = :className")
    List<Assignment> findByFormation(
            @Param("year") String year,
            @Param("className") String className);

    // Calculer le total des heures pour un enseignant
    @Query("SELECT COALESCE(SUM(a.assignedTimes), 0) FROM Assignment a WHERE a.user.id = :userId")
    Integer getTotalHoursByUserId(@Param("userId") Integer userId);

    // Trouver les ressources non affectées pour une formation
    @Query("SELECT r FROM Resource r " +
            "JOIN r.formationList f " +
            "WHERE f.year = :year AND f.className = :className " +
            "AND r.id NOT IN (SELECT a.resource.id FROM Assignment a)")
    List<Object> findUnassignedResources(
            @Param("year") String year,
            @Param("className") String className);

    // Statistiques par type de cours
    @Query("SELECT a.lessonType, COUNT(a), SUM(a.assignedTimes) " +
            "FROM Assignment a GROUP BY a.lessonType")
    List<Object[]> getStatisticsByLessonType();

    // Enseignants avec leurs heures
    @Query("SELECT u.id, u.firstName, u.lastName, COALESCE(SUM(a.assignedTimes), 0) " +
            "FROM User u LEFT JOIN Assignment a ON u.id = a.user.id " +
            "GROUP BY u.id, u.firstName, u.lastName")
    List<Object[]> getTeachersWithHours();
}
