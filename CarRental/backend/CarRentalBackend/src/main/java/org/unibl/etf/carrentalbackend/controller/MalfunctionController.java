package org.unibl.etf.carrentalbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.carrentalbackend.model.dto.MalfunctionDTO;
import org.unibl.etf.carrentalbackend.service.interfaces.MalfunctionService;
import org.unibl.etf.carrentalbackend.util.CustomLogger;

import java.util.List;

import static org.unibl.etf.carrentalbackend.util.Constants.EndpointUrls.API_MALFUNCTIONS_URL;

@RestController
@RequestMapping(API_MALFUNCTIONS_URL)
public class MalfunctionController {

    private final MalfunctionService malfunctionService;

    @Autowired
    public MalfunctionController(MalfunctionService malfunctionService) {
        this.malfunctionService = malfunctionService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<MalfunctionDTO> malfunctions = malfunctionService.getAll();
        CustomLogger.getInstance().info("Sent all malfunctions");

        return ResponseEntity.ok(malfunctions);
    }

    @PostMapping
    public ResponseEntity<?> insert(@Validated @RequestBody MalfunctionDTO malfunctionDTO){
        MalfunctionDTO inserted = malfunctionService.insert(malfunctionDTO);
        if(inserted != null){
            return ResponseEntity.ok(inserted);
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id){
        boolean deleted = malfunctionService.delete(id);
        if(!deleted){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
