package org.unibl.etf.carrentalbackend.service.interfaces;


import org.unibl.etf.carrentalbackend.model.dto.MapVehicleDTO;
import org.unibl.etf.carrentalbackend.model.dto.RentalDTO;
import org.unibl.etf.carrentalbackend.model.dto.RentalVehicleDTO;

import java.util.List;

public interface RentalService {
    List<RentalDTO> getAll();
    List<MapVehicleDTO> getVehiclesOnMap();
    List<RentalDTO> getRentalsByVehicle(int vehicleId);
}
