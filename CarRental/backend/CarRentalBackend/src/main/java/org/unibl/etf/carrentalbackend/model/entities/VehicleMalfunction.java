package org.unibl.etf.carrentalbackend.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vehicle_malfunction")
public class VehicleMalfunction {
    @EmbeddedId
    private VehicleMalfunctionId id;

    @MapsId("fkVehicleId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_vehicle_id", nullable = false)
    private RentalVehicle vehicle;

    @MapsId("fkMalfunctionId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_malfunction_id", nullable = false)
    private Malfunction malfunction;

}