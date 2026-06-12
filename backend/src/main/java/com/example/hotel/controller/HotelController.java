package com.example.hotel.controller;

import com.example.hotel.model.Hotel;
import com.example.hotel.model.Reservation;
import com.example.hotel.model.Room;
import com.example.hotel.service.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") 
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/hotels")
    public List<Hotel> getHotels(@RequestParam(required = false) String city) {
        if (city != null && !city.isBlank()) {
            return hotelService.getHotelsByCity(city);
        }
        return hotelService.getAllHotels();
    }

    @GetMapping("/rooms")
    public List<Room> getRooms(@RequestParam(required = false) LocalDate checkIn, 
                               @RequestParam(required = false) LocalDate checkOut,
                               @RequestParam(required = false) String city,
                               @RequestParam(required = false) String hotelName) {
        return hotelService.getAvailableRooms(checkIn, checkOut, city, hotelName);
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        return hotelService.getRoomById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/book/{roomId}")
    public ResponseEntity<?> createReservation(@PathVariable Long roomId,
                                               @RequestParam String guestName,
                                               @RequestParam String guestEmail,
                                               @RequestParam LocalDate checkIn,
                                               @RequestParam LocalDate checkOut,
                                               @RequestParam Integer age,
                                               @RequestParam String aadhaarNumber,
                                               @RequestParam Integer familyMembers,
                                               @RequestParam(defaultValue = "false") boolean airportPickup,
                                               @RequestParam(defaultValue = "false") boolean spaPackage,
                                               @RequestParam(defaultValue = "false") boolean dailyBreakfast) {
        try {
            Reservation reservation = hotelService.createReservation(roomId, guestName, guestEmail, checkIn, checkOut, age, aadhaarNumber, familyMembers, airportPickup, spaPackage, dailyBreakfast);
            return ResponseEntity.ok(reservation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/reservations/{bookingRef}")
    public ResponseEntity<Reservation> getReservation(@PathVariable String bookingRef) {
        return hotelService.getReservationByReference(bookingRef)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/reservations/user/{email}")
    public ResponseEntity<List<Reservation>> getUserReservations(@PathVariable String email) {
        return ResponseEntity.ok(hotelService.getReservationsByEmail(email));
    }

    @PostMapping("/payment/{bookingRef}")
    public ResponseEntity<?> confirmPayment(@PathVariable String bookingRef, @RequestParam(defaultValue = "CARD") String paymentMethod) {
        try {
            hotelService.confirmPayment(bookingRef, paymentMethod);
            return ResponseEntity.ok().body("Payment method confirmed");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not confirm payment: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/{bookingRef}")
    public ResponseEntity<?> cancelReservation(@PathVariable String bookingRef) {
        try {
            hotelService.cancelReservation(bookingRef);
            return ResponseEntity.ok().body("Reservation cancelled successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not cancel reservation: " + e.getMessage());
        }
    }

    @GetMapping("/reviews/{roomId}")
    public ResponseEntity<List<com.example.hotel.model.Review>> getRoomReviews(@PathVariable Long roomId) {
        return ResponseEntity.ok(hotelService.getRoomReviews(roomId));
    }

    @PostMapping("/reviews/{roomId}")
    public ResponseEntity<?> addReview(@PathVariable Long roomId,
                                       @RequestParam String guestName,
                                       @RequestParam Integer rating,
                                       @RequestParam String comment) {
        try {
            com.example.hotel.model.Review review = hotelService.addReview(roomId, guestName, rating, comment);
            return ResponseEntity.ok(review);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
