package sae.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sae.project.model.Tickets;

public interface TicketRepository extends JpaRepository<Tickets, Integer> {

}