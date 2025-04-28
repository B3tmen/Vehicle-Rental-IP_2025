package org.unibl.etf.carrentalbackend.service.interfaces;

import org.unibl.etf.carrentalbackend.model.dto.ElectricBicycleDTO;
import org.unibl.etf.carrentalbackend.model.entities.ElectricBicycle;

public interface ElectricBicycleService extends CrudBaseService<ElectricBicycle, ElectricBicycleDTO>,  DtoImageInserterUpdater<ElectricBicycleDTO> {
    boolean existsByBicycleId(String bicycleId);

}
