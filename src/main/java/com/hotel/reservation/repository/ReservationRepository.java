package com.hotel.reservation.repository;

import com.hotel.reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, String> {

    List<Reservation> findAllByOrderByCreatedAtDesc();

    Optional<Reservation> findByDigitalKey(String digitalKey);

    long countByInvoiceIsNotNull();

    @Query("""
            select count(r) > 0 from Reservation r
            where r.room.number = :roomNumber
              and r.status <> com.hotel.reservation.model.ReservationStatus.CHECKED_OUT
              and r.checkInDate < :checkOut and :checkIn < r.checkOutDate
            """)
    boolean existsOverlapping(@Param("roomNumber") String roomNumber, @Param("checkIn") LocalDate checkIn,
                              @Param("checkOut") LocalDate checkOut);
}
