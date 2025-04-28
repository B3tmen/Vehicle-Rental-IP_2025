package org.unibl.etf.carrentalbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.carrentalbackend.model.dto.ImageDTO;
import org.unibl.etf.carrentalbackend.service.interfaces.ImageService;

import java.io.IOException;

import static org.unibl.etf.carrentalbackend.util.Constants.EndpointUrls.API_IMAGES_URL;

@RestController
@RequestMapping(API_IMAGES_URL)
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> downloadImage(@RequestParam String relativePath, @PathVariable Integer id) {
        ImageDTO image = null;
        try {
            image = imageService.downloadImage(relativePath, id);

            return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getName() + "\"")
                    .body(image.getData());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
