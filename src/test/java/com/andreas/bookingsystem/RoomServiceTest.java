
package com.andreas.bookingsystem;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.andreas.bookingsystem.model.Room;
import com.andreas.bookingsystem.repository.RoomRepository;
import com.andreas.bookingsystem.repository.ReservationRepository;
import com.andreas.bookingsystem.service.RoomService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private RoomService roomService;

    @Test
    public void testGetAllRooms() {

        Room room1 = new Room("Meeting Room A", 5, "First Floor", true);
        Room room2 = new Room("Meeting Room B", 10, "Second Floor", true);
        Room room3 = new Room("Conference Room", 20, "Third Floor", true);

        List<Room> expectedRooms = List.of(room1, room2, room3);

        when(roomRepository.findAll()).thenReturn(expectedRooms);

        List<Room> actualRooms = roomService.getAllRooms();

        assertEquals(expectedRooms, actualRooms);
    }

    @Test
    public void createRoom() {
        Room newRoom = new Room("Meeting Room C", 8, "Fourth Floor", true);

        when(roomRepository.save(newRoom)).thenReturn(newRoom);

        Room savedRoom = roomService.createRoom(newRoom);

        assertEquals(newRoom, savedRoom);

        verify(roomRepository).save(newRoom);
    }

    @Test
    public void getRoomById() {
        Room newRoom = new Room("Meeting Room C", 8, "Fourth Floor", true);

        when(roomRepository.findById(5L)).thenReturn(Optional.of(newRoom));

        Optional<Room> actualRoom = roomService.getRoomById(5L);

        assertTrue(actualRoom.isPresent());
        assertEquals(newRoom, actualRoom.get());

        verify(roomRepository).findById(5L);
    }

    @Test
    public void fakeRoomId() {
        when(roomRepository.findById(10L)).thenReturn(Optional.empty());

        Optional<Room> actualRoom = roomService.getRoomById(10L);

        assertFalse(actualRoom.isPresent());

        verify(roomRepository).findById(10L);
    }

    @Test
    public void updatedRoom() {
        Room existingRoom = new Room("Meeting Room A", 5, "First Floor", true);
        Room newRoom = new Room("Meeting Room B", 10, "Second Floor", false);

        when(roomRepository.findById(5L)).thenReturn(Optional.of(existingRoom));

        Room result = roomService.updateRoom(5L, newRoom);

        assertEquals("Meeting Room B", result.getName());
        assertEquals(10, result.getCapacity());
        assertEquals("Second Floor", result.getLocation());
        assertFalse(result.isAvailable());

        verify(roomRepository).save(existingRoom);
    }

    @Test
    public void deleteRoom() {

        Room existingRoom = new Room("Meeting Room A", 5, "First Floor", true);

        when(roomRepository.findById(5L)).thenReturn(Optional.of(existingRoom));
        when(reservationRepository.findByRoomId(5L)).thenReturn(List.of());

        roomService.deleteRoom(5L);

        verify(roomRepository).delete(existingRoom);
    }

    @Test
    public void cannotDeleteRoomWithReservations() {

        Room existingRoom = new Room("Meeting Room A", 5, "First Floor", true);

        when(roomRepository.findById(5L)).thenReturn(Optional.of(existingRoom));
        when(reservationRepository.findByRoomId(5L))
                .thenReturn(List.of(new com.andreas.bookingsystem.model.Reservation()));

        assertThrows(
                IllegalStateException.class,
                () -> roomService.deleteRoom(5L));

        verify(roomRepository, never()).delete(existingRoom);
    }

}
