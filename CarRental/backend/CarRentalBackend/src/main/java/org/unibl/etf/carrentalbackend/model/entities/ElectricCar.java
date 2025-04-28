package org.unibl.etf.carrentalbackend.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.unibl.etf.carrentalbackend.model.enums.VehicleType;
import org.unibl.etf.carrentalbackend.util.Constants;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ToString(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "electric_car")
@PrimaryKeyJoinColumn(name = "fk_vehicle_id")
public class ElectricCar extends RentalVehicle {

    @Column(name = "car_id", nullable = false, length = 50)
    private String carId;

    @Column(name = "purchase_date", nullable = false)
    private LocalDateTime purchaseDate;

    @Column(name = "description", nullable = false, length = 45)
    private String description;

}