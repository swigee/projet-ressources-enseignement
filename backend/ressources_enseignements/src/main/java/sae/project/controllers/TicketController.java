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
}