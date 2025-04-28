package org.unibl.etf.carrentalbackend.service.interfaces;

import org.unibl.etf.carrentalbackend.model.dto.ManufacturerDTO;
import org.unibl.etf.carrentalbackend.model.entities.Manufacturer;

public interface ManufacturerService extends CrudBaseService<Manufacturer, ManufacturerDTO> {
    ManufacturerDTO getByPhoneNumber(String phoneNumber);
    ManufacturerDTO getByEmail(String email);
}
