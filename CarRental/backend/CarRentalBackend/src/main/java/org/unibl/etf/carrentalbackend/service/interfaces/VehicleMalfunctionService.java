package org.unibl.etf.carrentalbackend.service.interfaces;

import org.unibl.etf.carrentalbackend.model.dto.MalfunctionDTO;
import org.unibl.etf.carrentalbackend.model.dto.VehicleMalfunctionDTO;
import org.unibl.etf.carrentalbackend.model.entities.VehicleMalfunction;

import java.util.List;

public interface VehicleMalfunctionService extends CrudBaseService<VehicleMalfunction, VehicleMalfunctionDTO> {
    List<MalfunctionDTO> getMalfunctionsByVehicle(int vehicleId);
}
