package org.unibl.etf.clientapp.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.unibl.etf.clientapp.database.dao.impl.ClientDAOImpl;
import org.unibl.etf.clientapp.factory.impl.MySqlServiceFactoryImpl;
import org.unibl.etf.clientapp.factory.interfaces.ServiceFactory;
import org.unibl.etf.clientapp.model.beans.ClientBean;
import org.unibl.etf.clientapp.service.ClientService;
import org.unibl.etf.clientapp.util.Constants;

import java.io.IOException;

import static org.unibl.etf.clientapp.util.Constants.ServletURLs.LOGOUT_URL;

@WebServlet(LOGOUT_URL)
public class LogoutServlet extends HttpServlet {
    private final NavigationController navigationController;
    private final ClientService clientService;

    public LogoutServlet(){
        ServiceFactory serviceFactory = new MySqlServiceFactoryImpl();
        navigationController = new NavigationController();
        clientService = serviceFactory.getClientService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        Object bean = session.getAttribute(Constants.SessionParameters.AUTHENTICATED_CLIENT);
        if(bean == null){
            navigationController.navigateToLogin(req, resp);
            return;
        }
        ClientBean clientBean = (ClientBean) bean;
        clientService.logout(clientBean);
        session.invalidate();

        navigationController.navigateToLogout(req, resp);
    }
}
