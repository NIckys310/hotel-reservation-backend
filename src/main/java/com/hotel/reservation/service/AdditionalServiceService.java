package com.hotel.reservation.service;

import com.hotel.reservation.dto.ServiceCatalogItemDTO;
import com.hotel.reservation.exception.BusinessException;
import com.hotel.reservation.model.AddedService;
import com.hotel.reservation.model.Reservation;
import com.hotel.reservation.model.ServiceType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/** Subsystem 3: extra services with additional cost (spa $50, breakfast $15/day, transfer $30). */
@Service
public class AdditionalServiceService {

    public List<ServiceCatalogItemDTO> getCatalog() {
        return Arrays.stream(ServiceType.values())
                .map(type -> new ServiceCatalogItemDTO(type, type.getLabel(), type.getPrice(), type.isChargedPerDay()))
                .toList();
    }

    public AddedService addService(Reservation reservation, ServiceType type, Integer requestedQuantity) {
        int quantity = resolveQuantity(reservation, type, requestedQuantity);
        BigDecimal total = type.getPrice().multiply(BigDecimal.valueOf(quantity));
        AddedService service = new AddedService(type, type.getLabel(), quantity, type.getPrice(), total, LocalDateTime.now());
        reservation.getServices().add(service);
        return service;
    }

    private int resolveQuantity(Reservation reservation, ServiceType type, Integer requestedQuantity) {
        if (requestedQuantity == null) {
            return type.isChargedPerDay() ? (int) reservation.getNights() : 1;
        }
        if (requestedQuantity < 1) {
            throw new BusinessException("La cantidad debe ser mayor a cero");
        }
        if (type.isChargedPerDay() && requestedQuantity > reservation.getNights()) {
            throw new BusinessException("El desayuno no puede superar el número de noches de la estadía ("
                    + reservation.getNights() + ")");
        }
        return requestedQuantity;
    }
}
