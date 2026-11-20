# Smart Booking & Reservation System

A full-stack booking and reservation management application built with Java, Spring Boot, React, and PostgreSQL. The system allows users to browse rooms, make reservations, and manage their bookings, while administrators can manage rooms and monitor the platform.

## Features

### User features
- User registration and login
- JWT-based authentication
- Browse available rooms
- Create and manage reservations
- View personal reservations
- Cancel reservations

### Admin features
- Admin dashboard with system statistics
- Create, update, and delete rooms
- View and manage reservations
- View registered users
- Role-based access control

### Reservation management
- Prevents overlapping reservations
- Validates reservation dates
- Associates reservations with users and rooms
- Prevents deletion of rooms with existing reservations

## Technologies

**Backend**
- Java 21
- Spring Boot 4
- Spring Data JPA
- Spring Security
- JWT Authentication
- Maven

**Frontend**
- React
- JavaScript
- Vite
- React Router
- CSS

**Database & Tools**
- PostgreSQL
- Docker
- Docker Compose
- Git

## Project Structure

```text
smart-booking-system/
├── backend/
│   ├── src/
│   ├── Dockerfile
│   ├── docker-compose.yml
│   └── pom.xml
│
├── frontend/
│   ├── public/
│   ├── src/
│   └── package.json
│
├── .gitignore
└── README.md
```

## Getting Started

### Prerequisites

- Java 21 (for running the backend locally)
- Node.js and npm
- Docker Desktop

### 1. Clone the repository

```bash
git clone https://github.com/Antreas02/smart-booking-system.git
cd smart-booking-system
```

### 2. Configure environment variables

Create a `.env` file inside the `backend` folder:

```env
DB_PASSWORD=your_database_password
APP_JWT_SECRET=your_long_random_secret
```

Use your own secure values. Never commit this file to GitHub.

### 3. Start the backend and database

Open a terminal in the `backend` folder:

```bash
cd backend
docker compose up --build -d
```

The backend API will be available at:

`http://localhost:8080`

### 4. Start the frontend

Open another terminal in the `frontend` folder:

```bash
cd frontend
npm install
npm run dev
```

The frontend will be available at:

`http://localhost:5173`

## Running Backend Tests

From the `backend` folder, run:

**Windows:**
```powershell
.\mvnw.cmd test
```

**macOS / Linux:**
```bash
./mvnw test
```

## Stopping the Application

From the `backend` folder:

```bash
docker compose down
```

## Authentication & Security

The application uses JWT-based authentication and BCrypt password hashing. Protected endpoints require a valid token, while administrator operations are restricted to users with the `ADMIN` role.

## Author

Andreas Adamou

GitHub: [Antreas02](https://github.com/Antreas02)