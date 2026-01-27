
package sae.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sae.project.model.Ressources;


public interface RessourcesRepository extends JpaRepository<Ressources, Integer> {
    
}
