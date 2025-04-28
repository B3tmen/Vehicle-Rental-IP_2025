package org.unibl.etf.clientapp.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.unibl.etf.clientapp.factory.impl.MySqlServiceFactoryImpl;
import org.unibl.etf.clientapp.factory.interfaces.ServiceFactory;
import org.unibl.etf.clientapp.model.beans.ClientBean;
import org.unibl.etf.clientapp.model.beans.DriveBean;
import org.unibl.etf.clientapp.model.beans.VehicleBean;
import org.unibl.etf.clientapp.model.dto.*;
import org.unibl.etf.clientapp.model.dto.requests.PaymentCardRequest;
import org.unibl.etf.clientapp.model.dto.requests.RentalPaymentRequest;
import org.unibl.etf.clientapp.model.enums.CitizenType;
import org.unibl.etf.clientapp.service.*;
import org.unibl.etf.clientapp.util.Constants;
import org.unibl.etf.clientapp.util.CustomLogger;
import org.unibl.etf.clientapp.util.SqlDateAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@WebServlet(Constants.ServletURLs.PAYMENT_URL)
public class PaymentServlet extends HttpServlet {
    private final CustomLogger logger = CustomLogger.getInstance(PaymentServlet.class);
    private final NavigationController navigationController;
    private final PassportService passportService;
    private final PaymentService paymentService;
    private final RentalService rentalService;
    private final InvoiceService invoiceService;
    private final Gson gson;

    public PaymentServlet() {
        ServiceFactory serviceFactory = new MySqlServiceFactoryImpl();
        navigationController = new NavigationController();
        passportService = serviceFactory.getPassportService();
        paymentService = serviceFactory.getPaymentService();
        rentalService = serviceFactory.getRentalService();
        invoiceService = serviceFactory.getInvoiceService();
        gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new SqlDateAdapter())
                .create();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        resp.setContentType("application/json");

        RentalPaymentRequest rentalPaymentRequest = getRentalPayment(req);
        PaymentCardRequest paymentCardRequest = rentalPaymentRequest.getPaymentData();
        if(rentalPaymentRequest == null) {
            MessageResponse messageResponse = new MessageResponse("Missing payment data. Check your fields and try again.");
            resp.getWriter().write(gson.toJson(messageResponse));
            return;
        }

        ClientBean clientBean = (ClientBean) session.getAttribute(Constants.SessionParameters.AUTHENTICATED_CLIENT);
        VehicleBean vehicleBean = (VehicleBean) session.getAttribute(Constants.SessionParameters.SELECTED_VEHICLE_FOR_RENT);
        Passport passport = rentalPaymentRequest.getForeignerPassport();
        Location pickupLocation = rentalPaymentRequest.getStartLocation();
        Location dropoffLocation = rentalPaymentRequest.getEndLocation();
        Payment payment = getPayment(req, rentalPaymentRequest.getPaymentData());

        if(clientBean != null && vehicleBean != null && pickupLocation != null && dropoffLocation != null) {
            Rental rental = Rental.builder()
                    .id(null)
                    .vehicle(vehicleBean.getVehicle())
                    .client(clientBean.getClient())
                    .rentalDateTime(LocalDateTime.now())   //Date.valueOf(LocalDate.now())
                    .duration(paymentCardRequest.getRentDuration())
                    .pickupLocation(pickupLocation)
                    .dropoffLocation(dropoffLocation)
                    .payment(payment)
                    .build();

            completeRental(session, req, resp, rental, passport);
        }
    }

    private RentalPaymentRequest getRentalPayment(HttpServletRequest req) throws ServletException, IOException {
        RentalPaymentRequest rentalPaymentRequest = null;
        try (BufferedReader reader = req.getReader()) {
            String json = reader.readLine();
            rentalPaymentRequest = gson.fromJson(json, RentalPaymentRequest.class);
        } catch (JsonSyntaxException e) {
            logger.error(e.getMessage());
        }

        return rentalPaymentRequest;
    }

    private Payment getPayment(HttpServletRequest req, PaymentCardRequest paymentCardRequest) throws ServletException, IOException {
        String cardNumber = paymentCardRequest.getCardNumber();
        String cardType = paymentCardRequest.getCardType();
        String holderFirstName = paymentCardRequest.getHolderFirstName();
        String holderLastName = paymentCardRequest.getHolderLastName();
        String cvv = paymentCardRequest.getCvv();
        String expiryDateMonth = paymentCardRequest.getExpiryMonth();
        String expiryDateYear = paymentCardRequest.getExpiryYear();

        String token = paymentService.generateCardToken(cardNumber, cvv);
        String last4Digits = paymentService.extractLast4Digits(cardNumber);
        Date expiryDate = paymentService.generateDate(expiryDateMonth, expiryDateYear);
        ClientBean clientBean1 = (ClientBean) req.getSession().getAttribute(Constants.SessionParameters.AUTHENTICATED_CLIENT);

        return new Payment(null, token, cardType, expiryDate, holderFirstName, holderLastName, last4Digits, clientBean1.getClient());
    }

    private boolean doPayment(HttpSession session, Rental rental, Passport passport) throws ServletException, IOException {
        ClientBean clientBean = (ClientBean) session.getAttribute(Constants.SessionParameters.AUTHENTICATED_CLIENT);
        if(CitizenType.Foreigner.equals(clientBean.getClient().getCitizenType())) {
            if(passport == null)
                return false;

            int insertedPassport = passportService.insert(passport, rental.getClient().getId());
            if(insertedPassport == 0) {
                return false;
            }
        }
        Payment paymentInserted = paymentService.insert(rental.getPayment());
        if(paymentInserted == null) {
            return false;
        }
        rental.setPayment(paymentInserted);

        BigDecimal price = rental.getVehicle().getRentalPrice();
        int rentalDuration = rental.getDuration();
        DriveBean driveBean = new DriveBean(rentalDuration, price.multiply(new BigDecimal(rentalDuration)));
        session.setAttribute(Constants.SessionParameters.DRIVE_BEAN, driveBean);

        return true;
    }

    private void completeRental(HttpSession session, HttpServletRequest req, HttpServletResponse resp, Rental rental, Passport passport) throws ServletException, IOException{
        boolean paymentSuccessful = doPayment(session, rental, passport);
        if(!paymentSuccessful) {
            MessageResponse msgResponse = new MessageResponse("Payment failed. Check your fields and try again. Did you enter a correct card number or CVV?");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(msgResponse));
            return;
        }
        Rental insertedRental = rentalService.insert(rental);
        if(insertedRental == null) {
            MessageResponse messageResponse = new MessageResponse("Rental was not successful. Check your fields and try again.");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(messageResponse));
            return;
        }
        boolean invoiceCreated = createInvoice(insertedRental, req);

        if(invoiceCreated) {
            MessageResponse messageResponse = new MessageResponse("Your payment/rental was successful! You will be redirected to the driving window.");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(gson.toJson(messageResponse));
        }
    }

    private boolean createInvoice(Rental rental, HttpServletRequest req) throws ServletException {
        Invoice invoice = invoiceService.insert(rental);
        DriveBean bean = new DriveBean(invoice.getRental().getDuration(), invoice.getGrandTotal());
        req.getSession().setAttribute(Constants.SessionParameters.DRIVE_BEAN, bean);

        if(invoice != null){
            PdfGeneratorService pdfGeneratorService = new PdfGeneratorService();
            try{
                pdfGeneratorService.generateInvoicePdf(invoice, req);
            } catch (IOException e){
                logger.error(e.getMessage());
                return false;
            }

            return true;
        }

        return false;
    }

}
