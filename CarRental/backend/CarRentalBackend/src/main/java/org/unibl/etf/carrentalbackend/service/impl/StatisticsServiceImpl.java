package org.unibl.etf.carrentalbackend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.carrentalbackend.model.dto.*;
import org.unibl.etf.carrentalbackend.service.interfaces.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final RentalVehicleService rentalVehicleService;
    private final UserService userService;
    private final EmployeeService employeeService;
    private final VehicleMalfunctionService vehicleMalfunctionService;
    private final InvoiceService invoiceService;

    @Autowired
    public StatisticsServiceImpl(RentalVehicleService rentalVehicleService, UserService userService, EmployeeService employeeService, VehicleMalfunctionService vehicleMalfunctionService, InvoiceService invoiceService) {
        this.rentalVehicleService = rentalVehicleService;
        this.userService = userService;
        this.employeeService = employeeService;
        this.vehicleMalfunctionService = vehicleMalfunctionService;
        this.invoiceService = invoiceService;
    }


    @Override
    public Map<String, Integer> getVehicleTypesDistribution() {
        return rentalVehicleService.getAll().stream()
                .collect(Collectors.groupingBy(
                        vehicle -> vehicle.getType_().toString(),  // Convert Enum to String
                        Collectors.summingInt(e -> 1)  // Count occurrences
                ));
    }

    @Override
    public Map<String, Integer> getUserTypesDistribution() {
        return userService.getAll().stream()
                .collect(Collectors.groupingBy(
                        user -> user.getType().toString(),
                        Collectors.summingInt(e -> 1)
                ));
    }

    @Override
    public Map<String, Integer> getEmployeeRolesDistribution() {
        return employeeService.getAll().stream()
                .collect(Collectors.groupingBy(
                        employee -> employee.getRole().toString(),
                        Collectors.summingInt(e -> 1)
                ));
    }

    @Override
    public Map<String, Integer> getMalfunctionsByVehicleType() {
        return vehicleMalfunctionService.getAll().stream()
                .collect(Collectors.groupingBy(
                        vm -> vm.getVehicle().getType_().toString(), // Group by vehicle type (CAR, BICYCLE, SCOOTER)
                        Collectors.summingInt(e -> 1)  // Count malfunctions
                ));
    }

    @Override
    public List<MonthlyIncomeDTO> getMonthlyIncomes() {
        return invoiceService.getMonthlyIncomes();
    }

    @Override
    public List<VehicleTypeIncomeDTO> getVehicleTypeIncomes() {
        return invoiceService.getVehicleTypeIncomes();
    }
}
