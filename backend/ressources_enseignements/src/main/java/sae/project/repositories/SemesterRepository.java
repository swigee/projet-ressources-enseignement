
package sae.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sae.project.model.Semester;

public interface SemesterRepository extends JpaRepository<Semester, Integer> {

}
