package com.aut.edutech.service;

import com.aut.edutech.model.CategoriaTicket;
import com.aut.edutech.model.EstadoTicket;
import com.aut.edutech.model.Ticket;
import com.aut.edutech.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    private Ticket ticket;
    private Ticket ticketActualizado;
    private CategoriaTicket categoria;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup de datos comunes para los tests
        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitulo("Ticket 1");
        ticket.setDescripcionTicket("Descripción del ticket 1");
        ticket.setEstadoTicket(EstadoTicket.ABIERTO);
        ticket.setCategoriaTicket(CategoriaTicket.SOFTWARE);
        ticket.setAsignadoA("Usuario1");
        ticket.setCreadoPor("Admin");

        ticketActualizado = new Ticket();
        ticketActualizado.setId(1L);
        ticketActualizado.setTitulo("Ticket 1 Actualizado");
        ticketActualizado.setDescripcionTicket("Descripción actualizada");
        ticketActualizado.setEstadoTicket(EstadoTicket.CERRADO);
        ticketActualizado.setCategoriaTicket(CategoriaTicket.SOFTWARE);
        ticketActualizado.setAsignadoA("Usuario2");
        ticketActualizado.setCreadoPor("Admin");

        categoria = CategoriaTicket.SOFTWARE;
    }

    @Test
    void testCrearTicket() {
        when(ticketRepository.save(ticket)).thenReturn(ticket);

        Ticket resultado = ticketService.crearTicket(ticket);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getTitulo()).isEqualTo(ticket.getTitulo());
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void testObtenerTodosLosTickets() {
        when(ticketRepository.findAll()).thenReturn(Arrays.asList(ticket));

        List<Ticket> resultado = ticketService.obtenerTodosLosTickets();

        assertThat(resultado).isNotNull();
        assertThat(resultado.size()).isEqualTo(1);
        assertThat(resultado.get(0).getTitulo()).isEqualTo(ticket.getTitulo());
        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    void testObtenerTicketPorId() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        Optional<Ticket> resultado = ticketService.obtenerTicketPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getTitulo()).isEqualTo(ticket.getTitulo());
        verify(ticketRepository, times(1)).findById(1L);
    }

    @Test
    void testActualizarTicket() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(ticket)).thenReturn(ticketActualizado);

        Ticket resultado = ticketService.actualizarTicket(1L, ticketActualizado);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getTitulo()).isEqualTo(ticketActualizado.getTitulo());
        assertThat(resultado.getEstadoTicket()).isEqualTo(ticketActualizado.getEstadoTicket());
        verify(ticketRepository, times(1)).findById(1L);
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void testAsignarTicket() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(ticket)).thenReturn(ticket);

        Ticket resultado = ticketService.asignarTicket(1L, "Usuario2");

        assertThat(resultado).isNotNull();
        assertThat(resultado.getAsignadoA()).isEqualTo("Usuario2");
        verify(ticketRepository, times(1)).findById(1L);
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void testCategorizarTicket() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(ticket)).thenReturn(ticket);

        Ticket resultado = ticketService.categorizarTicket(1L, categoria);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getCategoriaTicket()).isEqualTo(categoria);
        verify(ticketRepository, times(1)).findById(1L);
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void testActualizarTicketNoExistenteEnService() {
        // Preparo un Ticket “de ejemplo” sin ID
        Ticket ticketToUpdate = new Ticket();
        ticketToUpdate.setTitulo("Título Nuevo");
        ticketToUpdate.setDescripcionTicket("Desc nueva");
        ticketToUpdate.setEstadoTicket(EstadoTicket.ABIERTO);
        ticketToUpdate.setCategoriaTicket(CategoriaTicket.SOFTWARE);
        ticketToUpdate.setAsignadoA("UsuarioX");
        ticketToUpdate.setCreadoPor("Admin");

        // Simulo que no lo encuentra en BD
        when(ticketRepository.findById(999L)).thenReturn(Optional.empty());
        // Al salvar, devuelvo tal cual el objeto que recibe
        when(ticketRepository.save(any(Ticket.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Ejecuto la operación
        Ticket resultado = ticketService.actualizarTicket(999L, ticketToUpdate);

        // Compruebo que entra en el elseGet: asigna el ID y salva
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(999L);
        assertThat(resultado.getTitulo()).isEqualTo("Título Nuevo");

        // Verificaciones de interacción
        verify(ticketRepository, times(1)).findById(999L);
        verify(ticketRepository, times(1)).save(ticketToUpdate);
    }
}