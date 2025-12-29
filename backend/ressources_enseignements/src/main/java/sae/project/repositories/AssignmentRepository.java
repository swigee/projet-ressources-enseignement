package sae.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sae.project.model.Assignment;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    List<Assignment> findByIduser_Iduser(Integer userId);
}
