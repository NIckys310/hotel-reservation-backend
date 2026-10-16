# Hotel Reservation System – Backend

**Producción:** Frontend https://hotel-reservation-frontend-nine.vercel.app · Backend https://hotel-reservation-backend-production-a9c8.up.railway.app

Spring Boot 3 (Java 21) REST API for the hotel reservation workshop. It applies the **Facade** design pattern:
`HotelFacade` is the single entry point that coordinates five subsystems. Data is stored in a relational database with
Spring Data JPA: PostgreSQL in production (Railway) and in-memory H2 for local development and tests.

## Architecture

```
HotelController  ──►  HotelFacade  ──►  RoomService               (HabitacionService)
                                   ├─►  RateService               (TarifaService)
                                   ├─►  AdditionalServiceService  (ServicioAdicionalService)
                                   ├─►  BillingService            (FacturacionService)
                                   └─►  AccessService             (AccesoService)
```

| Facade method (workshop name)        | Code                                   |
|--------------------------------------|----------------------------------------|
| `crearReserva(reservaDTO)`           | `createReservation(ReservationRequestDTO)` |
| `agregarServicio(reservaId, tipo)`   | `addService(reservationId, serviceType, quantity)` |
| `realizarCheckIn(reservaId)`         | `checkIn(reservationId)`               |
| `realizarCheckOut(reservaId)`        | `checkOut(reservationId)` – charges extras and builds the final invoice |

Business rules:

- 16 predefined rooms seeded on first start: 6 single ($100), 6 double ($150), 4 suites ($300) base price per night,
  each with name, size, bed type, view, amenities and photo.
- High season (December, January, June, July) = base price × 1.5; low season = base price. Each night is priced with its own season.
- Extra services: spa $50, breakfast $15 per day, transfer $30.
- Check-in issues a digital key (UUID); check-out revokes it, releases the room and generates the invoice.

## REST endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/hotel/reservar` | Create a reservation |
| GET | `/api/hotel/disponibilidad?checkIn=YYYY-MM-DD&checkOut=YYYY-MM-DD&type=SINGLE\|DOUBLE\|SUITE&minPrice=&maxPrice=` | Availability with price per room (price filter on the average nightly price) |
| POST | `/api/hotel/servicios/{reservationId}` | Add an extra service `{ "serviceType": "SPA", "quantity": 1 }` |
| PUT | `/api/hotel/checkin/{reservationId}` | Check-in (generates digital key) |
| PUT | `/api/hotel/checkout/{reservationId}` | Check-out (final invoice) |
| GET | `/api/hotel/reserva/{reservationId}` | Reservation detail |
| POST | `/api/auth/login` | Login `{ "username": "admin", "password": "hotel123" }` |
| POST | `/api/auth/register` | Register `{ "fullName", "username", "password" }` |
| GET | `/api/hotel/tarifa`, `/api/hotel/servicios`, `/api/hotel/habitaciones`, `/api/hotel/reservas` | Supporting endpoints for the frontend |

Example body for `POST /api/hotel/reservar`:

```json
{
  "guestName": "Ana Pérez",
  "documentId": "1001234567",
  "email": "ana@example.com",
  "phone": "3001234567",
  "roomNumber": "203",
  "checkIn": "2026-12-20",
  "checkOut": "2026-12-23",
  "numberOfGuests": 2
}
```

## Run locally

```bash
mvn spring-boot:run      # http://localhost:8080
mvn test
```

## Deploy (Railway)

The repository includes a `Dockerfile`; Railway builds it automatically. The app listens on the `PORT` variable.
Add a PostgreSQL database in the Railway project and set on the backend service:

```
SPRING_DATASOURCE_URL=jdbc:postgresql://${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/${{Postgres.PGDATABASE}}
SPRING_DATASOURCE_USERNAME=${{Postgres.PGUSER}}
SPRING_DATASOURCE_PASSWORD=${{Postgres.PGPASSWORD}}
```

Tables are created automatically (`spring.jpa.hibernate.ddl-auto=update`).
Optional: set `CORS_ALLOWED_ORIGINS` to the Vercel URL (defaults to `*`).
