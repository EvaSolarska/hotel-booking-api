# Hotel Booking API

A RESTful API for hotel management and room reservations, built with Spring Boot, Spring Security, JWT authentication, and PostgreSQL. The application supports hotel and room management, reservation handling, and role-based access control (RBAC) with three roles: Admin, Hotel Manager, and User.

## Technologies Used

- **Java & Spring Boot**
- **Spring Security & JWT**
- **Spring Data JPA & Hibernate**
- **PostgreSQL**
- **Jakarta Bean Validation**
- **MapStruct**
- **Lombok**
- **JUnit**
- **Docker & Docker Compose**
- **Swagger UI**

## Roles

The system defines three roles:

- **`ADMIN`**: Full access to the system, including user management, hotel creation, and hotel manager assignments.
- **`HOTEL_MANAGER`**: Can manage hotel details, rooms, and reservations for assigned hotels only.
- **`USER`**: Can browse hotels and rooms, create reservations, and cancel their own bookings.


## Getting Started

The application can be run using **Docker Compose** or **locally**.

### 1. Clone the Repository

Clone the repository and navigate to the project directory:

``` 
git clone https://github.com/EvaSolarska/hotel-booking-api.git
cd hotel-booking-api
```

### 2. Configure Environment Variables

Copy the example environment file:

``` 
cp .env.example .env
```

Update the `.env` file with your configuration values.

### Run with Docker Compose

Build and start the application:

``` 
docker compose up --build -d
```

### Run Locally

1. Start your local PostgreSQL database.
2. Run the application using the Maven wrapper:

``` 
./mvnw spring-boot:run
```

The API will be available at http://localhost:8080.

## API Endpoints

API documentation is available through Swagger UI once the application is running.

- **Swagger UI:** http://localhost:8080/swagger-ui.html

### 1. Authentication (`/api/auth`)

Public endpoints. Both return a Bearer JWT token upon success.

| Method | Endpoint | Access | Description |
| --- | --- | --- | --- |
| `POST` | `/api/auth/register` | Public | Register a new user account |
| `POST` | `/api/auth/login` | Public |	Authenticate user |

---

### 2. Hotels (`/api/hotels`)

| Method | Endpoint | Access | Description |
| --- | --- | --- | --- |
| `POST` | `/api/hotels` | Admin | Create a new hotel |
| `GET` | `/api/hotels` | Public | List all hotels |
| `GET` | `/api/hotels/{id}` | Public | Get details of a specific hotel |
| `PUT` | `/api/hotels/{id}` | Admin / Hotel Manager | Update hotel details |
| `DELETE` | `/api/hotels/{id}` | Admin | Remove a hotel |
| `POST` | `/api/hotels/{id}/managers/{userId}` | Admin | Assign a manager to a hotel |
| `DELETE` | `/api/hotels/{id}/managers/{userId}` | Admin | Remove a manager from a hotel |
| `GET` | `/api/hotels/{id}/managers` | Admin | List all managers assigned to a hotel |

---

### 3. Rooms (`/api/hotels/{hotelId}/rooms`)

| Method | Endpoint | Access | Description |
| --- | --- | --- | --- |
| `POST` | `/api/hotels/{hotelId}/rooms` | Admin / Hotel Manager | Add a room to the specified hotel |
| `GET` | `/api/hotels/{hotelId}/rooms` | Public | List all rooms in a given hotel |
| `GET` | `/api/hotels/{hotelId}/rooms/{roomId}` | Public | Get details of a specific room |
| `PUT` | `/api/hotels/{hotelId}/rooms/{roomId}` | Admin / Hotel Manager | Update room details |
| `DELETE` | `/api/hotels/{hotelId}/rooms/{roomId}` | Admin / Hotel Manager | Remove a room from a hotel |

---

### 4. Reservations (`/api/reservations`)

| Method | Endpoint | Access | Description |
| --- | --- | --- | --- |
| `POST` | `/api/reservations` | Authenticated | Book a room (validates date overlaps and capacity) |
| `GET` | `/api/reservations` | Authenticated | List all reservations for the logged-in user |
| `GET` | `/api/reservations/{id}` | Admin / Hotel Manager / Owner | View detailed reservation information |
| `PATCH` | `/api/reservations/{id}/cancel` | Admin / Hotel Manager / Owner | Cancel a reservation |
| `GET` | `/api/reservations/hotel/{hotelId}` | Admin / Hotel Manager | View all reservations for a specific hotel |

---

### 5. User Management (`/api/users`)

| Method | Endpoint | Access | Description |
| --- | --- | --- | --- |
| `GET` | `/api/users` | Admin | List all registered users |
| `GET` | `/api/users/{id}` | Admin | Get user details by ID |
| `PUT` | `/api/users/{id}/role` | Admin | Change a user's system role |
| `DELETE` | `/api/users/{id}` | Admin | Delete a user account |
