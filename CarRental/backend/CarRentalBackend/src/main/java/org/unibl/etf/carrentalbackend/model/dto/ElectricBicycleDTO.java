package org.unibl.etf.carrentalbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElectricBicycleDTO extends RentalVehicleDTO {
    private String bicycleId;
    private Integer ridingAutonomy;
}
