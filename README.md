# Company Employee Management System

A Spring Boot web application for managing companies and their employees. Provides full CRUD operations with form-based authentication, input validation, and paginated views.

## Tech Stack

- **Java 8** with **Spring Boot 2.7**
- **Spring Data JPA** / Hibernate (MySQL)
- **Spring Security** (form login, BCrypt)
- **Thymeleaf** server-side templates
- **Lombok** for boilerplate reduction
- **H2** in-memory database for tests

## Project Structure

```
src/main/java/com/example/companyemployeespring/
├── config/          # Security configuration
├── controller/      # MVC controllers (Company, Employee, Main)
├── exception/       # Global exception handler, custom exceptions
├── model/           # JPA entities (Company, Employee)
├── repository/      # Spring Data repositories
└── service/         # Business logic layer
```

## Prerequisites

- Java 8+
- Maven 3.6+
- MySQL server

## Setup

1. Create a MySQL database:
   ```sql
   CREATE DATABASE company_employee;
   ```

2. Configure database credentials in `src/main/resources/application-dev.properties` (defaults to `root`/`root` on `localhost:3306`).

3. Build and run:
   ```bash
   mvn spring-boot:run
   ```

4. Open `http://localhost:8080` and log in with the default credentials (`admin` / `admin`).

## Running Tests

```bash
mvn test
```

Tests use an H2 in-memory database and require no external setup.

## Features

- **Company management** -- create, list, edit, and delete companies
- **Employee management** -- create, list, edit, delete, and view employee details
- **Pagination** -- 10 items per page for both listing views
- **Validation** -- server-side input validation with error messages
- **Security** -- all pages require authentication; form-based login with BCrypt
- **Exception handling** -- custom 404/500 error pages
