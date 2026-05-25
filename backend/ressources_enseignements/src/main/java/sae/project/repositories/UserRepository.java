package sae.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sae.project.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsernameIgnoreCase(String username);
}
