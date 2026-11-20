package com.andreas.bookingsystem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.andreas.bookingsystem.repository.ReservationRepository;
import com.andreas.bookingsystem.repository.RoomRepository;
import com.andreas.bookingsystem.repository.UserRepository;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import com.andreas.bookingsystem.model.User;
import com.andreas.bookingsystem.model.UserRole;
import com.andreas.bookingsystem.dto.ReservationRequest;
import com.andreas.bookingsystem.model.Reservation;
import com.andreas.bookingsystem.model.Room;
import com.andreas.bookingsystem.service.ReservationService;
import java.time.LocalDateTime;
import com.andreas.bookingsystem.model.ReservationStatus;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

        @Mock
        private ReservationRepository reservationRepository;

        @Mock
        private RoomRepository roomRepository;

        @Mock
        private UserRepository userRepository;

        @InjectMocks
        private ReservationService reservationService;

        @Test
        public void getAllReservations() {
                User user1 = new User(
                                "Andreas",
                                "Adamou",
                                "user@example.com",
                                "password123",
                                UserRole.USER);

                Room room1 = new Room("Conference Room B", 10, "First Floor", true);

                Reservation reservation1 = new Reservation(
                                user1,
                                room1,
                                LocalDateTime.of(2026, 12, 15, 10, 0),
                                LocalDateTime.of(2026, 12, 15, 11, 0),
                                ReservationStatus.CONFIRMED);

                Reservation reservation2 = new Reservation(
                                user1,
                                room1,
                                LocalDateTime.of(2026, 12, 16, 12, 0),
                                LocalDateTime.of(2026, 12, 16, 13, 0),
                                ReservationStatus.CONFIRMED);

                List<Reservation> expectedReservations = List.of(reservation1, reservation2);

                when(userRepository.findByEmail("user@example.com"))
                                .thenReturn(Optional.of(user1));

                when(reservationRepository.findByUserEmail("user@example.com")).thenReturn(expectedReservations);

                List<Reservation> actualReservations = reservationService.getAllReservations("user@example.com");

                assertEquals(expectedReservations, actualReservations);
                verify(reservationRepository).findByUserEmail("user@example.com");
                verify(userRepository).findByEmail("user@example.com");
        }

        @Test
        public void getAllReservationsForAdmin() {
                User admin1 = new User(
                                "Andreas",
                                "Adamou",
                                "admin@example.com",
                                "password123",
                                UserRole.ADMIN);

                Room room1 = new Room("Conference Room C", 8, "First Floor", true);

                Reservation reservation1 = new Reservation(
                                admin1,
                                room1,
                                LocalDateTime.of(2026, 12, 15, 10, 0),
                                LocalDateTime.of(2026, 12, 15, 11, 0),
                                ReservationStatus.CONFIRMED);

                Reservation reservation2 = new Reservation(
                                admin1,
                                room1,
                                LocalDateTime.of(2026, 12, 16, 12, 0),
                                LocalDateTime.of(2026, 12, 16, 13, 0),
                                ReservationStatus.CONFIRMED);

                List<Reservation> expectedReservations = List.of(reservation1, reservation2);

                when(userRepository.findByEmail("admin@example.com"))
                                .thenReturn(Optional.of(admin1));

                when(reservationRepository.findAll()).thenReturn(expectedReservations);

                List<Reservation> actualReservations = reservationService.getAllReservations("admin@example.com");

                assertEquals(expectedReservations, actualReservations);
                verify(reservationRepository).findAll();
                verify(userRepository).findByEmail("admin@example.com");

        }

        @Test
        public void createReservation() {
                User user1 = new User(
                                "Andreas",
                                "Adamou",
                                "user@example.com",
                                "password123",
                                UserRole.USER);

                Room room1 = new Room("Conference Room C", 8, "First Floor", true);
                room1.setId(5L);

                LocalDateTime startTime = LocalDateTime.now().plusDays(10);
                LocalDateTime endTime = startTime.plusHours(1);

                ReservationRequest reservationRequest = new ReservationRequest();
                reservationRequest.setRoomId(5L);
                reservationRequest.setStartTime(startTime);
                reservationRequest.setEndTime(endTime);
                reservationRequest.setStatus(ReservationStatus.CONFIRMED);

                Reservation expectedReservation = new Reservation(
                                user1,
                                room1,
                                startTime,
                                endTime,
                                ReservationStatus.CONFIRMED);

                when(roomRepository.findById(5L))
                                .thenReturn(Optional.of(room1));

                when(reservationRepository.findByRoomId(5L))
                                .thenReturn(List.of());

                when(userRepository.findByEmail("user@example.com"))
                                .thenReturn(Optional.of(user1));

                when(reservationRepository.save(any(Reservation.class)))
                                .thenReturn(expectedReservation);

                Reservation savedReservation = reservationService.createReservation(
                                reservationRequest, "user@example.com");

                assertEquals(expectedReservation, savedReservation);

                verify(roomRepository).findById(5L);
                verify(reservationRepository).findByRoomId(5L);
                verify(userRepository).findByEmail("user@example.com");
                verify(reservationRepository).save(any(Reservation.class));
        }

        @Test
        public void createReservationWithUnavailableRoom() {
                User user1 = new User(
                                "Andreas",
                                "Adamou",
                                "user@example.com",
                                "password123",
                                UserRole.USER);

                Room room1 = new Room("Conference Room C", 8, "First Floor", false);
                room1.setId(5L);

                LocalDateTime startTime = LocalDateTime.now().plusDays(10);
                LocalDateTime endTime = startTime.plusHours(1);

                ReservationRequest reservationRequest = new ReservationRequest();
                reservationRequest.setRoomId(5L);
                reservationRequest.setStartTime(startTime);
                reservationRequest.setEndTime(endTime);
                reservationRequest.setStatus(ReservationStatus.CONFIRMED);

                when(roomRepository.findById(5L))
                                .thenReturn(Optional.of(room1));

                assertThrows(RuntimeException.class, () -> {
                        reservationService.createReservation(reservationRequest, "user@example.com");
                });
                verify(reservationRepository, never()).save(any(Reservation.class));

        }

        @Test
        public void getReservationById() {
                User user1 = new User(
                                "Andreas",
                                "Adamou",
                                "user@example.com",
                                "password123",
                                UserRole.USER);

                Room room1 = new Room("Conference Room C", 8, "First Floor", false);

                Reservation expectedReservation = new Reservation(
                                user1,
                                room1,
                                LocalDateTime.of(2026, 12, 16, 12, 0),
                                LocalDateTime.of(2026, 12, 16, 13, 0),
                                ReservationStatus.CONFIRMED);

                when(userRepository.findByEmail("user@example.com"))
                                .thenReturn(Optional.of(user1));

                when(reservationRepository.findByIdAndUserEmail(7L, "user@example.com"))
                                .thenReturn(Optional.of(expectedReservation));

                Reservation actualReservation = reservationService.getReservationById(7L, "user@example.com");

                assertEquals(expectedReservation, actualReservation);
                verify(userRepository).findByEmail("user@example.com");
                verify(reservationRepository).findByIdAndUserEmail(7L, "user@example.com");

        }

        @Test
        public void updateReservation() {
                User user1 = new User(
                                "Andreas",
                                "Adamou",
                                "user@example.com",
                                "password123",
                                UserRole.USER);

                Room room1 = new Room("Conference Room C", 8, "First Floor", true);
                room1.setId(5L);

                LocalDateTime startTime = LocalDateTime.now().plusDays(10);
                LocalDateTime endTime = startTime.plusHours(1);

                ReservationRequest reservationRequest = new ReservationRequest();
                reservationRequest.setRoomId(5L);
                reservationRequest.setStartTime(startTime);
                reservationRequest.setEndTime(endTime);
                reservationRequest.setStatus(ReservationStatus.CONFIRMED);

                Reservation existingReservation = new Reservation(
                                user1,
                                room1,
                                LocalDateTime.now().plusDays(5),
                                LocalDateTime.now().plusDays(5).plusHours(1),
                                ReservationStatus.CONFIRMED);

                existingReservation.setId(7L);

                when(userRepository.findByEmail("user@example.com"))
                                .thenReturn(Optional.of(user1));

                when(reservationRepository.findByIdAndUserEmail(7L, "user@example.com"))
                                .thenReturn(Optional.of(existingReservation));

                when(roomRepository.findById(5L))
                                .thenReturn(Optional.of(room1));

                when(reservationRepository.findByRoomId(5L))
                                .thenReturn(List.of(existingReservation));

                when(reservationRepository.save(any(Reservation.class)))
                                .thenReturn(existingReservation);

                Reservation actualReservation = reservationService.updateReservation(
                                7L,
                                reservationRequest,
                                "user@example.com");

                assertEquals(room1, actualReservation.getRoom());
                assertEquals(startTime, actualReservation.getStartTime());
                assertEquals(endTime, actualReservation.getEndTime());
                assertEquals(ReservationStatus.CONFIRMED, actualReservation.getStatus());

                verify(userRepository).findByEmail("user@example.com");
                verify(reservationRepository).findByIdAndUserEmail(7L, "user@example.com");
                verify(roomRepository).findById(5L);
                verify(reservationRepository).findByRoomId(5L);
                verify(reservationRepository).save(any(Reservation.class));
        }

        @Test
        public void deleteReservation() {
                User user1 = new User(
                                "Andreas",
                                "Adamou",
                                "user@example.com",
                                "password123",
                                UserRole.USER);

                Reservation existingReservation = new Reservation(
                                user1,
                                new Room("Conference Room C", 8, "First Floor", true),
                                LocalDateTime.now().plusDays(5),
                                LocalDateTime.now().plusDays(5).plusHours(1),
                                ReservationStatus.CONFIRMED);

                existingReservation.setId(7L);

                when(userRepository.findByEmail("user@example.com"))
                                .thenReturn(Optional.of(user1));

                when(reservationRepository.findByIdAndUserEmail(7L, "user@example.com"))
                                .thenReturn(Optional.of(existingReservation));

                reservationService.deleteReservation(7L, "user@example.com");

                verify(reservationRepository).delete(existingReservation);
                verify(userRepository).findByEmail("user@example.com");
                verify(reservationRepository).findByIdAndUserEmail(7L, "user@example.com");
        }

}
