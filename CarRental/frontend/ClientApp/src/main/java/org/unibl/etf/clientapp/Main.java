package org.unibl.etf.clientapp;

import org.unibl.etf.clientapp.database.dao.impl.InvoiceDAOImpl;
import org.unibl.etf.clientapp.database.dao.interfaces.InvoiceDAO;
import org.unibl.etf.clientapp.factory.impl.MySqlServiceFactoryImpl;
import org.unibl.etf.clientapp.factory.interfaces.ServiceFactory;
import org.unibl.etf.clientapp.model.dto.*;
import org.unibl.etf.clientapp.model.enums.CardType;
import org.unibl.etf.clientapp.model.enums.CitizenType;
import org.unibl.etf.clientapp.model.enums.RentalStatusEnum;
import org.unibl.etf.clientapp.model.enums.VehicleType;
import org.unibl.etf.clientapp.service.InvoiceService;
import org.unibl.etf.clientapp.service.PdfGeneratorService;
import org.unibl.etf.clientapp.service.RentalService;
import org.unibl.etf.clientapp.service.RentalVehicleService;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        InvoiceDAO invoiceDAO = new InvoiceDAOImpl();

        InvoiceService invoiceService = new InvoiceService(new InvoiceDAOImpl());
        Rental rental = Rental.builder()
                .id(47)
                .vehicle(ElectricCar.builder().rentalPrice(new BigDecimal(100)).build())
                .duration(3)
                .build();

        Invoice invoice = invoiceService.insert(rental);
        System.out.println(invoice);
    }
}
