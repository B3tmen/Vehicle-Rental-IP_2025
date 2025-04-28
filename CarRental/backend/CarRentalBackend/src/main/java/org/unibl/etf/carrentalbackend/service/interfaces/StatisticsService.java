package org.unibl.etf.carrentalbackend.service.interfaces;

import org.unibl.etf.carrentalbackend.model.dto.MonthlyIncomeDTO;
import org.unibl.etf.carrentalbackend.model.dto.VehicleTypeIncomeDTO;

import java.util.List;
import java.util.Map;

public interface StatisticsService {
    Map<String, Integer> getVehicleTypesDistribution();
    Map<String, Integer> getUserTypesDistribution();
    Map<String, Integer> getEmployeeRolesDistribution();
    Map<String, Integer> getMalfunctionsByVehicleType();
    List<MonthlyIncomeDTO> getMonthlyIncomes();
    List<VehicleTypeIncomeDTO> getVehicleTypeIncomes();
}
