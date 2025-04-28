package org.unibl.etf.carrentalbackend.service.interfaces;

import org.unibl.etf.carrentalbackend.model.dto.ElectricCarDTO;
import org.unibl.etf.carrentalbackend.model.entities.ElectricCar;

public interface ElectricCarService extends CrudBaseService<ElectricCar, ElectricCarDTO>, DtoImageInserterUpdater<ElectricCarDTO> {
    boolean existsByCarId(String carId);
}
