package com.example.hotel.service;

import com.example.hotel.model.Hotel;
import com.example.hotel.model.Reservation;
import com.example.hotel.model.Room;
import com.example.hotel.repository.HotelRepository;
import com.example.hotel.repository.ReservationRepository;
import com.example.hotel.repository.RoomRepository;
import com.example.hotel.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class HotelService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final HotelRepository hotelRepository;
    private final ReviewRepository reviewRepository;

    public HotelService(RoomRepository roomRepository, ReservationRepository reservationRepository, HotelRepository hotelRepository, ReviewRepository reviewRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.hotelRepository = hotelRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public List<Hotel> getHotelsByCity(String city) {
        return hotelRepository.findByCity(city);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut, String city, String hotelName) {
        city = (city == null) ? "" : city.trim();
        hotelName = (hotelName == null) ? "" : hotelName.trim();

        if (!city.isEmpty() || !hotelName.isEmpty()) {
            if (checkIn != null && checkOut != null) {
                return roomRepository.findAvailableRoomsByCityAndHotel(city, hotelName, checkIn, checkOut);
            }
            return roomRepository.findByCityAndHotelName(city, hotelName);
        } else {
            if (checkIn != null && checkOut != null) {
                return roomRepository.findAvailableRooms(checkIn, checkOut);
            }
            return roomRepository.findAll();
        }
    }

    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    @Transactional
    public Reservation createReservation(Long roomId, String guestName, String guestEmail, LocalDate checkIn, LocalDate checkOut, Integer age, String aadhaarNumber, Integer familyMembers, boolean airportPickup, boolean spaPackage, boolean dailyBreakfast) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID"));

        long days = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (days <= 0) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        BigDecimal totalPrice = room.getPricePerNight().multiply(BigDecimal.valueOf(days));
        
        StringBuilder addonsBuilder = new StringBuilder();
        if (airportPickup) {
            totalPrice = totalPrice.add(BigDecimal.valueOf(2000));
            addonsBuilder.append("Airport Pickup, ");
        }
        if (spaPackage) {
            totalPrice = totalPrice.add(BigDecimal.valueOf(4500));
            addonsBuilder.append("Spa Package, ");
        }
        if (dailyBreakfast) {
            totalPrice = totalPrice.add(BigDecimal.valueOf(1000 * days));
            addonsBuilder.append("Daily Breakfast, ");
        }
        
        String addonsStr = addonsBuilder.toString();
        if (addonsStr.endsWith(", ")) {
            addonsStr = addonsStr.substring(0, addonsStr.length() - 2);
        }
        if (addonsStr.isEmpty()) {
            addonsStr = "None";
        }

        String bookingRef = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Reservation reservation = new Reservation(
                bookingRef, room, guestName, guestEmail, checkIn, checkOut, totalPrice, false, age, aadhaarNumber, familyMembers, addonsStr
        );

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsByEmail(String email) {
        return reservationRepository.findByGuestEmail(email);
    }

    @Transactional
    public void confirmPayment(String bookingReference, String paymentMethod) {
        Reservation reservation = reservationRepository.findByBookingReference(bookingReference)
                .orElseThrow(() -> new IllegalArgumentException("Invalid booking reference"));
        
        reservation.setPaymentMethod(paymentMethod);
        if ("CARD".equalsIgnoreCase(paymentMethod)) {
            reservation.setPaid(true);
        } else if ("CASH".equalsIgnoreCase(paymentMethod)) {
            reservation.setPaid(false); // Still pending payment at the hotel
        }
        
        reservationRepository.save(reservation);
    }

    public Optional<Reservation> getReservationByReference(String bookingReference) {
        return reservationRepository.findByBookingReference(bookingReference);
    }

    @Transactional
    public void cancelReservation(String bookingReference) {
        Reservation reservation = reservationRepository.findByBookingReference(bookingReference)
                .orElseThrow(() -> new IllegalArgumentException("Invalid booking reference"));
        reservationRepository.delete(reservation);
    }

    public List<com.example.hotel.model.Review> getRoomReviews(Long roomId) {
        return reviewRepository.findByRoomId(roomId);
    }

    @Transactional
    public com.example.hotel.model.Review addReview(Long roomId, String guestName, Integer rating, String comment) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID"));
        
        com.example.hotel.model.Review review = new com.example.hotel.model.Review(room, guestName, rating, comment);
        return reviewRepository.save(review);
    }
}
