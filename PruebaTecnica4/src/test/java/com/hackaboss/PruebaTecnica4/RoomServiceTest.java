package com.hackaboss.PruebaTecnica4;

import com.hackaboss.PruebaTecnica4.dto.RoomUpdateDto;
import com.hackaboss.PruebaTecnica4.model.Hotel;
import com.hackaboss.PruebaTecnica4.model.Room;
import com.hackaboss.PruebaTecnica4.repository.HotelRepository;
import com.hackaboss.PruebaTecnica4.repository.RoomRepository;
import com.hackaboss.PruebaTecnica4.service.RoomService;
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

class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private RoomService roomService;

    private Room room;
    private Hotel hotel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        hotel = new Hotel("H123", "Madrid", "Gran Hotel");
        room = new Room("R001", LocalDate.now(), LocalDate.now().plusDays(10), true, 2, 100.0, hotel);
    }

    /**
     * Prueba para verificar que una habitación se guarda correctamente si el hotel existe.
     */
    @Test
    void saveRoom_Success() {
        when(hotelRepository.existsById(hotel.getHotelCode())).thenReturn(true);
        when(roomRepository.existsById(room.getRoomCode())).thenReturn(false);
        when(roomRepository.save(room)).thenReturn(room);

        Room savedRoom = roomService.saveRoom(room);

        assertNotNull(savedRoom);
        assertEquals(room.getRoomCode(), savedRoom.getRoomCode());
    }

    /**
     * Prueba para verificar que una habitación no se guarda si el hotel no existe.
     */
    @Test
    void saveRoom_Fail_HotelNotFound() {
        when(hotelRepository.existsById(hotel.getHotelCode())).thenReturn(false);

        Room savedRoom = roomService.saveRoom(room);

        assertNull(savedRoom);
    }

    /**
     * Prueba para verificar que el servicio retorna todas las habitaciones correctamente.
     */
    @Test
    void findAllRooms() {
        List<Room> rooms = new ArrayList<>();
        rooms.add(room);
        when(roomRepository.findAll()).thenReturn(rooms);

        List<Room> result = roomService.findAllRooms();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    /**
     * Prueba para verificar que se obtiene una habitación por su código correctamente.
     */
    @Test
    void findRoomById_Success() {
        when(roomRepository.findById(room.getRoomCode())).thenReturn(Optional.of(room));

        Room foundRoom = roomService.findRoomById(room.getRoomCode());

        assertNotNull(foundRoom);
        assertEquals(room.getRoomCode(), foundRoom.getRoomCode());
    }

    /**
     * Prueba para verificar que si no se encuentra una habitación, se retorna null.
     */
    @Test
    void findRoomById_NotFound() {
        when(roomRepository.findById("InvalidCode")).thenReturn(Optional.empty());

        Room foundRoom = roomService.findRoomById("InvalidCode");

        assertNull(foundRoom);
    }

    /**
     * Prueba para verificar que una habitación se actualiza correctamente.
     */
    @Test
    void updateRoom_Success() {
        RoomUpdateDto updateDto = new RoomUpdateDto(LocalDate.now(), LocalDate.now().plusDays(5), 3, true, 120.0);
        when(roomRepository.findById(room.getRoomCode())).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        Room updatedRoom = roomService.updateRoom(room.getRoomCode(), updateDto);

        assertNotNull(updatedRoom);
        assertEquals(updateDto.getNumBed(), updatedRoom.getNumBed());
        assertEquals(updateDto.getPricePerNight(), updatedRoom.getPricePerNight());
    }

    /**
     * Prueba para verificar que si una habitación no existe, no se actualiza.
     */
    @Test
    void updateRoom_NotFound() {
        RoomUpdateDto updateDto = new RoomUpdateDto(LocalDate.now(), LocalDate.now().plusDays(5), 3, true, 120.0);
        when(roomRepository.findById("InvalidCode")).thenReturn(Optional.empty());

        Room updatedRoom = roomService.updateRoom("InvalidCode", updateDto);

        assertNull(updatedRoom);
    }

    /**
     * Prueba para verificar que una habitación se elimina correctamente.
     */
    @Test
    void deleteRoom_Success() {
        when(roomRepository.existsById(room.getRoomCode())).thenReturn(true);
        when(roomRepository.findById(room.getRoomCode())).thenReturn(Optional.of(room));
        doNothing().when(roomRepository).delete(room);

        Room deletedRoom = roomService.deleteRoom(room.getRoomCode());

        assertNotNull(deletedRoom);
        assertEquals(room.getRoomCode(), deletedRoom.getRoomCode());
    }

    /**
     * Prueba para verificar que si una habitación no existe, no se elimina.
     */
    @Test
    void deleteRoom_NotFound() {
        when(roomRepository.existsById("InvalidCode")).thenReturn(false);

        Room deletedRoom = roomService.deleteRoom("InvalidCode");

        assertNull(deletedRoom);
    }
}
