package com.aut.edutech.controller;

import com.aut.edutech.assembler.TicketModelAssembler;
import com.aut.edutech.model.CategoriaTicket;
import com.aut.edutech.model.Ticket;
import com.aut.edutech.service.TicketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketModelAssembler assembler;

    @PostMapping
    public ResponseEntity<EntityModel<Ticket>> crearTicket(@RequestBody Ticket ticket) {
        Ticket nuevo = ticketService.crearTicket(ticket);
        EntityModel<Ticket> model = assembler.toModel(nuevo);
        return ResponseEntity
            .created(model.getRequiredLink("self").toUri())
            .body(model);
    }

    @GetMapping
    public CollectionModel<EntityModel<Ticket>> obtenerTickets() {
        List<EntityModel<Ticket>> tickets = ticketService.obtenerTodosLosTickets().stream()
            .map(assembler::toModel)
            .toList();

        return CollectionModel.of(tickets,
            linkTo(methodOn(TicketController.class).obtenerTickets()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Ticket>> obtenerTicketPorId(@PathVariable Long id) {
        Optional<Ticket> opt = ticketService.obtenerTicketPorId(id);
        return opt
            .map(ticket -> ResponseEntity.ok(assembler.toModel(ticket)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Ticket>> actualizarTicket(
            @PathVariable Long id,
            @RequestBody Ticket ticketActualizado) {
        Ticket actualizado = ticketService.actualizarTicket(id, ticketActualizado);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @PutMapping("/{id}/asignar")
    public ResponseEntity<EntityModel<Ticket>> asignarTicket(
            @PathVariable Long id,
            @RequestParam String usuarioId) {
        Ticket actualizado = ticketService.asignarTicket(id, usuarioId);
        return actualizado != null
            ? ResponseEntity.ok(assembler.toModel(actualizado))
            : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/categorizar")
    public ResponseEntity<EntityModel<Ticket>> categorizarTicket(
            @PathVariable Long id,
            @RequestParam CategoriaTicket categoria) {
        Ticket actualizado = ticketService.categorizarTicket(id, categoria);
        return actualizado != null
            ? ResponseEntity.ok(assembler.toModel(actualizado))
            : ResponseEntity.notFound().build();
    }
}
