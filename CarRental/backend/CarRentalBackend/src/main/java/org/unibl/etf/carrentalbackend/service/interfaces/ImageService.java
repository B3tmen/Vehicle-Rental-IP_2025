package org.unibl.etf.carrentalbackend.service.interfaces;

import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.carrentalbackend.model.dto.ImageDTO;

import java.io.IOException;

public interface ImageService {
    ImageDTO getById(int id);
    ImageDTO updateImage(ImageDTO imageDTO, String relativePath, MultipartFile file) throws IOException;
    ImageDTO uploadImage(String relativePath, MultipartFile file) throws IOException;
    ImageDTO downloadImage(String relativePath, Integer id) throws IOException;
}
