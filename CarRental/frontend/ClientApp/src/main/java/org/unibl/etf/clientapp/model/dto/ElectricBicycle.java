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
public class ElectricBicycle extends RentalVehicle {
    private String bicycleId;
    private Integer ridingAutonomy;

    @Override
    public String getRuntimeVehicleType() {
        return "ElectricBicycle";
    }
}
