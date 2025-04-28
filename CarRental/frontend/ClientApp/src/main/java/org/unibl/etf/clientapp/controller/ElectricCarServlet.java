package org.unibl.etf.clientapp.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.unibl.etf.clientapp.factory.impl.MySqlServiceFactoryImpl;
import org.unibl.etf.clientapp.factory.interfaces.ServiceFactory;
import org.unibl.etf.clientapp.model.dto.ElectricCar;
import org.unibl.etf.clientapp.service.ElectricCarService;
import org.unibl.etf.clientapp.util.Constants;
import org.unibl.etf.clientapp.util.HelperClass;

import java.io.IOException;
import java.util.List;

import static org.unibl.etf.clientapp.util.Constants.ServletURLs.CARS_URL;

@WebServlet(CARS_URL)
public class ElectricCarServlet extends HttpServlet {
    private NavigationController navigationController;
    private ElectricCarService carService;

    public ElectricCarServlet(){
        ServiceFactory serviceFactory = new MySqlServiceFactoryImpl();
        this.navigationController = new NavigationController();
        this.carService = serviceFactory.getElectricCarService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!HelperClass.isClientAuthenticated(req, resp)){
            navigationController.redirectToLogin(req, resp);
            return;
        }

        List<ElectricCar> cars = carService.getAll();
        if(cars != null){
            req.setAttribute(Constants.SessionParameters.ELECTRIC_VEHICLES, cars);

            navigationController.navigateToHome(req, resp);
        }
    }
}
