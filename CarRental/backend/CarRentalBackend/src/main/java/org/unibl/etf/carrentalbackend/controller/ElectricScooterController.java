package org.unibl.etf.carrentalbackend.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.unibl.etf.carrentalbackend.model.dto.ElectricScooterDTO;
import org.unibl.etf.carrentalbackend.service.interfaces.ElectricScooterService;

import java.net.URI;

import static org.unibl.etf.carrentalbackend.util.Constants.EndpointUrls.API_SCOOTERS_URL;

@RestController
@RequestMapping(API_SCOOTERS_URL)
public class ElectricScooterController {

    private final ElectricScooterService electricScooterService;

    public ElectricScooterController(ElectricScooterService electricScooterService) {
        this.electricScooterService = electricScooterService;
    }

    @GetMapping("/{id}")
    public ElectricScooterDTO getById(@PathVariable int id) {
        return electricScooterService.getById(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> insert(@Validated @RequestPart("vehicle") ElectricScooterDTO scooterDTO,
                                                     @RequestPart(value = "image", required = false) MultipartFile file) {

        ElectricScooterDTO inserted = electricScooterService.insert(scooterDTO, file);
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
    public ResponseEntity<?> update(@Validated @RequestPart("vehicle") ElectricScooterDTO scooterDTO,
                                                     @RequestPart(value = "image", required = false) MultipartFile file) {
        ElectricScooterDTO updated = electricScooterService.update(scooterDTO, file);
        if(updated != null){
            return ResponseEntity.ok(updated);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> insert(@Validated @RequestBody ElectricScooterDTO electricScooterDTO){
        ElectricScooterDTO inserted = electricScooterService.insert(electricScooterDTO);
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
    public ResponseEntity<?> update(@Validated @RequestBody ElectricScooterDTO electricScooterDTO) {
        ElectricScooterDTO updated = electricScooterService.update(electricScooterDTO);
        if(updated != null) {
            return ResponseEntity.ok(updated);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
}
