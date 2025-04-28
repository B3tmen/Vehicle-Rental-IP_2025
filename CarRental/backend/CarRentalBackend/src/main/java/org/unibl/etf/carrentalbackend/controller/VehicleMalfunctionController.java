package org.unibl.etf.carrentalbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.carrentalbackend.model.dto.MalfunctionDTO;
import org.unibl.etf.carrentalbackend.model.dto.VehicleMalfunctionDTO;
import org.unibl.etf.carrentalbackend.service.interfaces.VehicleMalfunctionService;
import org.unibl.etf.carrentalbackend.util.CustomLogger;

import java.util.List;

import static org.unibl.etf.carrentalbackend.util.Constants.EndpointUrls.API_VEHICLE_MALFUNCTIONS_URL;

@RestController
@RequestMapping(API_VEHICLE_MALFUNCTIONS_URL)
public class VehicleMalfunctionController {
    private final VehicleMalfunctionService vehicleMalfunctionService;

    @Autowired
    public VehicleMalfunctionController(VehicleMalfunctionService vehicleMalfunctionService) {
        this.vehicleMalfunctionService = vehicleMalfunctionService;
    }

    @PostMapping
    public ResponseEntity<?> insert(@Validated @RequestBody VehicleMalfunctionDTO vehicleMalfunctionDTO){
        VehicleMalfunctionDTO inserted = vehicleMalfunctionService.insert(vehicleMalfunctionDTO);
        if(inserted != null){
            return ResponseEntity.ok(inserted);
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/{vehicleId}")
    public ResponseEntity<List<MalfunctionDTO>> getAllMalfunctions(@PathVariable int vehicleId) {
        List<MalfunctionDTO> malfunctions = vehicleMalfunctionService.getMalfunctionsByVehicle(vehicleId);
        CustomLogger.getInstance().info("Sent all malfunctions by vehicle");
        return ResponseEntity.ok(malfunctions);
    }

}
