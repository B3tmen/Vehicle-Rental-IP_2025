package org.unibl.etf.carrentalbackend.service.interfaces;

import org.unibl.etf.carrentalbackend.model.dto.UserDTO;
import org.unibl.etf.carrentalbackend.model.entities.User;

public interface UserService extends CrudBaseService<User, UserDTO> {
    UserDTO getByUsername(String username);
}
