package org.unibl.etf.carrentalbackend.controller;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.carrentalbackend.model.dto.AuthenticationResponseDTO;
import org.unibl.etf.carrentalbackend.model.dto.LoginRequestDTO;
import org.unibl.etf.carrentalbackend.service.interfaces.AuthenticationService;
import org.unibl.etf.carrentalbackend.util.CustomLogger;

import static org.unibl.etf.carrentalbackend.util.Constants.EndpointUrls.API_AUTH_URL;

@RestController
@RequestMapping(API_AUTH_URL)
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequestDTO loginRequestDTO) {
        CustomLogger.getInstance().info("[Server]: Attempting login ...");
        AuthenticationResponseDTO authResponse = authenticationService.login(loginRequestDTO);
        if(authResponse != null){
            CustomLogger.getInstance().info("[Server]: Login Successful");
            return ResponseEntity.ok(authResponse);
        }

        CustomLogger.getInstance().info("[Server]: Login Unsuccessful");
        return ResponseEntity.status(Response.SC_UNAUTHORIZED).body("Invalid credentials or user role");
    }
}
