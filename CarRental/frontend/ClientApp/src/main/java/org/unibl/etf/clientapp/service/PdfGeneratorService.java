package org.unibl.etf.clientapp.service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import jakarta.servlet.http.HttpServletRequest;
import org.unibl.etf.clientapp.model.dto.Client;
import org.unibl.etf.clientapp.model.dto.Invoice;
import org.unibl.etf.clientapp.model.dto.Rental;
import org.unibl.etf.clientapp.model.dto.RentalVehicle;
import org.unibl.etf.clientapp.util.ConfigReader;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PdfGeneratorService {
    private final ConfigReader configReader = ConfigReader.getInstance();

    private PdfFont boldFont;
    private PdfFont normalFont;

    public PdfGeneratorService() {
        try{
            boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateInvoicePdf(Invoice invoice, HttpServletRequest req) throws IOException  {
        Client client = invoice.getRental().getClient();
        String destination = req.getServletContext().getRealPath("/") + configReader.getInvoicesRelativePath() + invoice.getPdfName();
        Path absolutePath = Paths.get(destination).toAbsolutePath().normalize();
        String absolutePathStr = absolutePath.toString();
        System.out.println("Invoice path: " + absolutePathStr);

        File file = new File(absolutePath.toString());
        //if(!file.getParentFile().mkdirs()) throw new FileNotFoundException("Invoice parent directory does not exist");
        file.getParentFile().mkdirs();

        // Initialize PDF writer and document
        PdfWriter writer = new PdfWriter(absolutePathStr);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Add a title
        Paragraph title = new Paragraph("INVOICE")
                .setFont(boldFont)
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        document.add(title);

        // Create a table for the header with two columns
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{50, 50}))
                .useAllAvailableWidth()
                .setMarginBottom(20);
        headerTable.setBorder(Border.NO_BORDER);

        // Left column (empty in this case, or you could put a logo)
        headerTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

        // Right column for invoice number and date
        String invoiceDate = invoice.getIssueDate().toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Paragraph invoiceInfo = new Paragraph()
                .add(new Text("Invoice no: " + invoice.getId() + "\n")
                        .setFont(boldFont)
                        .setFontSize(12))
                .add(new Text("Issue Date: " + invoiceDate)
                        .setFont(normalFont)
                        .setFontSize(10)
                        .setTextAlignment(TextAlignment.RIGHT));

        headerTable.addCell(new Cell()
                .add(invoiceInfo)
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(Border.NO_BORDER));

        document.add(headerTable);

        // Create a two-column layout for company and billing info
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{50, 50}))
                .useAllAvailableWidth()
                .setMarginBottom(20);
        // Left column - Company information
        infoTable.addCell(createCompanySection());
        // Right column - Billing information
        infoTable.addCell(createClientSection(client));
        document.add(infoTable);

        // Adding rental details table
        document.add(createRentalTable(invoice));
        document.add(new Paragraph().setMarginBottom(10));

        // Adding footer with date
        Paragraph footer = new Paragraph()
                .add("Invoice generated on: " +
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .setFont(normalFont)
                .setTextAlignment(TextAlignment.RIGHT);
        document.add(footer);

        document.close();
    }

    private Paragraph createCompanySection() {
        Paragraph companySection = new Paragraph()
                .add(new Text("Billing Information\n").setFont(boldFont).setFontSize(12))
                .add(new Text("\nCompany\n").setFont(boldFont))
                .add(new Text("ETFBL_IP Vehicle Rentals" + "\n").setFont(normalFont))
                .add(new Text("\nAddress\n").setFont(boldFont))
                .add(new Text("Banja Luka, Patre 5" + "\n").setFont(normalFont));
        return companySection;
    }

    private Paragraph createClientSection(Client client) {
        Paragraph clientSection = new Paragraph()
                .add(new Text("Client Information\n").setFont(boldFont).setFontSize(12))
                .add(new Text("\nName\n").setFont(boldFont))
                .add(new Text(client.getFirstName() + " " + client.getLastName() + "\n").setFont(normalFont))
                .add(new Text("\nEmail\n").setFont(boldFont))
                .add(new Text(client.getEmail()).setFont(normalFont));
        return clientSection;
    }

    private Table createRentalTable(Invoice invoice) {
        RentalVehicle vehicle = invoice.getRental().getVehicle();
        Rental rental = invoice.getRental();

        Table table = new Table(UnitValue.createPercentArray(5)).useAllAvailableWidth();
        table.setMarginTop(10);
        table.setMarginBottom(10);

        // Table headers
        addTableHeader(table, "Type", boldFont);
        addTableHeader(table, "Manufacturer", boldFont);
        addTableHeader(table, "Model", boldFont);
        addTableHeader(table, "Price/Hour", boldFont);
        addTableHeader(table, "Rent Duration", boldFont);

        // Add car data
        addTableCell(table, vehicle.getRuntimeVehicleType(), normalFont);
        addTableCell(table, vehicle.getManufacturer().getName(), normalFont);
        addTableCell(table, vehicle.getModel(), normalFont);
        addTableCell(table, String.format("$%.2f", vehicle.getRentalPrice()), normalFont);
        addTableCell(table, rental.getDuration() + " hours", normalFont);

        // Add grand total row (spanning all columns)
        Cell totalLabelCell = new Cell(1, 4)
                .add(new Paragraph("Grand Total:").setFont(boldFont))
                .setTextAlignment(TextAlignment.RIGHT);

        Cell totalValueCell = new Cell()
                .add(new Paragraph(String.format("$%.2f", invoice.getGrandTotal())).setFont(boldFont));

        table.addCell(totalLabelCell);
        table.addCell(totalValueCell);

        return table;
    }

    private void addTableHeader(Table table, String headerText, PdfFont font) {
        Cell header = new Cell().add(new Paragraph(headerText).setFont(font));
        header.setBackgroundColor(ColorConstants.LIGHT_GRAY);
        table.addHeaderCell(header);
    }

    private void addTableCell(Table table, String cellText, PdfFont font) {
        table.addCell(new Cell().add(new Paragraph(cellText).setFont(font)));
    }
}
