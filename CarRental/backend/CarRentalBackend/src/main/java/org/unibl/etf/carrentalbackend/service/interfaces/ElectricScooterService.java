package org.unibl.etf.carrentalbackend.service.interfaces;

import org.unibl.etf.carrentalbackend.model.dto.ElectricScooterDTO;
import org.unibl.etf.carrentalbackend.model.entities.ElectricScooter;


public interface ElectricScooterService extends CrudBaseService<ElectricScooter, ElectricScooterDTO>,  DtoImageInserterUpdater<ElectricScooterDTO> {
    boolean existsByScooterId(String scooterId);
}
