package org.unibl.etf.carrentalbackend.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.carrentalbackend.interfaces.EntityDTOConverter;
import org.unibl.etf.carrentalbackend.model.dto.*;
import org.unibl.etf.carrentalbackend.model.entities.*;
import org.unibl.etf.carrentalbackend.model.enums.UserType;
import org.unibl.etf.carrentalbackend.repository.UserRepository;
import org.unibl.etf.carrentalbackend.service.interfaces.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService, EntityDTOConverter<User, UserDTO> {
    private final UserRepository repository;
    private final ModelMapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<UserDTO> getAll() {
        return repository.findAll().stream()
                .filter(u -> u.getIsActive().equals((byte) 1))
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public UserDTO getById(int id) {
        User user = repository.findById(id).orElse(null);
        return convertToDTO(user);
    }

    @Override
    public UserDTO insert(UserDTO dto) {
        return null;
    }

    @Override
    public UserDTO update(UserDTO dto) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public UserDTO getByUsername(String username) {
        User user = repository.findByUsername(username);

        return convertToDTO(user);
    }

    @Override
    public UserDTO convertToDTO(User entity) {
        if (UserType.Client.equals(entity.getType())) {
            Client client = (Client) entity;
            return mapper.map(client, ClientDTO.class);
        }
        else if (UserType.Employee.equals(entity.getType())) {
            Employee employee = (Employee) entity;
            return mapper.map(employee, EmployeeDTO.class);
        }

        return null;
    }

    @Override
    public User convertToEntity(UserDTO dto) {
        if (UserType.Client.equals(dto.getType())) {
            ClientDTO client = (ClientDTO) dto;
            return mapper.map(client, Client.class);
        }
        else if (UserType.Employee.equals(dto.getType())) {
            EmployeeDTO employee = (EmployeeDTO) dto;
            return mapper.map(employee, Employee.class);
        }

        return null;
    }
}
