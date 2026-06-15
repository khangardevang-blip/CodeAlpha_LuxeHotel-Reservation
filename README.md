# Hotel Reservation System

A comprehensive and professional web application designed to handle hotel room bookings, user management, and administrative tasks. The system features a responsive frontend designed with HTML/CSS/JavaScript and a robust backend built with Java and Spring Boot.

---

## 🌟 Features

- **User Authentication:** Secure login and registration for users.
- **Room Search & Browsing:** View available rooms, check details, and filter by room types.
- **Booking & Reservations:** Seamless reservation flow including date selection and payment processing.
- **User Dashboard:** Manage past and upcoming bookings.
- **Admin Panel:** Administrative access to manage rooms, hotels, and all user reservations.
- **Reviews & Ratings:** Users can leave reviews for rooms they have stayed in.

---

## 💻 Tech Stack

- **Frontend:** HTML5, CSS3, Vanilla JavaScript
- **Backend:** Java, Spring Boot, Spring Web, Spring Data JPA
- **Build Tool:** Maven
- **Deployment:** Configured for Docker and Render (`render.yaml`)

---

## 🏗️ System Architecture

The following diagram illustrates the high-level architecture of the application:

```mermaid
graph TD;
    Client[Web Browser - Frontend UI] -->|HTTP REST/JSON| Controller[Spring Boot Controllers]
    Controller --> Service[Service Layer]
    Service --> Repository[Spring Data JPA Repositories]
    Repository --> Database[(Relational Database)]
    
    subgraph Backend
        Controller
        Service
        Repository
    end
```

---

## 🔄 Booking Algorithm Flow

The reservation process follows this sequential flow:

```mermaid
sequenceDiagram
    actor User
    participant Frontend as UI/Frontend
    participant API as Backend (HotelController)
    participant DB as Database

    User->>Frontend: Search for Rooms (Dates)
    Frontend->>API: GET /api/rooms?checkIn=...&checkOut=...
    API->>DB: Query Available Rooms
    DB-->>API: List of Rooms
    API-->>Frontend: Return JSON Rooms Data
    Frontend-->>User: Display Available Rooms
    
    User->>Frontend: Select Room & Click Book
    Frontend->>User: Prompt Login (if not authenticated)
    User->>Frontend: Provide Payment & Confirm
    Frontend->>API: POST /api/reservations (Payload)
    API->>DB: Validate & Save Reservation
    DB-->>API: Reservation Confirmed
    API-->>Frontend: 200 OK (Reservation ID)
    Frontend-->>User: Show Booking Confirmation
```

---

## 🗄️ Database Entity Relationship

The core data models and their relationships:

```mermaid
erDiagram
    USER ||--o{ RESERVATION : makes
    USER ||--o{ REVIEW : writes
    HOTEL ||--|{ ROOM : contains
    ROOM ||--o{ RESERVATION : booked_in
    ROOM ||--o{ REVIEW : receives

    USER {
        Long id
        String username
        String password
        String email
        String role
    }
    HOTEL {
        Long id
        String name
        String location
        String description
    }
    ROOM {
        Long id
        String roomNumber
        RoomType type
        Double pricePerNight
        Boolean isAvailable
    }
    RESERVATION {
        Long id
        Date checkIn
        Date checkOut
        Double totalPrice
        String status
    }
    REVIEW {
        Long id
        Integer rating
        String comment
    }
```

---

## 🚀 Getting Started

### Prerequisites
- Python 3.x (for running the frontend locally)
- Java 17+ (for backend)
- Maven (included via `mvnw`)

### Running the Backend

1. Navigate to the `backend` directory.
2. Run the Spring Boot application using the Maven wrapper:
   ```bash
   # On Windows
   .\mvnw.cmd spring-boot:run
   
   # On macOS/Linux
   ./mvnw spring-boot:run
   ```
3. The backend will start on `http://localhost:8080`.

### Running the Frontend

1. Navigate to the `frontend` directory.
2. Start a simple HTTP server (e.g., using Python):
   ```bash
   python -m http.server 3000
   ```
3. Open your browser and navigate to `http://localhost:3000`.

---

## 📁 Project Structure

```text
task 4/
├── backend/
│   ├── src/main/java/com/example/hotel/
│   │   ├── controller/      # REST API Endpoints
│   │   ├── model/           # JPA Entities (User, Room, Reservation)
│   │   ├── repository/      # Database Access Interfaces
│   │   └── service/         # Business Logic
│   └── pom.xml              # Maven dependencies
├── frontend/
│   ├── css/                 # Stylesheets
│   ├── js/                  # Frontend Logic
│   ├── img/                 # Assets
│   ├── index.html           # Home Page
│   └── ...                  # Other HTML views
└── render.yaml              # Deployment configuration
```
