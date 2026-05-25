package sae.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sae.project.model.Software;

import java.util.List;

public interface SoftwareRepository extends JpaRepository<Software, Integer> {
    List<Software> findByStatus(String status);
    List<Software> findByResourceId(Integer resourceId);
    List<Software> findByStatusAndResourceId(String status, Integer resourceId);
}
