package com.hackaboss.PruebaTecnica4;

import com.hackaboss.PruebaTecnica4.model.Flight;
import com.hackaboss.PruebaTecnica4.repository.FlightRepository;
import com.hackaboss.PruebaTecnica4.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class FlightTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    private List<Flight> mockList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockList = new ArrayList<>();
    }

    /**
     * Prueba unitaria para verificar que el método `findAllFlight()`
     * de `FlightService` retorna correctamente una lista de vuelos.
     *
     * Se simula un repositorio con un vuelo de prueba y se valida que:
     * - La lista no sea nula.
     * - Contenga exactamente 1 vuelo.
     */
    @Test
    public void getFlights() {
        mockList.add(new Flight("AA1", "Estocolmo", "Austria", LocalDate.parse("2024-02-15"), 200, 100.00, null));
        when(flightRepository.findAll()).thenReturn(mockList);

        List<Flight> flightList = flightService.findAllFlight();

        assertNotNull(flightList);
        assertEquals(1, flightList.size());
    }

    /**
     * Prueba unitaria para verificar que el método `findAllFlight()`
     * retorna una lista vacía cuando no hay vuelos en la base de datos.
     *
     * Se simula un repositorio vacío y se valida que:
     * - La lista no sea nula.
     * - La lista esté vacía.
     */
    @Test
    public void getFlightsEmpty() {
        when(flightRepository.findAll()).thenReturn(mockList);

        List<Flight> flightList = flightService.findAllFlight();

        assertNotNull(flightList);
        assertTrue(flightList.isEmpty());
    }

    /**
     * Prueba unitaria para verificar que el método `findAvailableFlightWithOriginAndDestinationForDates()`
     * retorna correctamente una lista de vuelos entre ciertas fechas y con un origen y destino específicos.
     *
     * Se simula un repositorio con un vuelo de prueba y se valida que:
     * - La lista no sea nula.
     * - Contenga exactamente 1 vuelo con los parámetros indicados.
     */
    @Test
    public void getFlightsBetweenDatesAndCity() {
        Flight flight = new Flight("AA1", "Estocolmo", "Austria", LocalDate.parse("2024-02-15"), 200, 100.00, null);
        mockList.add(flight);

        when(flightRepository.findByOriginAndDestination("Estocolmo", "Austria")).thenReturn(mockList);

        List<Flight> flightList = flightService.findAvailableFlightWithOriginAndDestinationForDates(
                "Estocolmo", "Austria",
                LocalDate.parse("2024-02-15"),
                LocalDate.parse("2025-02-15")
        );

        assertNotNull(flightList);
        assertEquals(1, flightList.size());
    }
}
