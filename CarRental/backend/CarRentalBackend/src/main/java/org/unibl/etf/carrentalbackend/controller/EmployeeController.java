package org.unibl.etf.carrentalbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.unibl.etf.carrentalbackend.model.dto.EmployeeDTO;
import org.unibl.etf.carrentalbackend.service.interfaces.EmployeeService;

import java.net.URI;
import java.util.List;

import static org.unibl.etf.carrentalbackend.util.Constants.EndpointUrls.API_EMPLOYEES_URL;

@RestController
@RequestMapping(API_EMPLOYEES_URL)
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAll() {
        List<EmployeeDTO> employees = employeeService.getAll();

        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getById(@PathVariable int id) {
        EmployeeDTO employee = employeeService.getById(id);
        if(employee != null){
            return ResponseEntity.ok(employee);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> insert(@Validated @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO insertedEmployee = employeeService.insert(employeeDTO);
        if(insertedEmployee != null) {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(insertedEmployee.getId()).toUri();

            return ResponseEntity.created(location).body(insertedEmployee);
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> update(@Validated @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO updatedEmployee = employeeService.update(employeeDTO);
        if(updatedEmployee != null){
            return ResponseEntity.ok(updatedEmployee);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        boolean deleted = employeeService.delete(id);

        if(deleted){
            return ResponseEntity.noContent().build();
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
}
