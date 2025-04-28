package org.unibl.etf.clientapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental {
    private Integer id;
    private RentalVehicle vehicle;
    private Client client;
    private LocalDateTime rentalDateTime;
    private Integer duration;
    private Location pickupLocation;
    private Location dropoffLocation;
    private Payment payment;
}
