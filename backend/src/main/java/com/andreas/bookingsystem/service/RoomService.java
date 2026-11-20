
package com.andreas.bookingsystem.service;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.andreas.bookingsystem.repository.RoomRepository;
import com.andreas.bookingsystem.repository.ReservationRepository;
import java.util.List;
import com.andreas.bookingsystem.model.Room;
import java.util.Optional;

@Service
public class RoomService {

    private RoomRepository roomRepository;
    private ReservationRepository reservationRepository;

    public RoomService(
            RoomRepository roomRepository,
            ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room createRoom(Room newRoom) {
        return roomRepository.save(newRoom);
    }

    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    public Room updateRoom(Long id, Room updatedRoom) {
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        existingRoom.setName(updatedRoom.getName());
        existingRoom.setCapacity(updatedRoom.getCapacity());
        existingRoom.setLocation(updatedRoom.getLocation());
        existingRoom.setAvailable(updatedRoom.isAvailable());

        roomRepository.save(existingRoom);

        return existingRoom;
    }

    public void deleteRoom(Long id) {
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (!reservationRepository.findByRoomId(id).isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot delete this room because it has existing reservations.");
        }

        roomRepository.delete(existingRoom);
    }
}
