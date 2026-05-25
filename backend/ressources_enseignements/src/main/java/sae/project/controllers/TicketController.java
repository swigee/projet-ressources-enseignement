package sae.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import sae.project.dtos.ticket.TicketDto;
import sae.project.services.TicketService;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "http://localhost:4200")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping
    public void createTicket(@RequestBody TicketDto ticketDto) {
        ticketService.createTicket(ticketDto);
    }

    @GetMapping
    public java.util.List<sae.project.dtos.ticket.TicketResponseDto> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @PutMapping("/{id}/status")
    public void updateTicketStatus(@PathVariable Integer id, @RequestBody java.util.Map<String, String> body) {
        String status = body.get("status");
        ticketService.updateStatus(id, status);
    }
}