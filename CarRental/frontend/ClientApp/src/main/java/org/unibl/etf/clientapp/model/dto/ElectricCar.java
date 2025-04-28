package org.unibl.etf.clientapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.sql.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class ElectricCar extends RentalVehicle {
    private String carId;
    private Date purchaseDate;
    private String description;

    @Override
    public String getRuntimeVehicleType() {
        return "ElectricCar";
    }
}
