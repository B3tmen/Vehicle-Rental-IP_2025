package org.unibl.etf.carrentalbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.unibl.etf.carrentalbackend.model.dto.ElectricCarDTO;
import org.unibl.etf.carrentalbackend.service.interfaces.ElectricCarService;
import org.unibl.etf.carrentalbackend.util.CustomLogger;

import java.net.URI;
import java.util.List;

import static org.unibl.etf.carrentalbackend.util.Constants.EndpointUrls.API_CARS_URL;

@RestController
@RequestMapping(API_CARS_URL)
public class ElectricCarController {

    private final ElectricCarService electricCarService;

    @Autowired
    public ElectricCarController(ElectricCarService electricCarService) {
        this.electricCarService = electricCarService;
    }

    @GetMapping
    public List<ElectricCarDTO> getAll() {
        return electricCarService.getAll();
    }

    @GetMapping("/{id}")
    public ElectricCarDTO getById(@PathVariable int id) {
        return electricCarService.getById(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> insert(@Validated @RequestPart("vehicle") ElectricCarDTO carDTO,
                                                 @RequestPart(value = "image", required = false) MultipartFile file) {
        ElectricCarDTO inserted = electricCarService.insert(carDTO, file);
        return getInsertResponseEntity(inserted);
    }

    @PutMapping(path="/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(@Validated @RequestPart("vehicle") ElectricCarDTO carDTO,
                                               @RequestPart(value = "image", required = false) MultipartFile file) {
        ElectricCarDTO updated = electricCarService.update(carDTO, file);
        if(updated != null){
            CustomLogger.getInstance().info("[Server]: Updated vehicle with ID: " + updated.getId());
            return ResponseEntity.ok(updated);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> insert(@Validated @RequestBody ElectricCarDTO electricCarDTO) {
        ElectricCarDTO inserted = electricCarService.insert(electricCarDTO);
        return getInsertResponseEntity(inserted);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Validated @RequestBody ElectricCarDTO electricCarDTO) {
        ElectricCarDTO updated = electricCarService.update(electricCarDTO);
        if(updated != null) {
            return ResponseEntity.ok(updated);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    private ResponseEntity<?> getInsertResponseEntity(ElectricCarDTO inserted) {
        if (inserted != null) {
            CustomLogger.getInstance().info("[Server]: Inserted vehicle with ID: " + inserted.getId());
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(inserted.getId()).toUri();

            return ResponseEntity.created(location).body(inserted);
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }

}

