
package com.andreas.bookingsystem.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.andreas.bookingsystem.dto.ReservationRequest;
import com.andreas.bookingsystem.model.Reservation;
import com.andreas.bookingsystem.service.ReservationService;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public List<Reservation> getAllReservations(Authentication authentication) {
        return reservationService.getAllReservations(authentication.getName());
    }

    @PostMapping
    public Reservation createReservation(
            @RequestBody ReservationRequest reservationRequest,
            Authentication authentication) {
        return reservationService.createReservation(
                reservationRequest,
                authentication.getName());
    }

    @GetMapping("/{id}")
    public Reservation getReservationById(
            @PathVariable Long id,
            Authentication authentication) {
        return reservationService.getReservationById(id, authentication.getName());
    }

    @PutMapping("/{id}")
    public Reservation updateReservation(
            @PathVariable Long id,
            @RequestBody ReservationRequest reservationRequest,
            Authentication authentication) {
        return reservationService.updateReservation(
                id,
                reservationRequest,
                authentication.getName());
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(
            @PathVariable Long id,
            Authentication authentication) {
        reservationService.deleteReservation(id, authentication.getName());
    }

    @PatchMapping("/{id}/cancel")
    public Reservation cancelReservation(
            @PathVariable Long id,
            Authentication authentication) {
        return reservationService.cancelReservation(id, authentication.getName());
    }
}
