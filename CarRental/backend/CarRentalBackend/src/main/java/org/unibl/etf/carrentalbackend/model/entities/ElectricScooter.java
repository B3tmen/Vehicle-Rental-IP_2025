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
@Table(name = "electric_scooter")
@PrimaryKeyJoinColumn(name = "fk_vehicle_id")
public class ElectricScooter extends RentalVehicle{

    @Column(name = "scooter_id", nullable = false, length = 50)
    private String scooterId;

    @Column(name = "max_speed", nullable = false)
    private Integer maxSpeed;

}