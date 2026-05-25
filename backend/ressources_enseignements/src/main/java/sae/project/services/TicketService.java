package sae.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sae.project.dtos.ticket.TicketDto;
import sae.project.dtos.ticket.TicketResponseDto;
import sae.project.model.Ticket;
import sae.project.model.User;
import sae.project.repositories.TicketRepository;
import sae.project.repositories.UserRepository;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

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
        ticket.setStatus("OPEN");
        ticket.setDate(Date.valueOf(LocalDate.now())); 
        ticket.setUser(user);

        ticketsRepository.save(ticket);
    }

    public List<TicketResponseDto> getAllTickets() {
        return ticketsRepository.findAll().stream()
                .map(t -> new TicketResponseDto(
                        t.getId(),
                        t.getTitle(),
                        t.getDescription(),
                        t.getDate(),
                        t.getResolutionDate(),
                        t.getStatus(),
                        t.getUser() != null ? t.getUser().getId() : null,
                        t.getUser() != null ? t.getUser().getFirstName() + " " + t.getUser().getLastName()
                                : "Utilisateur Inconnu"))
                .collect(java.util.stream.Collectors.toList());
    }

    @jakarta.transaction.Transactional
    public void updateStatus(Integer ticketId, String status) {
        Ticket ticket = ticketsRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket introuvable"));
        ticket.setStatus(status);
        if ("RESOLVED".equals(status)) {
            ticket.setResolutionDate(Date.valueOf(LocalDate.now()));
        } else {
            ticket.setResolutionDate(null);
        }
        ticketsRepository.save(ticket);
    }
}