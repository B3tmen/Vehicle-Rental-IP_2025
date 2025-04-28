package org.unibl.etf.clientapp.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.unibl.etf.clientapp.factory.impl.MySqlServiceFactoryImpl;
import org.unibl.etf.clientapp.factory.interfaces.ServiceFactory;
import org.unibl.etf.clientapp.model.dto.ElectricScooter;
import org.unibl.etf.clientapp.service.ElectricScooterService;
import org.unibl.etf.clientapp.util.Constants;
import org.unibl.etf.clientapp.util.HelperClass;

import java.io.IOException;
import java.util.List;

import static org.unibl.etf.clientapp.util.Constants.ServletURLs.SCOOTERS_URL;

@WebServlet(SCOOTERS_URL)
public class ElectricScooterServlet extends HttpServlet {
    private NavigationController navigationController;
    private ElectricScooterService scooterService;

    public ElectricScooterServlet(){
        ServiceFactory serviceFactory = new MySqlServiceFactoryImpl();
        this.navigationController = new NavigationController();
        this.scooterService = serviceFactory.getElectricScooterService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!HelperClass.isClientAuthenticated(req, resp)){
            navigationController.redirectToLogin(req, resp);
            return;
        }

        List<ElectricScooter> scooters = scooterService.getAll();
        if(scooters != null){
            req.setAttribute(Constants.SessionParameters.ELECTRIC_VEHICLES, scooters);

            navigationController.navigateToHome(req, resp);
        }
    }
}
