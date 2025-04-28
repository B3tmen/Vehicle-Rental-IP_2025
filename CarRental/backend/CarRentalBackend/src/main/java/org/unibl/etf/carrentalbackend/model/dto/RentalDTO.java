package org.unibl.etf.carrentalbackend.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static org.unibl.etf.carrentalbackend.util.Constants.DATE_TIME_FORMAT;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalDTO {
    private int id;
    private RentalVehicleDTO vehicle;
    private ClientDTO client;
    @JsonFormat(pattern = DATE_TIME_FORMAT) // Match MySQL format
    private LocalDateTime rentalDateTime;
    private Integer duration;
    private LocationDTO pickupLocation;
    private LocationDTO dropoffLocation;
}
