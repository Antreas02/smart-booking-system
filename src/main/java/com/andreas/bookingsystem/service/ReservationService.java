package com.andreas.bookingsystem.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.andreas.bookingsystem.repository.ReservationRepository;
import java.time.LocalDateTime;
import java.util.List;
import com.andreas.bookingsystem.model.Reservation;
import com.andreas.bookingsystem.model.ReservationStatus;
import com.andreas.bookingsystem.model.Room;
import com.andreas.bookingsystem.repository.RoomRepository;
import com.andreas.bookingsystem.model.User;
import com.andreas.bookingsystem.repository.UserRepository;
import com.andreas.bookingsystem.model.UserRole;
import com.andreas.bookingsystem.dto.ReservationRequest;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;
    private RoomRepository roomRepository;
    private UserRepository userRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            RoomRepository roomRepository,
            UserRepository userRepository) {

        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public List<Reservation> getAllReservations(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found."));

        if (user.getRole() == UserRole.ADMIN) {
            return reservationRepository.findAll();
        }

        return reservationRepository.findByUserEmail(userEmail);
    }

    public Reservation createReservation(ReservationRequest reservationRequest, String userEmail) {

        Room room = roomRepository.findById(reservationRequest.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found."));

        if (!room.isAvailable()) {
            throw new RuntimeException("This room is not available for reservations.");
        }

        if (reservationRequest.getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot create a reservation in the past.");
        }

        if (!reservationRequest.getStartTime().isBefore(reservationRequest.getEndTime())) {
            throw new RuntimeException("End time must be after start time.");
        }

        List<Reservation> existingReservations = reservationRepository.findByRoomId(room.getId());

        for (Reservation existingReservation : existingReservations) {
            if (existingReservation.getStatus() != ReservationStatus.CANCELLED
                    && reservationRequest.getStartTime().isBefore(existingReservation.getEndTime())
                    && reservationRequest.getEndTime().isAfter(existingReservation.getStartTime())) {

                throw new RuntimeException("This reservation overlaps with another reservation.");
            }
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found."));

        Reservation newReservation = new Reservation(
                user,
                room,
                reservationRequest.getStartTime(),
                reservationRequest.getEndTime(),
                reservationRequest.getStatus());

        return reservationRepository.save(newReservation);
    }

    public Reservation getReservationById(Long id, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found."));

        if (user.getRole() == UserRole.ADMIN) {
            return reservationRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Reservation not found"));
        }

        return reservationRepository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Reservation not found"));
    }

    public Reservation updateReservation(Long id, ReservationRequest reservationRequest, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found."));

        Reservation existingReservation;

        if (user.getRole() == UserRole.ADMIN) {
            existingReservation = reservationRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Reservation not found."));
        } else {
            existingReservation = reservationRepository.findByIdAndUserEmail(id, userEmail)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Reservation not found."));
        }

        Room room = roomRepository.findById(reservationRequest.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found."));

        if (!room.isAvailable()) {
            throw new RuntimeException("This room is not available for reservations.");
        }

        if (reservationRequest.getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot update a reservation to a past date.");
        }

        if (!reservationRequest.getStartTime().isBefore(reservationRequest.getEndTime())) {
            throw new RuntimeException("End time must be after start time.");
        }

        List<Reservation> existingReservations = reservationRepository
                .findByRoomId(reservationRequest.getRoomId());

        for (Reservation reservation : existingReservations) {

            if (!reservation.getId().equals(existingReservation.getId())
                    && reservation.getStatus() != ReservationStatus.CANCELLED
                    && reservationRequest.getStartTime().isBefore(reservation.getEndTime())
                    && reservationRequest.getEndTime().isAfter(reservation.getStartTime())) {

                throw new RuntimeException("This reservation overlaps with another reservation.");
            }
        }

        existingReservation.setRoom(room);
        existingReservation.setStartTime(reservationRequest.getStartTime());
        existingReservation.setEndTime(reservationRequest.getEndTime());
        existingReservation.setStatus(reservationRequest.getStatus());

        return reservationRepository.save(existingReservation);
    }

    public void deleteReservation(Long id, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found."));

        Reservation reservation;

        if (user.getRole() == UserRole.ADMIN) {
            reservation = reservationRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Reservation not found."));
        } else {
            reservation = reservationRepository.findByIdAndUserEmail(id, userEmail)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Reservation not found."));
        }

        reservationRepository.delete(reservation);
    }

}
