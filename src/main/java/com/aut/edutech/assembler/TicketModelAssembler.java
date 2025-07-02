package com.aut.edutech.assembler;

import com.aut.edutech.controller.TicketController;
import com.aut.edutech.model.Ticket;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class TicketModelAssembler
        implements RepresentationModelAssembler<Ticket, EntityModel<Ticket>> {

    @Override
    public EntityModel<Ticket> toModel(Ticket ticket) {
        return EntityModel.of(ticket,
            // enlace a este recurso
            linkTo(methodOn(TicketController.class)
                .obtenerTicketPorId(ticket.getId())).withSelfRel(),
            // enlace al listado
            linkTo(methodOn(TicketController.class)
                .obtenerTickets()).withRel("tickets"),
            // opcional: enlace para asignar
            linkTo(methodOn(TicketController.class)
                .asignarTicket(ticket.getId(), ticket.getAsignadoA()))
                .withRel("asignar"),
            // opcional: enlace para categorizar
            linkTo(methodOn(TicketController.class)
                .categorizarTicket(ticket.getId(), ticket.getCategoriaTicket()))
                .withRel("categorizar"),
            // opcional: enlace para actualizar
            linkTo(methodOn(TicketController.class)
                .actualizarTicket(ticket.getId(), ticket))
                .withRel("actualizar")
        );
    }
}
