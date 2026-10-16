package com.hotel.reservation.service;

import com.hotel.reservation.dto.QuoteDTO;
import com.hotel.reservation.model.RoomType;
import com.hotel.reservation.model.Season;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.EnumSet;
import java.util.Set;

/** Subsystem 2: dynamic pricing by season, number of nights and room type. */
@Service
public class RateService {

    private static final Set<Month> HIGH_SEASON_MONTHS =
            EnumSet.of(Month.DECEMBER, Month.JANUARY, Month.JUNE, Month.JULY);

    public Season getSeason(LocalDate date) {
        return HIGH_SEASON_MONTHS.contains(date.getMonth()) ? Season.HIGH : Season.LOW;
    }

    public BigDecimal getNightlyRate(RoomType type, LocalDate night) {
        return type.getBasePrice().multiply(getSeason(night).getMultiplier()).setScale(2, RoundingMode.HALF_UP);
    }

    /** Each night is priced with the season of its own date, so stays that span two seasons are priced fairly. */
    public QuoteDTO calculateQuote(RoomType type, LocalDate checkIn, LocalDate checkOut) {
        long highNights = 0;
        long lowNights = 0;
        BigDecimal total = BigDecimal.ZERO;
        for (LocalDate night = checkIn; night.isBefore(checkOut); night = night.plusDays(1)) {
            if (getSeason(night) == Season.HIGH) {
                highNights++;
            } else {
                lowNights++;
            }
            total = total.add(getNightlyRate(type, night));
        }
        BigDecimal highPrice = type.getBasePrice().multiply(Season.HIGH.getMultiplier()).setScale(2, RoundingMode.HALF_UP);
        return new QuoteDTO(type, type.getLabel(), checkIn, checkOut, highNights + lowNights, highNights, lowNights,
                type.getBasePrice(), highPrice, total.setScale(2, RoundingMode.HALF_UP));
    }
}
