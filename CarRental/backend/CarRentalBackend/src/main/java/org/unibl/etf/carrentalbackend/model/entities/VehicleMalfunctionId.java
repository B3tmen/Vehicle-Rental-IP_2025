package org.unibl.etf.carrentalbackend.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class VehicleMalfunctionId implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -4290224813696571146L;
    @Column(name = "fk_vehicle_id", nullable = false)
    private Integer fkVehicleId;

    @Column(name = "fk_malfunction_id", nullable = false)
    private Integer fkMalfunctionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        VehicleMalfunctionId entity = (VehicleMalfunctionId) o;
        return Objects.equals(this.fkVehicleId, entity.fkVehicleId) &&
                Objects.equals(this.fkMalfunctionId, entity.fkMalfunctionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fkVehicleId, fkMalfunctionId);
    }

}