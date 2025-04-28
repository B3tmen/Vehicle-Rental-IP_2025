package org.unibl.etf.carrentalbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.carrentalbackend.model.dto.MonthlyIncomeDTO;
import org.unibl.etf.carrentalbackend.model.dto.VehicleTypeIncomeDTO;
import org.unibl.etf.carrentalbackend.service.interfaces.StatisticsService;

import java.util.List;
import java.util.Map;

import static org.unibl.etf.carrentalbackend.util.Constants.EndpointUrls.API_STATISTICS_URL;

@RestController
@RequestMapping(API_STATISTICS_URL)
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }


    @GetMapping("/vehicle-types")
    public Map<String, Integer> getVehicleTypesDistribution(){
        return statisticsService.getVehicleTypesDistribution();
    }

    @GetMapping("/user-types")
    public Map<String, Integer> getUserTypesDistribution(){
        return statisticsService.getUserTypesDistribution();
    }

    @GetMapping("/employee-roles")
    public Map<String, Integer> getEmployeeRolesDistribution(){
        return statisticsService.getEmployeeRolesDistribution();
    }

    @GetMapping("/income/monthly")
    public List<MonthlyIncomeDTO> getMonthlyIncomeDistribution(){
        return statisticsService.getMonthlyIncomes();
    }

    @GetMapping("/income/vehicle-type")
    public List<VehicleTypeIncomeDTO> getVehicleTypeIncomeDistribution(){
        return statisticsService.getVehicleTypeIncomes();
    }

    @GetMapping("/malfunctions/vehicle")
    public Map<String, Integer> getMalfunctionsByVehicle(){
        return statisticsService.getMalfunctionsByVehicleType();
    }
}
