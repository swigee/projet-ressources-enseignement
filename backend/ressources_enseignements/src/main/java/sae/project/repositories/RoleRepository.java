package sae.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sae.project.model.Role;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);

    @Query("SELECT COUNT(u) FROM User u JOIN u.roleList r WHERE r.id = :roleId")
    long countUsersByRoleId(@Param("roleId") int roleId);

    @Query("SELECT r.id, COUNT(u) FROM Role r LEFT JOIN r.userList u GROUP BY r.id")
    List<Object[]> countUsersForAllRoles();

    @Query("SELECT r.id, COUNT(p) FROM Role r LEFT JOIN r.permissions p GROUP BY r.id")
    List<Object[]> countPermissionsForAllRoles();

    @Query("SELECT COUNT(p) FROM Role r LEFT JOIN r.permissions p WHERE r.id = :roleId")
    long countPermissionsByRoleId(@Param("roleId") int roleId);
}
