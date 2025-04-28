package org.unibl.etf.carrentalbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.carrentalbackend.model.dto.MapVehicleDTO;
import org.unibl.etf.carrentalbackend.model.dto.RentalDTO;
import org.unibl.etf.carrentalbackend.service.interfaces.RentalService;
import org.unibl.etf.carrentalbackend.util.CustomLogger;

import java.util.List;

import static org.unibl.etf.carrentalbackend.util.Constants.EndpointUrls.API_RENTALS_URL;

@RequestMapping(API_RENTALS_URL)
@RestController
public class RentalsController {
    private final RentalService rentalService;

    @Autowired
    public RentalsController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public ResponseEntity<List<RentalDTO>> getAll() {
        List<RentalDTO> rentals = rentalService.getAll();
        CustomLogger.getInstance().info("Sent all rentals");
        return ResponseEntity.ok(rentals);
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<?> getAllByVehicle(@PathVariable String vehicleId) {
        int id = Integer.parseInt(vehicleId);
        List<RentalDTO> rentalsByVehicle = rentalService.getRentalsByVehicle(id);

        return ResponseEntity.ok(rentalsByVehicle);
    }

    @GetMapping("/map-vehicles")
    public ResponseEntity<?> getVehiclesOnMap(){
        List<MapVehicleDTO> vehiclesOnMap = rentalService.getVehiclesOnMap();
        if(!vehiclesOnMap.isEmpty()){
            return ResponseEntity.ok(vehiclesOnMap);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
}
