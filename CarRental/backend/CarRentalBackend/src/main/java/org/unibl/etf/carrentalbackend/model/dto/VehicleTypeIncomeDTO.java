package org.unibl.etf.carrentalbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleTypeIncomeDTO {
    private String vehicleType;
    private BigDecimal income;
}
