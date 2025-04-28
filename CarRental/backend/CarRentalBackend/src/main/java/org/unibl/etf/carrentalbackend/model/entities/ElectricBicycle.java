package org.unibl.etf.carrentalbackend.model.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@ToString(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "electric_bicycle")
@PrimaryKeyJoinColumn(name = "fk_vehicle_id")
public class ElectricBicycle extends RentalVehicle {

    @Column(name = "bicycle_id", nullable = false, length = 50)
    private String bicycleId;

    @Column(name = "riding_autonomy", nullable = false)
    private Integer ridingAutonomy;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_vehicle_id", nullable = false)
    private RentalVehicle rentalVehicle;

}