package org.unibl.etf.clientapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class ElectricScooter extends RentalVehicle {
    private String scooterId;
    private Integer maxSpeed;

    @Override
    public String getRuntimeVehicleType() {
        return "ElectricScooter";
    }
}
