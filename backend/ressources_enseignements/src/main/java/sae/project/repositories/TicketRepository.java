package sae.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sae.project.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    void deleteByUserId(Integer userId);
}