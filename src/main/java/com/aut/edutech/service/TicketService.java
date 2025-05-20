package com.aut.edutech.service;

import com.aut.edutech.model.Ticket;
import com.aut.edutech.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    public Ticket crearTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public List<Ticket> obtenerTodosLosTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> obtenerTicketPorId(Long id) {
        return ticketRepository.findById(id);
    }

    public Ticket actualizarTicket(Long id, Ticket ticketActualizado) {
        return ticketRepository.findById(id).map(ticket -> {
            ticket.setTitulo(ticketActualizado.getTitulo());
            ticket.setDescripcionTicket(ticketActualizado.getDescripcionTicket());
            ticket.setEstadoTicket(ticketActualizado.getEstadoTicket());
            ticket.setCategoriaTicket(ticketActualizado.getCategoriaTicket());
            ticket.setAsignadoA(ticketActualizado.getAsignadoA());
            ticket.setCreadoPor(ticketActualizado.getCreadoPor());
            return ticketRepository.save(ticket);
        }).orElseGet(() -> {
            ticketActualizado.setId(id);
            return ticketRepository.save(ticketActualizado);
        });
    }

    public void eliminarTicket(Long id) {
        ticketRepository.deleteById(id);
    }
}
