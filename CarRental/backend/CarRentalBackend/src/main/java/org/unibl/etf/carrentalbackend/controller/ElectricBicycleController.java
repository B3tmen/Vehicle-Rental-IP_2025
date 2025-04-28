package org.unibl.etf.carrentalbackend.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.unibl.etf.carrentalbackend.model.dto.ElectricBicycleDTO;
import org.unibl.etf.carrentalbackend.service.interfaces.ElectricBicycleService;

import java.net.URI;

import static org.unibl.etf.carrentalbackend.util.Constants.EndpointUrls.API_BICYCLES_URL;

@RestController
@RequestMapping(API_BICYCLES_URL)
public class ElectricBicycleController {

    private final ElectricBicycleService electricBicycleService;

    public ElectricBicycleController(ElectricBicycleService electricBicycleService) {
        this.electricBicycleService = electricBicycleService;
    }

    @GetMapping("/{id}")
    public ElectricBicycleDTO getById(@PathVariable int id) {
        return electricBicycleService.getById(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> insert(@Validated @RequestPart("vehicle") ElectricBicycleDTO bicycleDTO,
                                                     @RequestPart(value = "image", required = false) MultipartFile file) {
        ElectricBicycleDTO inserted = electricBicycleService.insert(bicycleDTO, file);
        if(inserted != null) {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(inserted.getId()).toUri();

            return ResponseEntity.created(location).body(inserted);
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }


    @PutMapping(path="/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(@Validated @RequestPart("vehicle") ElectricBicycleDTO bicycleDTO,
                                                     @RequestPart(value = "image", required = false) MultipartFile file) {
        ElectricBicycleDTO updated = electricBicycleService.update(bicycleDTO, file);
        if(updated != null){
            return ResponseEntity.ok(updated);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> insert(@Validated @RequestBody ElectricBicycleDTO electricBicycleDTO){
        ElectricBicycleDTO inserted = electricBicycleService.insert(electricBicycleDTO);
        if(inserted != null){
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(inserted.getId()).toUri();

            return ResponseEntity.created(location).body(inserted);
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Validated @RequestBody ElectricBicycleDTO electricBicycleDTO) {
        ElectricBicycleDTO updated = electricBicycleService.update(electricBicycleDTO);
        if(updated != null) {
            return ResponseEntity.ok(updated);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
}
