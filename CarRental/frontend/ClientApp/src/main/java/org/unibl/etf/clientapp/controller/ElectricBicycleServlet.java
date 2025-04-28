package org.unibl.etf.clientapp.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.unibl.etf.clientapp.factory.impl.MySqlServiceFactoryImpl;
import org.unibl.etf.clientapp.factory.interfaces.ServiceFactory;
import org.unibl.etf.clientapp.model.dto.ElectricBicycle;
import org.unibl.etf.clientapp.service.ElectricBicycleService;
import org.unibl.etf.clientapp.util.Constants;
import org.unibl.etf.clientapp.util.HelperClass;

import java.io.IOException;
import java.util.List;

import static org.unibl.etf.clientapp.util.Constants.ServletURLs.BICYCLES_URL;

@WebServlet(BICYCLES_URL)
public class ElectricBicycleServlet extends HttpServlet {
    private NavigationController navigationController;
    private ElectricBicycleService bicycleService;

    public ElectricBicycleServlet(){
        ServiceFactory serviceFactory = new MySqlServiceFactoryImpl();
        this.navigationController = new NavigationController();
        this.bicycleService = serviceFactory.getElectricBicycleService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!HelperClass.isClientAuthenticated(req, resp)){
            navigationController.redirectToLogin(req, resp);
            return;
        }

        List<ElectricBicycle> bicycles = bicycleService.getAll();
        if(bicycles != null){
            req.setAttribute(Constants.SessionParameters.ELECTRIC_VEHICLES, bicycles);

            navigationController.navigateToHome(req, resp);
        }
    }
}
