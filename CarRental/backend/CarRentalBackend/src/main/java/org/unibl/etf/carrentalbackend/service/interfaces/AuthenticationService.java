package org.unibl.etf.carrentalbackend.service.interfaces;

import org.unibl.etf.carrentalbackend.model.dto.AuthenticationResponseDTO;
import org.unibl.etf.carrentalbackend.model.dto.ClientDTO;
import org.unibl.etf.carrentalbackend.model.dto.LoginRequestDTO;
import org.unibl.etf.carrentalbackend.model.dto.UserDTO;

public interface AuthenticationService {
    AuthenticationResponseDTO login(LoginRequestDTO loginRequestDTO);

}
