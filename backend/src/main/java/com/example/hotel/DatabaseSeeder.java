package com.example.hotel;

import com.example.hotel.model.Hotel;
import com.example.hotel.model.Room;
import com.example.hotel.model.RoomType;
import com.example.hotel.model.User;
import com.example.hotel.repository.HotelRepository;
import com.example.hotel.repository.RoomRepository;
import com.example.hotel.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;

    public DatabaseSeeder(RoomRepository roomRepository, HotelRepository hotelRepository, UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            userRepository.save(new User("Admin", "admin@luxehotel.com", "admin", "ADMIN"));
        }

        if (hotelRepository.count() == 0) {
            Hotel hotelMumbai = new Hotel("LuxeHotel Mumbai", "Mumbai", "Marine Drive, Mumbai", "Premium sea-facing hotel.", "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&q=80&w=800");
            Hotel hotelDelhi = new Hotel("LuxeHotel Delhi", "Delhi", "Connaught Place, Delhi", "Luxury stay in the heart of the capital.", "https://images.unsplash.com/photo-1542314831-c6a4d2706822?auto=format&fit=crop&q=80&w=800");
            Hotel hotelBangalore = new Hotel("LuxeHotel Bangalore", "Bangalore", "MG Road, Bangalore", "High-tech luxury in India's Silicon Valley.", "https://images.unsplash.com/photo-1551882547-ff40c0d129df?auto=format&fit=crop&q=80&w=800");
            
            hotelRepository.saveAll(List.of(hotelMumbai, hotelDelhi, hotelBangalore));

            roomRepository.saveAll(List.of(
                new Room(hotelMumbai, "M-101", RoomType.STANDARD, new BigDecimal("8000.00"), "A cozy standard room with a city view.", "img/standard.png"),
                new Room(hotelMumbai, "M-201", RoomType.DELUXE, new BigDecimal("15000.00"), "A spacious deluxe room with a king-sized bed and ocean view.", "img/deluxe.png"),
                new Room(hotelMumbai, "M-301", RoomType.SUITE, new BigDecimal("35000.00"), "A luxurious suite with a private balcony, jacuzzi, and premium amenities.", "img/suite.png"),
                
                new Room(hotelDelhi, "D-101", RoomType.STANDARD, new BigDecimal("6000.00"), "A comfortable room for business travelers.", "img/std_d.png"),
                new Room(hotelDelhi, "D-201", RoomType.DELUXE, new BigDecimal("12000.00"), "Premium city-view room.", "img/dlx_d.png"),
                
                new Room(hotelBangalore, "B-101", RoomType.STANDARD, new BigDecimal("7500.00"), "Modern tech-enabled smart room.", "img/std_b.png"),
                new Room(hotelBangalore, "B-201", RoomType.DELUXE, new BigDecimal("14000.00"), "Spacious room with a panoramic city view.", "img/dlx_b.png"),
                new Room(hotelBangalore, "B-301", RoomType.SUITE, new BigDecimal("30000.00"), "Executive suite with luxury amenities and private office area.", "img/ste_b.png")
            ));
            System.out.println("Database seeded with hotels and dummy rooms.");
        }
    }
}
