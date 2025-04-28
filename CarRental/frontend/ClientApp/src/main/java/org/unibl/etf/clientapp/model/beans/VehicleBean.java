package org.unibl.etf.clientapp.model.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.clientapp.model.dto.RentalVehicle;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleBean implements Serializable {
    private RentalVehicle vehicle;
}
