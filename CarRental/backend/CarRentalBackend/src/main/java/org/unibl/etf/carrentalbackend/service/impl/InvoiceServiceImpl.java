package org.unibl.etf.carrentalbackend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.carrentalbackend.model.dto.MonthlyIncomeDTO;
import org.unibl.etf.carrentalbackend.model.dto.VehicleTypeIncomeDTO;
import org.unibl.etf.carrentalbackend.model.entities.Invoice;
import org.unibl.etf.carrentalbackend.model.entities.RentalVehicle;
import org.unibl.etf.carrentalbackend.model.enums.VehicleType;
import org.unibl.etf.carrentalbackend.repository.InvoiceRepository;
import org.unibl.etf.carrentalbackend.service.interfaces.InvoiceService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public List<MonthlyIncomeDTO> getMonthlyIncomes() {
        List<Invoice> invoices = invoiceRepository.findAll();
        Map<LocalDateTime, BigDecimal> dailyTotals = new TreeMap<>(Comparator.reverseOrder());

        for (Invoice invoice : invoices) {
            LocalDateTime issueDate = invoice.getIssueDate();
            int year = issueDate.getYear();
            if(LocalDateTime.now().getYear() == year) {     // We only take current year invoices
                dailyTotals.put(issueDate, invoice.getGrandTotal());
            }
        }

        // Convert to DTOs
        return dailyTotals.entrySet().stream()
                .map(entry -> new MonthlyIncomeDTO(
                        entry.getKey(),
                        entry.getValue()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleTypeIncomeDTO> getVehicleTypeIncomes() {
        List<Invoice> invoices = invoiceRepository.findAll();
        Map<VehicleType, BigDecimal> vehicleTypeIncomes = new HashMap<>();
        for(Invoice invoice : invoices) {
            RentalVehicle vehicle = invoice.getRental().getVehicle();
            if(vehicleTypeIncomes.containsKey(vehicle.getType_())) {        // If type/key exists, add to existing income
//                BigDecimal oldIncome = vehicleTypeIncomes.get(vehicle.getType_());
//                vehicleTypeIncomes.put(vehicle.getType_(), oldIncome.add(invoice.getGrandTotal()));
                vehicleTypeIncomes.computeIfPresent(
                        vehicle.getType_(), (key, oldIncome) -> oldIncome.add(invoice.getGrandTotal())
                );
            }
            else{
                vehicleTypeIncomes.put(vehicle.getType_(), invoice.getGrandTotal());
            }
        }

        // If there are < 3 types, populate the rest with 0's
        if(!vehicleTypeIncomes.containsKey(VehicleType.Car)) vehicleTypeIncomes.put(VehicleType.Car, BigDecimal.ZERO);
        if(!vehicleTypeIncomes.containsKey(VehicleType.Bicycle)) vehicleTypeIncomes.put(VehicleType.Bicycle, BigDecimal.ZERO);
        if(!vehicleTypeIncomes.containsKey(VehicleType.Scooter)) vehicleTypeIncomes.put(VehicleType.Scooter, BigDecimal.ZERO);

        return vehicleTypeIncomes.entrySet().stream()
                .map(entry -> new VehicleTypeIncomeDTO(
                        entry.getKey().toString(),
                        entry.getValue()
                ))
                .collect(Collectors.toList());
    }
}
