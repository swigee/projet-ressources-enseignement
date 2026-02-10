package sae.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sae.project.model.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Integer> {

}
