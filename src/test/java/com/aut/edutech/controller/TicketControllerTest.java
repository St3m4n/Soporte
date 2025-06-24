package com.aut.edutech.controller;

import com.aut.edutech.model.CategoriaTicket;
import com.aut.edutech.model.EstadoTicket;
import com.aut.edutech.model.Ticket;
import com.aut.edutech.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    private MockMvc mockMvc;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();

        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitulo("Ticket 1");
        ticket.setDescripcionTicket("Descripción del ticket 1");
        ticket.setEstadoTicket(EstadoTicket.ABIERTO);
        ticket.setCategoriaTicket(CategoriaTicket.SOFTWARE);
        ticket.setAsignadoA("Usuario1");
        ticket.setCreadoPor("Admin");
    }

    @Test
    void testCrearTicket() throws Exception {
        Ticket nuevoTicket = new Ticket();
        nuevoTicket.setId(1L);
        nuevoTicket.setTitulo("Ticket 1");
        nuevoTicket.setDescripcionTicket("Descripción del ticket 1");
        nuevoTicket.setEstadoTicket(EstadoTicket.ABIERTO);
        nuevoTicket.setCategoriaTicket(CategoriaTicket.SOFTWARE);
        nuevoTicket.setAsignadoA("Usuario1");
        nuevoTicket.setCreadoPor("Admin");

        // Simula el comportamiento del servicio
        when(ticketService.crearTicket(any(Ticket.class))).thenReturn(nuevoTicket);

        mockMvc.perform(post("/api/tickets")
                .contentType("application/json")
                .content(
                        "{\"titulo\":\"Ticket 1\",\"descripcionTicket\":\"Descripción del ticket 1\",\"estadoTicket\":\"ABIERTO\",\"categoriaTicket\":\"SOFTWARE\",\"asignadoA\":\"Usuario1\",\"creadoPor\":\"Admin\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Ticket 1"));
    }

    @Test
    void testObtenerTodosLosTickets() throws Exception {
        when(ticketService.obtenerTodosLosTickets()).thenReturn(Arrays.asList(ticket));

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Ticket 1"));
    }

    @Test
    void testObtenerTicketPorId() throws Exception {
        when(ticketService.obtenerTicketPorId(1L)).thenReturn(Optional.of(ticket));

        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Ticket 1"));
    }

    @Test
    void testActualizarTicket() throws Exception {
        Ticket ticketActualizado = new Ticket();
        ticketActualizado.setId(1L);
        ticketActualizado.setTitulo("Ticket 1 Actualizado");
        ticketActualizado.setDescripcionTicket("Descripción actualizada");
        ticketActualizado.setEstadoTicket(EstadoTicket.CERRADO);
        ticketActualizado.setCategoriaTicket(CategoriaTicket.SOFTWARE);
        ticketActualizado.setAsignadoA("Usuario2");
        ticketActualizado.setCreadoPor("Admin");

        // Simula el comportamiento del servicio
        when(ticketService.actualizarTicket(eq(1L), any(Ticket.class))).thenReturn(ticketActualizado);

        mockMvc.perform(put("/api/tickets/1")
                .contentType("application/json")
                .content(
                        "{\"titulo\":\"Ticket 1 Actualizado\",\"descripcionTicket\":\"Descripción actualizada\",\"estadoTicket\":\"CERRADO\",\"categoriaTicket\":\"SOFTWARE\",\"asignadoA\":\"Usuario2\",\"creadoPor\":\"Admin\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Ticket 1 Actualizado"));
    }

    @Test
    void testAsignarTicket() throws Exception {
        Ticket ticketAsignado = new Ticket();
        ticketAsignado.setId(1L);
        ticketAsignado.setAsignadoA("Usuario2"); // Usuario2 debe ser asignado correctamente

        // Simula el comportamiento del servicio
        when(ticketService.asignarTicket(eq(1L), eq("Usuario2"))).thenReturn(ticketAsignado);

        mockMvc.perform(put("/api/tickets/1/asignar?usuarioId=Usuario2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.asignadoA").value("Usuario2"));
    }

    @Test
    void testCategorizarTicket() throws Exception {
        Ticket ticketCategorizado = new Ticket();
        ticketCategorizado.setId(1L);
        ticketCategorizado.setCategoriaTicket(CategoriaTicket.SOFTWARE); // Verificar que la categoría se asigna
                                                                         // correctamente

        // Simula el comportamiento del servicio
        when(ticketService.categorizarTicket(eq(1L), eq(CategoriaTicket.SOFTWARE))).thenReturn(ticketCategorizado);

        mockMvc.perform(put("/api/tickets/1/categorizar?categoria=SOFTWARE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoriaTicket").value("SOFTWARE"));
    }

    @Test
    void testCrearTicketConDatosIncorrectos() throws Exception {
        // Simula la creación de un ticket con datos incorrectos (falta algún campo)
        mockMvc.perform(post("/api/tickets")
                .contentType("application/json")
                .content(
                        "{\"titulo\":\"\",\"descripcionTicket\":\"\",\"estadoTicket\":\"\",\"categoriaTicket\":\"\",\"asignadoA\":\"\",\"creadoPor\":\"\"}"))
                .andExpect(status().isBadRequest()); // Esperamos un 400 Bad Request
    }

    @Test
    void testObtenerTicketPorIdNoExistente() throws Exception {
        // Simula que no existe un ticket con ID 999
        when(ticketService.obtenerTicketPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tickets/999"))
                .andExpect(status().isNotFound()); // Esperamos un 404 Not Found
    }

    @Test
    void testActualizarTicketNoExistente() throws Exception {
        Ticket ticketActualizado = new Ticket();
        ticketActualizado.setId(999L);
        ticketActualizado.setTitulo("Ticket No Existente");

        // Simula que no existe un ticket con ID 999
        when(ticketService.actualizarTicket(eq(999L), any(Ticket.class))).thenReturn(null);

        mockMvc.perform(put("/api/tickets/999")
                .contentType("application/json")
                .content("{\"titulo\":\"Ticket No Existente\"}"))
                .andExpect(status().isNotFound()); // Esperamos un 404 Not Found
    }

    @Test
    void testAsignarTicketConUsuarioNoExistente() throws Exception {
        // Simula que no existe el ticket con ID 999
        when(ticketService.asignarTicket(eq(999L), eq("UsuarioNoExistente"))).thenReturn(null);

        mockMvc.perform(put("/api/tickets/999/asignar?usuarioId=UsuarioNoExistente"))
                .andExpect(status().isNotFound()); // Esperamos un 404 Not Found
    }

}
