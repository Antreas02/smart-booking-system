package com.andreas.bookingsystem.service;

import org.springframework.stereotype.Service;

import com.andreas.bookingsystem.repository.RoomRepository;
import java.util.List;
import com.andreas.bookingsystem.model.Room;
import java.util.Optional;

@Service
public class RoomService {

    private RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
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
        Room existingRoom = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        existingRoom.setName(updatedRoom.getName());
        existingRoom.setCapacity(updatedRoom.getCapacity());
        existingRoom.setLocation(updatedRoom.getLocation());
        existingRoom.setAvailable(updatedRoom.isAvailable());
        roomRepository.save(existingRoom);
        return existingRoom;

    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
}
