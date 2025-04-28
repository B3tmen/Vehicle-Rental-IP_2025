package org.unibl.etf.carrentalbackend.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface DtoImageInserterUpdater<D> {
    D insert(D dto, MultipartFile imageFile);
    D update(D dto, MultipartFile imageFile);
}
