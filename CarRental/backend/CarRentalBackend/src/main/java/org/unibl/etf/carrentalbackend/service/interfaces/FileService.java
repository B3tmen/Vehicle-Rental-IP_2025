package org.unibl.etf.carrentalbackend.service.interfaces;

import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.carrentalbackend.model.entities.RentalVehicle;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface FileService<T> {
    List<T> save(MultipartFile file);
}
