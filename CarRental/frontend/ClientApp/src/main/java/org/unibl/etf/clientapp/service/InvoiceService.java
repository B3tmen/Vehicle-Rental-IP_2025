package org.unibl.etf.clientapp.service;

import org.unibl.etf.clientapp.database.dao.interfaces.InvoiceDAO;
import org.unibl.etf.clientapp.model.dto.Invoice;
import org.unibl.etf.clientapp.model.dto.Rental;
import org.unibl.etf.clientapp.util.ConfigReader;
import org.unibl.etf.clientapp.util.Constants;
import org.unibl.etf.clientapp.util.CustomLogger;
import org.unibl.etf.clientapp.util.UUIDHelper;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InvoiceService {
    private final ConfigReader configReader = ConfigReader.getInstance();
    private final CustomLogger logger = CustomLogger.getInstance(InvoiceService.class);
    private final InvoiceDAO dao;

    public InvoiceService(InvoiceDAO dao) {
        this.dao = dao;
    }

    public Invoice insert(Rental rental) {
        BigDecimal rentalPrice = rental.getVehicle().getRentalPrice();
        String pdfName = UUIDHelper.getRandomUUID() + Constants.PDF_EXTENSION;
        String url = configReader.getInvoicesURL() + pdfName;
        Timestamp issueDate = Timestamp.valueOf(LocalDateTime.now());
        BigDecimal grandTotal = rentalPrice.multiply(new BigDecimal(rental.getDuration()));
        Invoice invoiceToInsert = new Invoice(null, pdfName, url, issueDate, grandTotal, rental);
        Invoice invoice = null;

        try{
            invoice = dao.insert(invoiceToInsert);
        } catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return invoice;
    }

    public List<Invoice> getAllByClientId(int clientId) {
        List<Invoice> invoices = new ArrayList<>();
        try{
            invoices = dao.getAllByClientId(clientId);
        } catch (SQLException e){
            logger.error(e.getMessage());
        }

        return invoices;
    }
}
