package sae.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sae.project.dtos.TicketDto;
import sae.project.model.Tickets;
import sae.project.model.Users;
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
        Users user = usersRepository.findById(dto.iduser).orElseThrow();

        Tickets ticket = new Tickets();
        ticket.setTitle(dto.title);
        ticket.setDescription(dto.description);
        ticket.setStatue("OPEN"); // Valeur par défaut
        ticket.setDate(Date.valueOf(LocalDate.now())); // Date d'aujourd'hui
        ticket.setIduser(user);

        ticketsRepository.save(ticket);
    }
}