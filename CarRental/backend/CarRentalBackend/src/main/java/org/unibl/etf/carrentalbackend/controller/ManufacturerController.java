package org.unibl.etf.carrentalbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.unibl.etf.carrentalbackend.model.dto.ManufacturerDTO;
import org.unibl.etf.carrentalbackend.service.impl.ManufacturerServiceImpl;
import org.unibl.etf.carrentalbackend.util.CustomLogger;

import java.net.URI;
import java.util.List;

import static org.unibl.etf.carrentalbackend.util.Constants.EndpointUrls.API_MANUFACTURERS_URL;

@RestController
@RequestMapping(API_MANUFACTURERS_URL)
@PreAuthorize("hasRole('Administrator')")
public class ManufacturerController {

    private final ManufacturerServiceImpl manufacturerService;

    @Autowired
    public ManufacturerController(ManufacturerServiceImpl manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<ManufacturerDTO> manufacturers = manufacturerService.getAll();
        CustomLogger.getInstance().info("Sent all manufacturers");

        return ResponseEntity.ok(manufacturers);
    }

    @GetMapping("/quantity")
    public int getQuantity() {
        List<ManufacturerDTO> manufacturers = manufacturerService.getAll();
        return manufacturers.size();
    }

    @PostMapping
    public ResponseEntity<?> insert(@Validated @RequestBody ManufacturerDTO manufacturerDTO) {
        ManufacturerDTO insertedManufacturer = manufacturerService.insert(manufacturerDTO);
        if(insertedManufacturer != null) {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(insertedManufacturer.getId()).toUri();

            return ResponseEntity.created(location).body(insertedManufacturer);
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Validated @RequestBody ManufacturerDTO manufacturerDTO) {
        ManufacturerDTO updatedManufacturer = manufacturerService.update(manufacturerDTO);

        return ResponseEntity.ok(updatedManufacturer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        boolean deleted = manufacturerService.delete(id);

        if(deleted){
            return ResponseEntity.noContent().build();
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
}
