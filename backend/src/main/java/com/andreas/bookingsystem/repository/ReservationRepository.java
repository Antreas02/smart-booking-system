package com.andreas.bookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.andreas.bookingsystem.model.Reservation;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    public List<Reservation> findByRoomId(Long roomId);

    List<Reservation> findByUserEmail(String email);

    Optional<Reservation> findByIdAndUserEmail(Long id, String email);
}
