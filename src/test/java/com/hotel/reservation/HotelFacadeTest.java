package com.hotel.reservation;

import com.hotel.reservation.dto.ReservationRequestDTO;
import com.hotel.reservation.exception.BusinessException;
import com.hotel.reservation.facade.HotelFacade;
import com.hotel.reservation.model.Reservation;
import com.hotel.reservation.model.ReservationStatus;
import com.hotel.reservation.model.ServiceType;
import com.hotel.reservation.service.RateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class HotelFacadeTest {

    @Autowired
    private HotelFacade hotelFacade;

    @Autowired
    private RateService rateService;

    private static LocalDate nextLowSeasonDate() {
        LocalDate date = LocalDate.now().plusDays(1);
        while (date.getMonthValue() != 3) {
            date = date.plusDays(1);
        }
        return date.withDayOfMonth(10);
    }

    @Test
    void hotelHasAtLeastFifteenRooms() {
        assertTrue(hotelFacade.getAllRooms().size() >= 15);
    }

    @Test
    void highSeasonCostsOneAndAHalfTimesBasePrice() {
        LocalDate december = LocalDate.of(LocalDate.now().getYear() + 1, 12, 1);
        var quote = rateService.calculateQuote(com.hotel.reservation.model.RoomType.DOUBLE, december, december.plusDays(2));
        assertEquals(new BigDecimal("450.00"), quote.total());
    }

    @Test
    void fullReservationFlowGeneratesInvoice() {
        LocalDate checkIn = nextLowSeasonDate();
        Reservation reservation = hotelFacade.createReservation(new ReservationRequestDTO(
                "Ana Pérez", "123", "ana@example.com", "300", "101", checkIn, checkIn.plusDays(3), 1));
        assertEquals(new BigDecimal("300.00"), reservation.getLodgingCost());

        hotelFacade.addService(reservation.getId(), ServiceType.SPA, null);
        hotelFacade.addService(reservation.getId(), ServiceType.BREAKFAST, null);
        hotelFacade.addService(reservation.getId(), ServiceType.TRANSFER, null);

        Reservation checkedIn = hotelFacade.checkIn(reservation.getId());
        assertNotNull(checkedIn.getDigitalKey());

        Reservation checkedOut = hotelFacade.checkOut(reservation.getId());
        assertEquals(ReservationStatus.CHECKED_OUT, checkedOut.getStatus());
        // 300 lodging + 50 spa + 45 breakfast (3 days) + 30 transfer
        assertEquals(new BigDecimal("425.00"), checkedOut.getInvoice().getTotal());
    }

    @Test
    void overlappingReservationIsRejected() {
        LocalDate checkIn = nextLowSeasonDate().plusDays(5);
        hotelFacade.createReservation(new ReservationRequestDTO(
                "Luis Gómez", "456", "luis@example.com", null, "402", checkIn, checkIn.plusDays(2), 2));
        assertThrows(BusinessException.class, () -> hotelFacade.createReservation(new ReservationRequestDTO(
                "Otra Persona", "789", "otra@example.com", null, "402", checkIn.plusDays(1), checkIn.plusDays(3), 2)));
    }
}
