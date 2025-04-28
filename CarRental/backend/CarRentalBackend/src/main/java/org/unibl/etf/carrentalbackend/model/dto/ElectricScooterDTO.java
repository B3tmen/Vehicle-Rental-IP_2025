package org.unibl.etf.carrentalbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ElectricScooterDTO extends RentalVehicleDTO{
    private String scooterId;
    private Integer maxSpeed;
}
