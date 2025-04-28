package org.unibl.etf.clientapp.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.unibl.etf.clientapp.factory.impl.MySqlServiceFactoryImpl;
import org.unibl.etf.clientapp.factory.interfaces.ServiceFactory;
import org.unibl.etf.clientapp.model.beans.ClientBean;
import org.unibl.etf.clientapp.model.dto.requests.AuthenticationRequest;
import org.unibl.etf.clientapp.service.ClientService;
import org.unibl.etf.clientapp.util.Constants;
import org.unibl.etf.clientapp.util.CustomLogger;
import org.unibl.etf.clientapp.util.HelperClass;

import java.io.IOException;

import static org.unibl.etf.clientapp.util.Constants.ServletURLs.HOME_URL;

@WebServlet(HOME_URL)
public class HomeServlet extends HttpServlet {
    private final CustomLogger logger = CustomLogger.getInstance(HomeServlet.class);
    private final NavigationController navigationController;
    private final ClientService clientService;

    public HomeServlet(){
        ServiceFactory serviceFactory = new MySqlServiceFactoryImpl();

        this.navigationController = new NavigationController();
        this.clientService = serviceFactory.getClientService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!HelperClass.isClientAuthenticated(req, resp)){
            navigationController.redirectToLogin(req, resp);
            return;
        }

        AuthenticationRequest authRequest = new AuthenticationRequest("username1", "bbb");
        HttpSession session = req.getSession();

        ClientBean clientBean = clientService.login(authRequest);
        if (clientBean != null) {
            session.setAttribute(Constants.SessionParameters.AUTHENTICATED_CLIENT, clientBean);
            logger.info("Client " + clientBean.getClient().getUsername() + ", ID: " + clientBean.getClient().getId() + ", has logged in.");
            navigationController.navigateToHome(req, resp);
        }
//        Object object = session.getAttribute(Constants.SessionParameters.AUTHENTICATED_CLIENT);
//        if(object != null) {
//            ClientBean clientBean = (ClientBean) object;
//
//            logger.info("Client " + clientBean.getClient().getUsername() + ", ID: " + clientBean.getClient().getId() + ", has logged in.");
//            navigationController.navigateToHome(req, resp);
//        }

    }
}
