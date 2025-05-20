package com.aut.edutech.controller;

import com.aut.edutech.model.Ticket;
import com.aut.edutech.service.TicketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public ResponseEntity<Ticket> crearTicket(@RequestBody Ticket ticket) {
        Ticket nuevo = ticketService.crearTicket(ticket);
        return ResponseEntity.ok(nuevo);
    }

    @GetMapping
    public List<Ticket> obtenerTickets() {
        return ticketService.obtenerTodosLosTickets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> obtenerTicketPorId(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketService.obtenerTicketPorId(id);
        return ticket.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> actualizarTicket(@PathVariable Long id, @Valid @RequestBody Ticket ticketActualizado) {
        Ticket actualizado = ticketService.actualizarTicket(id, ticketActualizado);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTicket(@PathVariable Long id) {
        ticketService.eliminarTicket(id);
        return ResponseEntity.noContent().build();
    }
}
