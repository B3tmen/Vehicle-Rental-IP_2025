package org.unibl.etf.clientapp.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.unibl.etf.clientapp.factory.impl.MySqlServiceFactoryImpl;
import org.unibl.etf.clientapp.factory.interfaces.ServiceFactory;
import org.unibl.etf.clientapp.model.beans.ClientBean;
import org.unibl.etf.clientapp.model.dto.*;
import org.unibl.etf.clientapp.model.dto.requests.AuthenticationRequest;
import org.unibl.etf.clientapp.model.dto.requests.ChangePasswordRequest;
import org.unibl.etf.clientapp.model.enums.CardType;
import org.unibl.etf.clientapp.model.enums.CitizenType;
import org.unibl.etf.clientapp.model.enums.VehicleType;
import org.unibl.etf.clientapp.service.*;
import org.unibl.etf.clientapp.util.Constants;
import org.unibl.etf.clientapp.util.HelperClass;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import static org.unibl.etf.clientapp.util.Constants.ServletURLs.PROFILE_URL;

@WebServlet(PROFILE_URL)
@MultipartConfig
public class ProfileServlet extends HttpServlet {
    private NavigationController navigationController;
    private final ClientService clientService;
    private final ImageService imageService;
    private final InvoiceService invoiceService;
    private final Gson gson;

    public ProfileServlet() {
        ServiceFactory serviceFactory = new MySqlServiceFactoryImpl();
        this.invoiceService = serviceFactory.getInvoiceService();
        this.clientService = serviceFactory.getClientService();
        this.imageService = serviceFactory.getImageService();
        this.navigationController = new NavigationController();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!HelperClass.isClientAuthenticated(req, resp)){
            navigationController.redirectToLogin(req, resp);
            return;
        }

        HttpSession session = req.getSession();
        ClientBean clientBean = (ClientBean) session.getAttribute(Constants.SessionParameters.AUTHENTICATED_CLIENT);
        if (clientBean != null) {
            // Get rentals for the client
            List<Invoice> invoices = invoiceService.getAllByClientId(clientBean.getClient().getId());
            req.setAttribute(Constants.SessionParameters.CLIENT_INVOICES, invoices);
        }

        navigationController.navigateToProfile(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        resp.setContentType("application/json");
        String oldPassword = getPasswordVariantFromName(req, "oldPassword");
        String newPassword = getPasswordVariantFromName(req, "newPassword");
        String confirmPassword = getPasswordVariantFromName(req, "confirmPassword");
        Part filePart = req.getPart("newAvatar");
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(oldPassword, newPassword, confirmPassword);
        ClientBean clientBean = (ClientBean) session.getAttribute(Constants.SessionParameters.AUTHENTICATED_CLIENT);

        boolean oldPasswordVerified = HelperClass.verifyPassword(changePasswordRequest.getOldPassword(), clientBean.getClient().getPasswordHash());
        boolean newPasswordsMatch = changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword());
        if(!oldPasswordVerified){
            MessageResponse msgResponse = new MessageResponse("Failed to change password. Check your old password!");
            resp.getWriter().write(gson.toJson(msgResponse));
            return;
        }
        if(!newPasswordsMatch) {
            MessageResponse msgResponse = new MessageResponse("Failed to change password. New passwords do not match!");
            resp.getWriter().write(gson.toJson(msgResponse));
            return;
        }
        MessageResponse msgResponse = null;
        if(filePart == null) {
            msgResponse = new MessageResponse("Failed to update image - no image selected.");
        }

        Image image = imageService.updateImage(clientBean.getClient(), filePart, req.getServletContext().getRealPath("/"));
        Client updatedClient = clientService.changePassword(clientBean.getClient(), changePasswordRequest);
        if(updatedClient != null) {
            if(msgResponse != null) msgResponse.setMessage(msgResponse.getMessage() + " Password changed successfully!");   // if msg!=null from filePart==null
            else msgResponse = new MessageResponse("Password changed successfully!");

            if(image != null) {
                clientBean.getClient().setAvatarImage(image);
                msgResponse.setMessage(msgResponse.getMessage() + " Image updated successfully!");
            }
            ClientBean updatedClientBean = new ClientBean(updatedClient);
            updatedClientBean.setAuthenticated(true);
            session.setAttribute(Constants.SessionParameters.AUTHENTICATED_CLIENT, updatedClientBean);

            resp.getWriter().write(gson.toJson(msgResponse));
        }
        else{
            msgResponse = new MessageResponse("Failed to change password. Internal error.");
            resp.getWriter().write(gson.toJson(msgResponse));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //testPdfService(req);

        HttpSession session = req.getSession();
        ClientBean clientBean = (ClientBean) session.getAttribute(Constants.SessionParameters.AUTHENTICATED_CLIENT);
        int deactivated = clientService.deactivate(clientBean.getClient().getId());
        if(deactivated == 0) {
            MessageResponse msgResponse = new MessageResponse("Failed to deactivate account. Internal error.");
            resp.getWriter().write(gson.toJson(msgResponse));
            return;
        }

        MessageResponse msgResponse = new MessageResponse("Successfully deactivated account. You will be redirected to the login page.");
        resp.getWriter().write(gson.toJson(msgResponse));
    }

    private String getPasswordVariantFromName(HttpServletRequest req, String parameterName) throws ServletException, IOException {
        return req.getPart(parameterName).getInputStream().readAllBytes().length > 0
                ? new String(req.getPart(parameterName).getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim()
                : null;
    }

    private void testPdfService(HttpServletRequest req) {
//        RentalVehicle rv = ElectricCar.builder()
//                .id(1)
//                .model("Model X")
//                .rentalPrice(BigDecimal.valueOf(10))
//                .image(null)
//                .manufacturer(new Manufacturer(1, "TESLA", "USA", "Address1", "065/123-222", "+421321321", "tesla@mail.com"))
//                .rentalStatus(new RentalStatus(1, "Free"))
//                .type(VehicleType.Car)
//                .carId("dsadsadsa")
//                .description("Descrption 1111")
//                .purchaseDate(Date.valueOf("2024-04-30"))
//                .build();
//
//        Client client = new Client(1, "username", "password", "Ivan", "Kuruzovic", "1111", "ivan@mail.com", "065/123-456", true, CitizenType.Local, 123321, null);
//
//        Rental rental = Rental.builder()
//                .id(1)
//                .vehicle(rv)
//                .client(client)
//                .rentalDateTime(Date.valueOf(LocalDate.now()))
//                .duration(3)
//                .pickupLocation(null)
//                .dropoffLocation(null)
//                .payment(new Payment(1, "token1", CardType.VISA.getName(), null, "Ivan", "Kuruzovic", "1234", client))
//                .build();
//
//        BigDecimal grandTotal = rv.getRentalPrice().multiply(BigDecimal.valueOf(rental.getDuration()));
//        Invoice invoice = new Invoice(1, "Test-Pdf", Timestamp.valueOf(rental.getRentalDateTime().toString()), grandTotal, rental);
//
//        PdfGeneratorService pdfService = new PdfGeneratorService();
//        try{
//            pdfService.generateInvoicePdf(invoice, req);
//        } catch (IOException e){
//            e.printStackTrace();
//        }

    }

}
