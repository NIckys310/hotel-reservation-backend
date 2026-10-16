# Hotel Reservation System – Backend

**Producción:** Frontend https://hotel-reservation-frontend-nine.vercel.app · Backend https://hotel-reservation-backend-production-a9c8.up.railway.app

Spring Boot 3 (Java 21) REST API for the hotel reservation workshop. It applies the **Facade** design pattern:
`HotelFacade` is the single entry point that coordinates five subsystems. State is kept in memory with Java collections.

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

- 16 predefined rooms: 6 single ($100), 6 double ($150), 4 suites ($300) base price per night.
- High season (December, January, June, July) = base price × 1.5; low season = base price. Each night is priced with its own season.
- Extra services: spa $50, breakfast $15 per day, transfer $30.
- Check-in issues a digital key (UUID); check-out revokes it, releases the room and generates the invoice.

## REST endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/hotel/reservar` | Create a reservation |
| GET | `/api/hotel/disponibilidad?checkIn=YYYY-MM-DD&checkOut=YYYY-MM-DD&type=SINGLE\|DOUBLE\|SUITE` | Availability with price per room |
| POST | `/api/hotel/servicios/{reservationId}` | Add an extra service `{ "serviceType": "SPA", "quantity": 1 }` |
| PUT | `/api/hotel/checkin/{reservationId}` | Check-in (generates digital key) |
| PUT | `/api/hotel/checkout/{reservationId}` | Check-out (final invoice) |
| GET | `/api/hotel/reserva/{reservationId}` | Reservation detail |
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
Optional: set `CORS_ALLOWED_ORIGINS` to the Vercel URL (defaults to `*`).
