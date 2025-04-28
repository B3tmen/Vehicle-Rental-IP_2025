package org.unibl.etf.carrentalbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.carrentalbackend.model.dto.UserDTO;
import org.unibl.etf.carrentalbackend.service.interfaces.UserService;
import org.unibl.etf.carrentalbackend.util.CustomLogger;

import java.util.List;

import static org.unibl.etf.carrentalbackend.util.Constants.EndpointUrls.API_USERS_URL;

@RestController
@RequestMapping(API_USERS_URL)
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAll();
        CustomLogger.getInstance().info("Sent all users");
        return ResponseEntity.ok(users);
    }

    @GetMapping("/quantity")
    public int getQuantity() {
        List<UserDTO> users = userService.getAll();
        return users.size();
    }
}
