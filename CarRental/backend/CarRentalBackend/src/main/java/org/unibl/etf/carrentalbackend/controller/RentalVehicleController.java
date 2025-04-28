package org.unibl.etf.carrentalbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.carrentalbackend.model.dto.RentalVehicleDTO;
import org.unibl.etf.carrentalbackend.model.entities.RentalVehicle;
import org.unibl.etf.carrentalbackend.service.interfaces.RentalVehicleService;
import org.unibl.etf.carrentalbackend.service.interfaces.VehicleCsvService;
import org.unibl.etf.carrentalbackend.util.CustomLogger;

import java.util.List;

import static org.unibl.etf.carrentalbackend.util.Constants.EndpointUrls.API_VEHICLES_URL;

@RestController
@RequestMapping(API_VEHICLES_URL)
public class RentalVehicleController {
    private static CustomLogger logger = CustomLogger.getInstance();
    private final RentalVehicleService rentalVehicleService;
    private final VehicleCsvService csvService;

    @Autowired
    public RentalVehicleController(RentalVehicleService rentalVehicleService, VehicleCsvService csvService) {
        this.rentalVehicleService = rentalVehicleService;
        this.csvService = csvService;
    }

    @GetMapping
    public ResponseEntity<List<RentalVehicleDTO>> getAll() {
        List<RentalVehicleDTO> vehicles = rentalVehicleService.getAll();
        logger.info("Sent all vehicles");

        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/{id}")
    public RentalVehicleDTO getById(@PathVariable int id) {
        return rentalVehicleService.getById(id);
    }

    @GetMapping("/quantity")
    public int getQuantity() {
        List<RentalVehicleDTO> vehicles = rentalVehicleService.getAll();
        return vehicles.size();
    }



    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<?> upload(@RequestPart("csv") MultipartFile csvFile) {
        List<RentalVehicle> vehicles = csvService.save(csvFile);
        if(!vehicles.isEmpty()){
            return ResponseEntity.ok(vehicles);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to save file data. Check data validity.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Validated @RequestBody RentalVehicleDTO rentalVehicleDTO) {
        RentalVehicleDTO updated = rentalVehicleService.update(rentalVehicleDTO);
        if(updated != null){
            return ResponseEntity.ok(updated);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        boolean success = rentalVehicleService.delete(id);
        if(success){
            return ResponseEntity.noContent().build();
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

}
