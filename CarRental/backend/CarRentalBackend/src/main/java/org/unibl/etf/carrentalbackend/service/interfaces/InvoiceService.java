package org.unibl.etf.carrentalbackend.service.interfaces;

import org.unibl.etf.carrentalbackend.model.dto.MonthlyIncomeDTO;
import org.unibl.etf.carrentalbackend.model.dto.VehicleTypeIncomeDTO;

import java.util.List;

public interface InvoiceService {
    List<MonthlyIncomeDTO> getMonthlyIncomes();
    List<VehicleTypeIncomeDTO> getVehicleTypeIncomes();
}
