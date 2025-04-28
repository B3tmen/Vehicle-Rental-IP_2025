package org.unibl.etf.clientapp.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.unibl.etf.clientapp.exception.UserNotActiveException;
import org.unibl.etf.clientapp.factory.impl.MySqlServiceFactoryImpl;
import org.unibl.etf.clientapp.factory.interfaces.ServiceFactory;
import org.unibl.etf.clientapp.model.beans.ClientBean;
import org.unibl.etf.clientapp.model.dto.requests.AuthenticationRequest;
import org.unibl.etf.clientapp.service.ClientService;
import org.unibl.etf.clientapp.util.Constants;
import org.unibl.etf.clientapp.util.CustomLogger;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final CustomLogger logger = CustomLogger.getInstance(LoginServlet.class);
    private final NavigationController navigationController;
    private final ClientService clientService;

    public LoginServlet() {
        ServiceFactory serviceFactory = new MySqlServiceFactoryImpl();
        navigationController = new NavigationController();
        clientService = serviceFactory.getClientService();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        navigationController.navigateToLogin(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if (username == null || password == null) {     // e.g. just /login was entered, just go to the login page
            navigationController.navigateToLogin(req, resp);
            return;
        }

        AuthenticationRequest authRequest = new AuthenticationRequest(username, password);
        HttpSession session = req.getSession();
        String errorMessage = null;
        session.setAttribute(Constants.SessionParameters.ERROR_MESSAGE, errorMessage);      // default value

        ClientBean clientBean = null;
        boolean isActive = true;
        try{
            clientBean = clientService.login(authRequest);
        } catch (UserNotActiveException e) {
            isActive = false;
            logger.warn(e.getMessage());
        }

        if(clientBean == null){
            errorMessage = isActive ? "Invalid username/password. Please try again." : "This account has been deactivated. Please contact an Operator and try again.";
            session.setAttribute(Constants.SessionParameters.ERROR_MESSAGE, errorMessage);
            navigationController.navigateToLogin(req, resp);
            return;
        }

        System.out.println("AVATAR: " + clientBean.getClient().getAvatarImage().getUrl());

        session.setAttribute(Constants.SessionParameters.AUTHENTICATED_CLIENT, clientBean);
        navigationController.navigateToHome(req, resp);
    }
}
