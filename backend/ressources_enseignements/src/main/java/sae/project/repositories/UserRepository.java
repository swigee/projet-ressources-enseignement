package sae.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sae.project.model.Users;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByUsername(String username);
}