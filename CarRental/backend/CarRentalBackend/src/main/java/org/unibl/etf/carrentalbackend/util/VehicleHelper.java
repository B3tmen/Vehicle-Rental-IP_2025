package org.unibl.etf.carrentalbackend.util;

import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.carrentalbackend.model.dto.ImageDTO;
import org.unibl.etf.carrentalbackend.model.dto.RentalVehicleDTO;
import org.unibl.etf.carrentalbackend.service.interfaces.ImageService;

import java.io.IOException;

public class VehicleHelper {

    public static void addImage(RentalVehicleDTO dto, ImageService imageService, String relativePath, MultipartFile imageFile){
        ImageDTO imageDTO;
        try {
            imageDTO = imageService.uploadImage(relativePath, imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dto.setImage(imageDTO);
    }

    public static void updateOldImage(RentalVehicleDTO dto, ImageService imageService, String relativePath, MultipartFile imageFile){
        // We can't update the image, if there is no image
        if(dto.getImage() != null){
            if(dto.getImage().getId() == null){     // If no ID is present of the image, insert it as a new image
                addImage(dto, imageService, relativePath, imageFile);
            }
            else {      // If ID is present of the image, update the old image
                ImageDTO oldImageDTO = imageService.getById(dto.getImage().getId());
                if(oldImageDTO != null){
                    ImageDTO updatedImage;
                    try {
                        updatedImage = imageService.updateImage(oldImageDTO, relativePath, imageFile);
                        dto.setImage(updatedImage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        else{       // if image didn't exist in the first place, insert it
            addImage(dto, imageService, relativePath, imageFile);
        }
    }

    public static void setImageUrlForVehicle(RentalVehicleDTO dto, ImageService imageService, String relativePath) {
        if(dto.getImage() != null){
            try {
                ImageDTO imgDto = imageService.downloadImage(relativePath, dto.getImage().getId());
                dto.getImage().setUrl(imgDto.getUrl());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
