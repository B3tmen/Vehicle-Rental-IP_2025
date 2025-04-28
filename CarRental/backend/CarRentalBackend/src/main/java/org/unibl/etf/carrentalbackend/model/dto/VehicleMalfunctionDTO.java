package org.unibl.etf.carrentalbackend.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.carrentalbackend.model.entities.VehicleMalfunctionId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleMalfunctionDTO {
    @JsonIgnore
    private VehicleMalfunctionId vehicleMalfunctionId;
    private RentalVehicleDTO vehicle;
    private MalfunctionDTO malfunction;
}
