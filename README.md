# Customer Management App

Spring Boot + Angular app for managing customer records.

## Stack

- Java 17, Spring Boot 3.
- H2 in-memory DB, Liquibase
- Caffeine cache
- Angular 16
- JUnit 5, Mockito, AssertJ

## To run locally

**Backend**

```bash
cd backend
./mvnw spring-boot:run
```

Windows:

```cmd
mvnw.cmd spring-boot:run
```

Runs on `http://localhost:8080`

**Frontend**

```bash
cd frontend
npm install
npm start
```

Runs on `http://localhost:4200`

Start the backend first.

---

## API

`POST /api/v1/customers`

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "dateOfBirth": "1990-05-15"
}
```

Returns 201 with the saved record including id, createdAt, updatedAt.

`GET /api/v1/customers` — returns all customers

`GET /api/v1/customers/{id}` — returns 404 if not found

Validation errors come back as a map of field → message. Server errors return `{ status, message, timestamp }`.

---

## Useful links

| Frontend | http://localhost:4200 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| H2 Console | http://localhost:8080/h2-console |

H2 connection: `jdbc:h2:mem:customerdb` / username `sa` / no password

## Notes

Went with separate request/response DTOs rather than exposing the entity directly — makes it easier to change the DB schema without breaking the API contract.

DTOs are records since they don't need to be mutable.

Swagger annotations are in a separate interface `CustomerControllerSwagger` so the controller stays readable.

Cache is set to 5 min TTL, evicted on any write.

## Tests

```bash
# backend
./mvnw test

# frontend
npm test
```

Controller, service, and mapper each have their own test class. Service and mapper tests don't load a Spring context.
