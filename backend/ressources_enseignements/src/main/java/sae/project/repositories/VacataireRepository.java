package sae.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sae.project.model.Vacataire;

import java.util.List;
import java.util.Optional;

@Repository
public interface VacataireRepository extends JpaRepository<Vacataire, Integer> {

    List<Vacataire> findByStatut(String statut);

    @Query("SELECT v FROM Vacataire v WHERE LOWER(v.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(v.prenom) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Vacataire> searchByName(@Param("keyword") String keyword);

    /** Find a contractor profile by its linked user account ID. */
    Optional<Vacataire> findByUser_Id(Integer userId);

    /** List all contractor profiles that already have a linked user account. */
    List<Vacataire> findByUserIsNotNull();

    /** List all contractor profiles that do not yet have a linked user account. */
    List<Vacataire> findByUserIsNull();
}
