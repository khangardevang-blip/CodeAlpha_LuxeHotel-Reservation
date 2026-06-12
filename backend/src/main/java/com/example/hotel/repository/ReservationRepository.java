package com.example.hotel.repository;

import com.example.hotel.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByBookingReference(String bookingReference);
    List<Reservation> findByGuestEmail(String guestEmail);
}
