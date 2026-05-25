package sae.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sae.project.model.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    Permission findByName(String name);
}

