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

    // Trouver les affectations par formation (via ressource)
    @Query("SELECT a FROM Assignment a " +
            "JOIN a.resource r " +
            "JOIN r.formationList f " +
            "WHERE f.year = :year AND f.className = :className")
    List<Assignment> findByFormation(
            @Param("year") String year,
            @Param("className") String className);

    // Enseignants avec leurs heures (exclure les étudiants)
    @Query("SELECT u.id, u.firstName, u.lastName, COALESCE(SUM(a.assignedTimes), 0) " +
            "FROM User u LEFT JOIN Assignment a ON u.id = a.user.id " +
            "WHERE u.id NOT IN (SELECT ur.id FROM User ur JOIN ur.roleList r WHERE r.id = 3) " +
            "GROUP BY u.id, u.firstName, u.lastName")
    List<Object[]> getTeachersWithHours();
}
