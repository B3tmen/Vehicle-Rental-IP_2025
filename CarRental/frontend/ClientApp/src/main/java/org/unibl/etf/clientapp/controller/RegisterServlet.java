package org.unibl.etf.clientapp.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.unibl.etf.clientapp.database.dao.impl.ClientDAOImpl;
import org.unibl.etf.clientapp.factory.impl.MySqlServiceFactoryImpl;
import org.unibl.etf.clientapp.factory.interfaces.ServiceFactory;
import org.unibl.etf.clientapp.model.dto.Client;
import org.unibl.etf.clientapp.model.dto.Image;
import org.unibl.etf.clientapp.model.enums.CitizenType;
import org.unibl.etf.clientapp.service.ClientService;
import org.unibl.etf.clientapp.service.ImageService;
import org.unibl.etf.clientapp.util.CustomLogger;

import java.io.IOException;

import static org.unibl.etf.clientapp.util.Constants.ServletURLs.REGISTER_URL;

@WebServlet(REGISTER_URL)
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,        // 1 MB
        maxFileSize = 1024 * 1024 * 10,         // 10 MB
        maxRequestSize = 1024 * 1024 * 100      // 100 MB
)
public class RegisterServlet extends HttpServlet {
    private final CustomLogger logger = CustomLogger.getInstance(RegisterServlet.class);
    private final NavigationController navigationController;
    private final ClientService clientService;
    private final ImageService imageService;

    public RegisterServlet() {
        ServiceFactory serviceFactory = new MySqlServiceFactoryImpl();
        navigationController = new NavigationController();
        clientService = serviceFactory.getClientService();
        this.imageService = serviceFactory.getImageService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        navigationController.navigateToRegistration(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirm-password");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String personalCardNumber = req.getParameter("personalCardNumber");
        String phoneNumber = req.getParameter("phoneNumber");
        String email = req.getParameter("email");
        String citizenType = req.getParameter("citizenshipSelect");
        Part filePart = req.getPart("avatarImageFile");

        if(password.equals(confirmPassword)) {
            Client client = new Client(null, username, password, firstName, lastName, personalCardNumber, email, phoneNumber, true, getCitizenType(citizenType), null, null);
            Client registeredClient = clientService.register(client);

            if(registeredClient == null) {
                logger.warn("Client was not registered.");
            }

            if(filePart != null) {
                Image avatar = imageService.insertImage(registeredClient, filePart, req.getServletContext().getRealPath("/"));
                if(avatar == null) {
                    logger.warn("Failed to save avatar for client.");
                }
                logger.info("Avatar for client saved.");
            }

            logger.info("Client registered. ID: " + registeredClient.getId());
            navigationController.navigateToLogin(req, resp);
        }
        else {
            logger.warn("Registration failed. Passwords do not match.");
            // TODO: set error
            navigationController.navigateToLogin(req, resp);
        }
    }

    private CitizenType getCitizenType(String citizenType) {
        if(CitizenType.Local.toString().equalsIgnoreCase(citizenType)) {
            return CitizenType.Local;
        }
        return CitizenType.Foreigner;
    }
}
