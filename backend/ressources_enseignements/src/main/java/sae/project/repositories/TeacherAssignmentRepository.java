package sae.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sae.project.model.Assignment;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherAssignmentRepository extends JpaRepository<Assignment, Integer> {

    @Query("SELECT a FROM Assignment a WHERE a.user.id = :userId")
    List<Assignment> findByUserId(@Param("userId") Integer userId);

    @Query("SELECT a FROM Assignment a WHERE a.resource.id = :resourceId")
    List<Assignment> findByResourceId(@Param("resourceId") Integer resourceId);

    @Query("SELECT a FROM Assignment a WHERE a.user.id = :userId AND a.resource.id = :resourceId")
    Optional<Assignment> findByUserIdAndResourceId(
            @Param("userId") Integer userId,
            @Param("resourceId") Integer resourceId);

    @Query("SELECT a FROM Assignment a WHERE a.user.id = :userId " +
            "AND a.resource.id = :resourceId " +
            "AND a.lessonType = :lessonType")
    Optional<Assignment> findByUserIdAndResourceIdAndLessonType(
            @Param("userId") Integer userId,
            @Param("resourceId") Integer resourceId,
            @Param("lessonType") String lessonType);

    List<Assignment> findByLessonType(String lessonType);

    @Query("SELECT a FROM Assignment a " +
            "JOIN a.resource r " +
            "JOIN r.programs f " +
            "WHERE f.year = :year AND f.className = :className")
    List<Assignment> findByFormation(
            @Param("year") String year,
            @Param("className") String className);

    @Query("SELECT DISTINCT a FROM Assignment a " +
            "JOIN a.resource r " +
            "JOIN r.programs f " +
            "WHERE (:year IS NULL OR f.year = :year) " +
            "AND (:className IS NULL OR f.className = :className) " +
            "AND (:program IS NULL OR f.name = :program)")
    List<Assignment> findByFormationFlexible(
            @Param("year") String year,
            @Param("className") String className,
            @Param("program") String program);

    @Query("SELECT COALESCE(SUM(a.assignedTimes), 0) FROM Assignment a WHERE a.user.id = :userId")
    Integer getTotalHoursByUserId(@Param("userId") Integer userId);

    @Query("SELECT r FROM Resource r " +
            "JOIN r.programs f " +
            "WHERE f.year = :year AND f.className = :className " +
            "AND r.id NOT IN (SELECT a.resource.id FROM Assignment a)")
    List<Object> findUnassignedResources(
            @Param("year") String year,
            @Param("className") String className);


    @Query("SELECT a.lessonType, COUNT(a), SUM(a.assignedTimes) " +
            "FROM Assignment a GROUP BY a.lessonType")
    List<Object[]> getStatisticsByLessonType();


    @Query("SELECT u.id, u.firstName, u.lastName, COALESCE(SUM(a.assignedTimes), 0), u.type " +
            "FROM User u LEFT JOIN Assignment a ON u.id = a.user.id " +
            "WHERE u.id NOT IN (SELECT ur.id FROM User ur JOIN ur.roles r WHERE r.id = 3) " +
            "GROUP BY u.id, u.firstName, u.lastName, u.type")
    List<Object[]> getTeachersWithHours();

    @Modifying
    @Transactional
    @Query("DELETE FROM Assignment a WHERE a.user.id = :userId")
    void deleteByUserId(@Param("userId") int userId);

}
