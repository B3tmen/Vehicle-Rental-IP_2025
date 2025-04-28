package org.unibl.etf.carrentalbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.unibl.etf.carrentalbackend.model.dto.PromotionDTO;
import org.unibl.etf.carrentalbackend.service.interfaces.PromotionService;
import org.unibl.etf.carrentalbackend.util.CustomLogger;

import java.net.URI;
import java.util.List;

import static org.unibl.etf.carrentalbackend.util.Constants.EndpointUrls.API_PROMOTIONS_URL;

@RestController
@RequestMapping(API_PROMOTIONS_URL)
public class PromotionController {
    private final PromotionService promotionService;

    @Autowired
    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<PromotionDTO> promotions = promotionService.getAll();
        CustomLogger.getInstance().info("[Server]: Sent all promotions");
        return ResponseEntity.ok(promotions);
    }

    @PostMapping
    public ResponseEntity<?> insert(@Validated @RequestBody PromotionDTO promotionDTO) {
        PromotionDTO inserted = promotionService.insert(promotionDTO);
        if(inserted != null) {
            CustomLogger.getInstance().info("[Server]: Added promotion");
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(inserted.getId()).toUri();

            return ResponseEntity.created(location).build();
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }
}
