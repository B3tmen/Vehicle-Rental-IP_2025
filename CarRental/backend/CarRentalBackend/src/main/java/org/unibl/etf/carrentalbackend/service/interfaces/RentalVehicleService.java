package org.unibl.etf.carrentalbackend.service.interfaces;

import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.carrentalbackend.interfaces.EntityDTOConverter;
import org.unibl.etf.carrentalbackend.model.dto.MalfunctionDTO;
import org.unibl.etf.carrentalbackend.model.dto.MapVehicleDTO;
import org.unibl.etf.carrentalbackend.model.dto.RentalVehicleDTO;
import org.unibl.etf.carrentalbackend.model.entities.RentalVehicle;

import java.util.List;

public interface RentalVehicleService extends CrudBaseService<RentalVehicle, RentalVehicleDTO>, EntityDTOConverter<RentalVehicle, RentalVehicleDTO> {
    boolean uploadCSV(MultipartFile file);
    List<MalfunctionDTO> getAllMalfunctions(int vehicleId);
}
