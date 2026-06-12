package com.example.hotel.repository;

import com.example.hotel.model.Room;
import com.example.hotel.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByType(RoomType type);

    @Query("SELECT r FROM Room r WHERE r.id NOT IN " +
           "(SELECT res.room.id FROM Reservation res WHERE " +
           "(res.checkInDate < :checkOut AND res.checkOutDate > :checkIn))")
    List<Room> findAvailableRooms(@Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);

    @Query("SELECT r FROM Room r WHERE LOWER(r.hotel.city) LIKE LOWER(CONCAT('%', :city, '%')) " +
           "AND LOWER(r.hotel.name) LIKE LOWER(CONCAT('%', :hotelName, '%')) AND r.id NOT IN " +
           "(SELECT res.room.id FROM Reservation res WHERE " +
           "(res.checkInDate < :checkOut AND res.checkOutDate > :checkIn))")
    List<Room> findAvailableRoomsByCityAndHotel(@Param("city") String city, @Param("hotelName") String hotelName, @Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);

    @Query("SELECT r FROM Room r WHERE LOWER(r.hotel.city) LIKE LOWER(CONCAT('%', :city, '%')) " +
           "AND LOWER(r.hotel.name) LIKE LOWER(CONCAT('%', :hotelName, '%'))")
    List<Room> findByCityAndHotelName(@Param("city") String city, @Param("hotelName") String hotelName);
}
