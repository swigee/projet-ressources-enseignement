package sae.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sae.project.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRights(String rights);
}

