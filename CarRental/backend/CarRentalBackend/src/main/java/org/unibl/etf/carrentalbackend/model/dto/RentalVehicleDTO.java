package org.unibl.etf.carrentalbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.carrentalbackend.model.enums.VehicleType;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalVehicleDTO {
    private Integer id;
    private String model;
    private BigDecimal price;
    private BigDecimal rentalPrice;
    private ImageDTO image;
    private ManufacturerDTO manufacturer;
    private PromotionDTO promotion;
    private AnnouncementDTO announcement;
    private RentalStatusDTO rentalStatus;
    private VehicleType type_;
    private Boolean isActive;
}
