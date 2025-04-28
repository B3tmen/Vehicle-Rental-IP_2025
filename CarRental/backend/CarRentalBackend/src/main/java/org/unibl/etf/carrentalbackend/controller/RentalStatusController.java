package org.unibl.etf.carrentalbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.carrentalbackend.model.dto.RentalStatusDTO;
import org.unibl.etf.carrentalbackend.service.interfaces.RentalStatusService;
import org.unibl.etf.carrentalbackend.util.CustomLogger;

import java.util.List;

import static org.unibl.etf.carrentalbackend.util.Constants.EndpointUrls.API_RENTAL_STATUSES_URL;

@RestController
@RequestMapping(API_RENTAL_STATUSES_URL)
public class RentalStatusController {

    private static CustomLogger logger = CustomLogger.getInstance();
    private final RentalStatusService rentalStatusService;

    @Autowired
    public RentalStatusController(RentalStatusService rentalStatusService) {
        this.rentalStatusService = rentalStatusService;
    }

    @GetMapping
    public List<RentalStatusDTO> getAll() {
        return rentalStatusService.getAll();
    }
}
