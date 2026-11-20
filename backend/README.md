# Smart Booking and Reservation System

A backend application for managing rooms and reservations. I built this project to practise developing a Java application with Spring Boot, working with a PostgreSQL database, and adding authentication and access control.

## Features

- User registration and login
- JWT-based authentication
- Two user roles: `USER` and `ADMIN`
- Create, view, update, and delete rooms
- Create, view, update, and cancel reservations
- Reservations are linked to the user who created them and the room being booked
- Validation to prevent invalid dates and overlapping reservations
- Access control for protected operations

## Technologies

- Java 21
- Spring Boot 4
- Spring Data JPA
- Spring Security
- JSON Web Tokens (JWT)
- PostgreSQL
- Maven
- Docker and Docker Compose

## Running with Docker

You will need [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed.

1. Clone the repository:

   ```bash
   git clone https://github.com/Antreas02/smart-booking-system.git
   cd smart-booking-system
   ```

2. Create a file named `.env` in the project root directory.

3. Add the following variables to the file, replacing the example values with your own:

   ```env
   DB_PASSWORD=your_database_password
   APP_JWT_SECRET=your_long_random_secret
   ```

4. Build and start the application:

   ```bash
   docker compose up --build -d
   ```

The application will be available at:

`http://localhost:8080`

To stop the containers, run:

```bash
docker compose down
```

## Running locally

To run the application without Docker, you will need Java 21 and PostgreSQL installed.

1. Create a PostgreSQL database named `booking_system`.
2. Create the `.env` file in the project root with the `DB_PASSWORD` and `APP_JWT_SECRET` variables shown above.
3. Make sure PostgreSQL is running on `localhost:5432`.
4. Start the application from the project directory.

**Windows:**

```powershell
.\mvnw.cmd spring-boot:run
```

**macOS / Linux:**

```bash
./mvnw spring-boot:run
```

## Running the tests

Run the test suite with the Maven wrapper:

**Windows:**

```powershell
.\mvnw.cmd test
```

**macOS / Linux:**

```bash
./mvnw test
```

## Notes

- The project currently contains the backend API; there is no frontend in this repository yet.
- Do not commit your `.env` file. It contains local credentials and is excluded through `.gitignore`.