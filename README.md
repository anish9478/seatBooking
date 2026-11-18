# Seat Booking System

A backend application built using **Java, Spring Boot, Spring Security, JWT Authentication, and MySQL** that allows users to view available seats, book seats, cancel bookings, and manage reservations securely.

## Features

### Authentication & Authorization
- User registration and login.
- JWT-based authentication.
- Password encryption using BCrypt.
- Role-based authorization (USER and ADMIN).
- Secure API endpoints.

### Seat Management
- Create new seats (Admin only).
- View all available seats.
- Retrieve seat details by ID.
- Prevent duplicate seat bookings.

### Booking Management
- Book available seats.
- View personal booking history.
- Cancel confirmed bookings.
- Prevent unauthorized booking cancellations.
- Prevent multiple bookings of the same seat.
- Handle concurrent booking requests using optimistic locking.

### Admin Dashboard
- View total seats.
- View available seats.
- View confirmed bookings.
- View total registered users.
- View all bookings.

### Exception Handling
- Centralized exception handling.
- Custom exceptions for booking conflicts, unauthorized operations, and missing resources.
- Appropriate HTTP status codes.

## Technologies Used

| Technology | Purpose |
|---|---|
| Java | Backend programming |
| Spring Boot | Application framework |
| Spring Security | Authentication and authorization |
| Spring Data JPA | Database operations |
| Hibernate | ORM |
| MySQL | Relational database |
| JWT | Token-based authentication |
| BCrypt | Password encryption |
| Maven | Dependency management |
| JUnit 5 | Unit testing |
| MockMvc | API integration testing |
| Postman | API testing |

## Project Structure

```text
src
 ├── main
 │   ├── java/com/anish/seatbooking
 │   │   ├── config
 │   │   ├── controller
 │   │   ├── dtos
 │   │   ├── entity
 │   │   ├── exception
 │   │   ├── repository
 │   │   ├── service
 │   │   └── SeatbookingApplication.java
 │   └── resources
 │       └── application.properties
 │
 └── test
     └── java/com/anish/seatbooking
```

## Database Design

The application uses MySQL with three main entities:

- **User:** Stores user information, encrypted passwords, and roles.
- **Seat:** Stores seat numbers, booking status, and version information.
- **Booking:** Stores booking details, associated users, seats, booking time, and booking status.

Relationships:
- One user can have multiple bookings.
- One seat can have multiple booking records over time.
- Each booking belongs to one user and one seat.

## API Endpoints

### Authentication

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a user |
| POST | `/api/auth/login` | Login and generate JWT |
| POST | `/api/auth/admin` | Create an admin (ADMIN only) |

### Seat Management

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/seat` | Create a seat (ADMIN) |
| GET | `/api/seat` | Get all seats |
| GET | `/api/seat/{id}` | Get seat by ID |

### Booking Management

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/bookings/{seatId}` | Book a seat |
| GET | `/api/bookings/my` | Get logged-in user's bookings |
| DELETE | `/api/bookings/{bookingId}` | Cancel a booking |

### Admin Dashboard

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/admin/dashboard` | Get dashboard statistics |
| GET | `/api/admin/bookings` | View all bookings |

Protected endpoints require a JWT token in the Authorization header:

```text
Authorization: Bearer YOUR_JWT_TOKEN
```

## Installation and Setup

### Prerequisites

- Java 21 or compatible configured Java version.
- MySQL.
- Maven.
- IntelliJ IDEA or another Java IDE.
- Postman (for API testing).

### 1. Clone the Repository

```bash
git clone https://github.com/anish9478/seatBooking.git
cd seatBooking
```

### 2. Create MySQL Database

```sql
CREATE DATABASE seatbooking_db;
```

### 3. Configure Environment Variables

Configure your database credentials and JWT settings in `application.properties`.

Example:

```properties
spring.application.name=seatbooking

spring.datasource.url=jdbc:mysql://localhost:3306/seatbooking_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=${JWT_SECRET}
jwt.expiration=86400000
```

Set the environment variables before starting the application:

- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`

Use a securely generated Base64-encoded JWT secret of at least 256 bits. Never commit real credentials or secrets to GitHub.

### 4. Run the Application

```bash
./mvnw spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

## Testing

Run the complete test suite:

```bash
./mvnw clean test
```

The project currently has **29 passing tests**, covering service logic, authentication, authorization, booking operations, concurrent booking, and API integration.

## Key Learning Outcomes

- Building REST APIs using Spring Boot.
- Implementing JWT authentication.
- Applying role-based authorization with Spring Security.
- Designing relational database relationships.
- Managing transactions using Spring Data JPA.
- Handling concurrent booking requests with optimistic locking
