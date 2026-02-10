package sae.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sae.project.dtos.TicketDto;
import sae.project.model.Ticket;
import sae.project.model.User;
import sae.project.repositories.TicketRepository;
import sae.project.repositories.UserRepository;
import java.sql.Date;
import java.time.LocalDate;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketsRepository;
    @Autowired
    private UserRepository usersRepository;

    public void createTicket(TicketDto dto) {
        User user = usersRepository.findById(dto.userId).orElseThrow();

        Ticket ticket = new Ticket();
        ticket.setTitle(dto.title);
        ticket.setDescription(dto.description);
        ticket.setStatus("OPEN"); // Valeur par défaut
        ticket.setDate(Date.valueOf(LocalDate.now())); // Date d'aujourd'hui
        ticket.setUser(user);

        ticketsRepository.save(ticket);
    }
}