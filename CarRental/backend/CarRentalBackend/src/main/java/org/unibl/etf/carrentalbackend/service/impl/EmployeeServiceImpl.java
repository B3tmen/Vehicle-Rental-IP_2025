package org.unibl.etf.carrentalbackend.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.unibl.etf.carrentalbackend.interfaces.EntityDTOConverter;
import org.unibl.etf.carrentalbackend.model.dto.EmployeeDTO;
import org.unibl.etf.carrentalbackend.model.entities.Employee;
import org.unibl.etf.carrentalbackend.model.enums.EmployeeRole;
import org.unibl.etf.carrentalbackend.repository.EmployeeRepository;
import org.unibl.etf.carrentalbackend.service.interfaces.EmployeeService;
import org.unibl.etf.carrentalbackend.util.TextHasher;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService, EntityDTOConverter<Employee, EmployeeDTO> {

    private final EmployeeRepository repository;
    private final ModelMapper mapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Override
    public List<EmployeeDTO> getAll() {
        return repository.findAll().stream()
                .filter(e -> e.getIsActive().equals((byte) 1))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO getById(int id) {
        Employee employee = repository.findById(id).filter(e -> e.getIsActive().equals((byte) 1)).orElse(null);
        return convertToDTO(employee);
    }

    @Override
    public EmployeeDTO insert(EmployeeDTO dto) {
        String hash = TextHasher.getBCryptHash(dto.getPasswordHash());
        dto.setPasswordHash(hash);

        Employee employee = repository.save(convertToEntity(dto));
        return convertToDTO(employee);
    }

    @Override
    public EmployeeDTO update(EmployeeDTO dto) {
        String hash = TextHasher.getBCryptHash(dto.getPasswordHash());
        dto.setPasswordHash(hash);

        Employee employee = repository.save(convertToEntity(dto));
        return convertToDTO(employee);
    }

    @Override
    public boolean delete(Integer id) {
        boolean exists = repository.existsById(id);
        if(!exists){
            return false;
        }

        EmployeeDTO employee = getById(id);
        employee.setIsActive(false);
        EmployeeDTO updated = update(employee);

        return updated != null;
    }

    @Override
    public EmployeeRole getEmployeeRoleByUsername(String username) {
        Employee employee = repository.findByUsername(username);
        if(employee == null){
            throw new UsernameNotFoundException("User not found");
        }

        return employee.getRole();
    }

    @Override
    public EmployeeDTO convertToDTO(Employee entity) {
        return mapper.map(entity, EmployeeDTO.class);
    }

    @Override
    public Employee convertToEntity(EmployeeDTO dto) {
        return mapper.map(dto, Employee.class);
    }


}
