package org.unibl.etf.carrentalbackend.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.unibl.etf.carrentalbackend.exception.UserNotActiveException;
import org.unibl.etf.carrentalbackend.model.dto.*;
import org.unibl.etf.carrentalbackend.model.enums.EmployeeRole;
import org.unibl.etf.carrentalbackend.model.enums.UserType;
import org.unibl.etf.carrentalbackend.security.jwt.JwtService;
import org.unibl.etf.carrentalbackend.service.interfaces.AuthenticationService;
import org.unibl.etf.carrentalbackend.service.interfaces.ClientService;
import org.unibl.etf.carrentalbackend.service.interfaces.EmployeeService;
import org.unibl.etf.carrentalbackend.service.interfaces.UserService;
import org.unibl.etf.carrentalbackend.util.CustomLogger;

import java.util.Arrays;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final CustomLogger logger = CustomLogger.getInstance();

    private final UserService userService;
    private final ClientService clientService;
    private final EmployeeService employeeService;
    private final ModelMapper mapper;

    private final HttpServletRequest request;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthenticationServiceImpl(UserService userService, ClientService clientService, EmployeeService employeeService, ModelMapper mapper,
                                     HttpServletRequest request, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.clientService = clientService;
        this.employeeService = employeeService;
        this.mapper = mapper;
        this.request = request;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public AuthenticationResponseDTO login(LoginRequestDTO loginRequestDTO) throws UserNotActiveException {
        logger.info("User " + this.request.getRemoteAddr() + " is trying to log in...");
        UserDTO user = null;
        AuthenticationResponseDTO dto = null;

        String token = verify(loginRequestDTO);
        if(!token.equals("Fail")){
            user = userService.getByUsername(loginRequestDTO.getUsername());
            if(user != null){
                if(!user.getIsActive())
                    throw new UserNotActiveException("User " + user.getUsername() + " is not active");

                logger.info("User " + user.getUsername() + " is logged in");

                if(user.getType().equals(UserType.Employee)){   // No use to check for clients, because they can't use this backend application
                    user = employeeService.getById(user.getId());
                    dto = new AuthenticationResponseDTO(user, token, Arrays.asList(EmployeeRole.Administrator, EmployeeRole.Manager, EmployeeRole.Operator));
                }
            }
            else{
                logger.warning("User " + this.request.getRemoteAddr() + " is not logged in. Entered bad credentials.");
            }
        }

        return dto;
    }

    public String verify(LoginRequestDTO request){
        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        if(auth.isAuthenticated()){
            String token = jwtService.generateToken(request.getUsername());
            logger.info("Generated JWT token: " + token);
            return token;
        }

        return "Fail";
    }
}
