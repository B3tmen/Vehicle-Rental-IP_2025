package org.unibl.etf.clientapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.unibl.etf.clientapp.model.enums.VehicleType;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class RentalVehicle {
    private Integer id;
    private String model;
    private BigDecimal rentalPrice;
    private Image image;
    private Manufacturer manufacturer;
    private RentalStatus rentalStatus;
    private VehicleType type;

    public abstract String getRuntimeVehicleType();
}
