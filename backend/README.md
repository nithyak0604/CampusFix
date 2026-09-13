# CampusFix Backend

This project uses Java 21, Spring Boot, Spring Data JPA, Spring Security, Bean Validation, and MySQL.

## Prerequisites


## Stack

- Java 21
- Spring Boot 3.5

## Requirements

- JDK 21+
- Maven 3.9+
- MySQL 8+

## Configuration

Create the database:

```sql
CREATE DATABASE campusfix_db;
```

Configure these environment variables. Do not commit real values:

```powershell
$env:DB_URL = "jdbc:mysql://localhost:3306/campusfix_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
$env:DB_USERNAME = "your_mysql_username"
$env:DB_PASSWORD = "your_mysql_password"
$env:JWT_SECRET = "a-random-secret-with-at-least-32-characters"
```

Optional values include `SERVER_PORT`, `CORS_ALLOWED_ORIGINS`, `APP_UPLOAD_DIR`, `APP_UPLOAD_MAX_FILE_SIZE`, and `JPA_DDL_AUTO`. See `.env.example`.

## Run

```powershell
mvn spring-boot:run
```

The API runs at `http://localhost:8080`. OpenAPI JSON is available at `/v3/api-docs` and Swagger UI at `/swagger-ui/index.html`.

## Test

```powershell
mvn test
```

The current workspace does not include Maven on `PATH`, so Maven tests/builds must be run after installing Maven or adding the Maven wrapper.

## Authentication

Register with `POST /api/auth/register` using `name`, `email`, `password`, `role` (`STUDENT` or `FACULTY`), and optional `phone`. Login with `POST /api/auth/login`. Send the returned token as:

```text
Authorization: Bearer <token>
```

Public registration cannot create privileged roles. Seed or administer `ADMIN`, `MAINTENANCE`, and `VERIFIER` users through a controlled database/bootstrap process.

## API overview

| Method | Endpoint | Access |
| --- | --- | --- |
| POST | `/api/auth/register` | Public |
| POST | `/api/auth/login` | Public |
| GET/PUT | `/api/users/{id}` | Owner or ADMIN |
| POST | `/api/issues` | STUDENT, FACULTY |
| GET | `/api/issues`, `/api/issues/{id}` | Authenticated; reporters see their own issues |
| PUT | `/api/issues/{id}/status` | Authenticated; transition rules apply |
| PUT | `/api/issues/{id}/assign` | ADMIN |
| GET | `/api/maintenance/issues` | MAINTENANCE |
| PUT | `/api/issues/{id}/resolve` | Assigned MAINTENANCE |
| GET | `/api/verification/pending` | VERIFIER |
| PUT | `/api/issues/{id}/verify` | VERIFIER |
| PUT | `/api/issues/{id}/confirm` | Reporting user |
| PUT | `/api/issues/{id}/reopen` | Reporting user; reason required |
| POST | `/api/issues/{id}/feedback` | Reporting user after closure |
| POST | `/api/issues/{id}/images` | Owner, assigned MAINTENANCE, or VERIFIER |
| GET | `/api/notifications` | Authenticated |

## Workflow

`REPORTED -> UNDER_REVIEW -> ASSIGNED -> IN_PROGRESS -> RESOLVED -> VERIFICATION_PENDING -> VERIFIED -> USER_CONFIRMATION_PENDING -> CLOSED`

Verification rejection follows `VERIFICATION_PENDING -> REJECTED -> IN_PROGRESS`. Reporter rejection follows `USER_CONFIRMATION_PENDING -> REOPENED -> IN_PROGRESS`. Every transition is validated centrally and recorded in `issue_status_history`.

## Roles

- `STUDENT`, `FACULTY`: create and track owned issues, upload original evidence, confirm/reopen, and submit feedback.
- `ADMIN`: review issues, assign maintenance staff, and access all issues.
- `MAINTENANCE`: view active assignments, resolve assigned issues, and upload after-repair evidence.
- `VERIFIER`: inspect pending issues and approve or reject them independently.

## Persistence and uploads

JPA creates/updates these tables with local `ddl-auto=update`: `users`, `locations`, `issues`, `issue_images`, `assignments`, `issue_status_history`, `verification_records`, `notifications`, and `feedback`.

Images are stored locally under `APP_UPLOAD_DIR` through the `FileStorageService` abstraction. Only JPEG, PNG, and WebP files up to 5 MB are accepted. The abstraction can later be implemented with S3 without changing issue workflow code.

## Structure

```text
src/main/java/com/campusfix/
	config/       security and CORS
	controller/   REST endpoints
	dto/          validated request/response records
	entity/       JPA entities and enums
	exception/    API error handling
	repository/   Spring Data repositories
	security/     JWT authentication
	service/      business workflows
	storage/      local storage abstraction
```

Frontend and dashboard clients should use DTO field names exactly as returned, send JWTs with every protected request, and handle `400`, `401`, `403`, and `404` responses using the error response fields `timestamp`, `status`, `message`, and `path`.
