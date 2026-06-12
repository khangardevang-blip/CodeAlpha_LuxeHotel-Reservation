package com.example.hotel.controller;

import com.example.hotel.model.User;
import com.example.hotel.model.Reservation;
import com.example.hotel.repository.UserRepository;
import com.example.hotel.repository.ReservationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    public AdminController(UserRepository userRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationRepository.findAll());
    }

    @PutMapping("/reservations/{id}/status")
    public ResponseEntity<?> updateReservationStatus(@PathVariable Long id, @RequestBody java.util.Map<String, Boolean> payload) {
        return reservationRepository.findById(id).map(reservation -> {
            boolean isPaid = payload.getOrDefault("isPaid", false);
            reservation.setPaid(isPaid);
            if (isPaid) {
                reservation.setPaymentMethod("MANUAL_CLEAR");
            } else {
                reservation.setPaymentMethod("PENDING");
            }
            reservationRepository.save(reservation);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
