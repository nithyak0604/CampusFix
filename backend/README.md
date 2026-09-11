# CampusFix Backend

Spring Boot REST API for CampusFix. This project uses Java 21, Spring Boot, Spring Data JPA, Spring Security, Bean Validation, and MySQL.

## Prerequisites

This project uses Java 25, Spring Boot, Spring Data JPA, Spring Security, Bean Validation, and MySQL.
## Local configuration

Set the following environment variables before starting the application. Never commit their real values.

```powershell
$env:DB_USERNAME = "your_mysql_username"
$env:DB_PASSWORD = "your_mysql_password"
$env:JWT_SECRET = "a-long-random-secret-with-at-least-32-characters"
```

`DB_URL` defaults to a local `campusfix` MySQL database. See `.env.example` for all configuration values.

## Run

```powershell
mvn spring-boot:run
```

The API starts on `http://localhost:8080` by default. Authentication and feature endpoints will be added in the next implementation steps.
