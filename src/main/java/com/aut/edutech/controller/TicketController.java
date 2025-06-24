package com.aut.edutech.controller;

import com.aut.edutech.model.CategoriaTicket;
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
    public ResponseEntity<Ticket> actualizarTicket(@PathVariable Long id, @RequestBody Ticket ticketActualizado) {
        Ticket actualizado = ticketService.actualizarTicket(id, ticketActualizado);
        if (actualizado == null) {
            return ResponseEntity.notFound().build(); // Retorna 404 si no se encuentra el ticket
        }
        return ResponseEntity.ok(actualizado); // Retorna 200 OK si se actualizó el ticket
    }

    @PutMapping("/{id}/asignar")
    public ResponseEntity<Ticket> asignarTicket(@PathVariable Long id, @RequestParam String usuarioId) {
        Ticket actualizado = ticketService.asignarTicket(id, usuarioId);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/categorizar")
    public ResponseEntity<Ticket> categorizarTicket(@PathVariable Long id, @RequestParam CategoriaTicket categoria) {
        Ticket actualizado = ticketService.categorizarTicket(id, categoria);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }
}
