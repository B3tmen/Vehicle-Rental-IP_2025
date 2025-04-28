package org.unibl.etf.carrentalbackend.service.interfaces;

import org.unibl.etf.carrentalbackend.model.dto.EmployeeDTO;
import org.unibl.etf.carrentalbackend.model.entities.Employee;
import org.unibl.etf.carrentalbackend.model.enums.EmployeeRole;

public interface EmployeeService extends CrudBaseService<Employee, EmployeeDTO> {
    EmployeeRole getEmployeeRoleByUsername(String username);
}
