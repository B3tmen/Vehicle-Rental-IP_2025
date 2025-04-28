package org.unibl.etf.carrentalbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.unibl.etf.carrentalbackend.model.dto.AnnouncementDTO;
import org.unibl.etf.carrentalbackend.service.interfaces.AnnouncementService;
import org.unibl.etf.carrentalbackend.util.CustomLogger;

import java.net.URI;
import java.util.List;

import static org.unibl.etf.carrentalbackend.util.Constants.EndpointUrls.API_ANNOUNCEMENTS_URL;

@RestController
@RequestMapping(API_ANNOUNCEMENTS_URL)
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @Autowired
    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<AnnouncementDTO> announcements = announcementService.getAll();
        CustomLogger.getInstance().info("[Server]: Sent all announcements.");
        return ResponseEntity.ok(announcements);
    }

    @PostMapping
    public ResponseEntity<?> insert(@Validated @RequestBody AnnouncementDTO announcementDTO) {
        AnnouncementDTO inserted = announcementService.insert(announcementDTO);
        if(inserted != null) {
            CustomLogger.getInstance().info("[Server]: Added announcement with ID: " + inserted.getId());
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
