package com.hackaboss.PruebaTecnica4;

import com.hackaboss.PruebaTecnica4.dto.FlightBookingUpdateDto;
import com.hackaboss.PruebaTecnica4.model.Flight;
import com.hackaboss.PruebaTecnica4.model.FlightBooking;
import com.hackaboss.PruebaTecnica4.model.Person;
import com.hackaboss.PruebaTecnica4.repository.FlightBookingRepository;
import com.hackaboss.PruebaTecnica4.service.FlighBookingService;
import com.hackaboss.PruebaTecnica4.service.IFlightService;
import com.hackaboss.PruebaTecnica4.service.IPersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/**
 * Pruebas unitarias para la clase FlightBookingService.
 * Se prueban los métodos principales como guardar, buscar, actualizar y eliminar reservas de vuelo.
 */
class FlightBookingServiceTest {

    @Mock
    private FlightBookingRepository flightBookingRepository;

    @Mock
    private IFlightService flightService;

    @Mock
    private IPersonService personService;

    @InjectMocks
    private FlighBookingService flightBookingService;

    private Flight flight;
    private FlightBooking flightBooking;
    private Person person;
    /**
     * Configuración inicial antes de cada prueba.
     * Se inicializan los mocks y se crea una reserva de vuelo de prueba.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        flight = new Flight("FL123", "Madrid", "Paris", LocalDate.now().plusDays(1), 200, 100.0, null);
        person = new Person("12345678A", "john.doe@example.com", "Doe", "John");
        List<Person> persons = new ArrayList<>();
        persons.add(person);
        flightBooking = new FlightBooking(1L,  1,LocalDate.now().plusDays(1), "economy", 100.0, flight, persons);
    }
    /**
     * Prueba para verificar que una reserva de vuelo se guarda correctamente si el vuelo está disponible.
     */
    @Test
    void saveFlightBooking_Success() {
        when(flightService.findFlightById(flight.getFlightCode())).thenReturn(flight);
        when(personService.findPersonById(person.getDni())).thenReturn(person);
        when(flightBookingRepository.save(any(FlightBooking.class))).thenReturn(flightBooking);

        FlightBooking savedBooking = flightBookingService.saveFlightBooking(flightBooking);

        assertNotNull(savedBooking);
        assertEquals(flightBooking.getFlight().getFlightCode(), savedBooking.getFlight().getFlightCode());
    }
    /**
     * Prueba para verificar que no se guarda una reserva si el vuelo no está disponible.
     */
    @Test
    void saveFlightBooking_Fail_NoAvailableFlight() {
        when(flightService.findFlightById(flight.getFlightCode())).thenReturn(null);

        FlightBooking savedBooking = flightBookingService.saveFlightBooking(flightBooking);

        assertNull(savedBooking);
    }
    /**
     * Prueba para verificar que el servicio retorna todas las reservas de vuelos correctamente.
     */
    @Test
    void findAllFlightBookings() {
        List<FlightBooking> bookings = new ArrayList<>();
        bookings.add(flightBooking);
        when(flightBookingRepository.findAll()).thenReturn(bookings);

        List<FlightBooking> result = flightBookingService.findAllFlightBooking();

        assertNotNull(result);
        assertEquals(1, result.size());
    }
    /**
     * Prueba para verificar que una reserva de vuelo se obtiene correctamente por su ID.
     */
    @Test
    void findFlightBookingById_Success() {
        when(flightBookingRepository.findById(flightBooking.getId())).thenReturn(Optional.of(flightBooking));

        FlightBooking foundBooking = flightBookingService.findFlightBookingById(flightBooking.getId());

        assertNotNull(foundBooking);
        assertEquals(flightBooking.getId(), foundBooking.getId());
    }
    /**
     * Prueba para verificar que una reserva de vuelo se actualiza correctamente.
     */
    @Test
    void updateFlightBooking_Success() {
        FlightBookingUpdateDto updateDto = new FlightBookingUpdateDto("business", 200.0);
        when(flightBookingRepository.findById(flightBooking.getId())).thenReturn(Optional.of(flightBooking));
        when(flightBookingRepository.save(any(FlightBooking.class))).thenReturn(flightBooking);

        FlightBooking updatedBooking = flightBookingService.updateFlightBooking(flightBooking.getId(), updateDto);

        assertNotNull(updatedBooking);
        assertEquals(updateDto.getSeatType(), updatedBooking.getSeatType());
        assertEquals(updateDto.getSeatPrice(), updatedBooking.getTotalPrice());
    }

    /**
     * Prueba para verificar que una reserva de vuelo se elimina correctamente.
     */
    @Test
    void deleteFlightBooking_Success() {
        when(flightBookingRepository.existsById(flightBooking.getId())).thenReturn(true);
        when(flightBookingRepository.findById(flightBooking.getId())).thenReturn(Optional.of(flightBooking));
        doNothing().when(flightBookingRepository).delete(flightBooking);

        FlightBooking deletedBooking = flightBookingService.deleteFlightBooking(flightBooking.getId());

        assertNotNull(deletedBooking);
        assertEquals(flightBooking.getId(), deletedBooking.getId());
    }
}
