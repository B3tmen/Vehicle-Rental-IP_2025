package org.unibl.etf.clientapp.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.unibl.etf.clientapp.database.dao.impl.*;
import org.unibl.etf.clientapp.factory.impl.MySqlServiceFactoryImpl;
import org.unibl.etf.clientapp.factory.interfaces.ServiceFactory;
import org.unibl.etf.clientapp.model.beans.ClientBean;
import org.unibl.etf.clientapp.model.beans.PaymentBean;
import org.unibl.etf.clientapp.model.beans.VehicleBean;
import org.unibl.etf.clientapp.model.dto.Payment;
import org.unibl.etf.clientapp.model.dto.RentalVehicle;
import org.unibl.etf.clientapp.model.enums.VehicleType;
import org.unibl.etf.clientapp.service.*;
import org.unibl.etf.clientapp.util.Constants;
import org.unibl.etf.clientapp.util.HelperClass;

import java.io.IOException;

@WebServlet(Constants.ServletURLs.RENT_URL)
public class RentServlet extends HttpServlet {
    private NavigationController navigationController;
    private PaymentService paymentService;

    public RentServlet() {
        ServiceFactory serviceFactory = new MySqlServiceFactoryImpl();
        navigationController = new NavigationController();
        paymentService = serviceFactory.getPaymentService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!HelperClass.isClientAuthenticated(req, resp)){
            navigationController.redirectToLogin(req, resp);
            return;
        }

        HttpSession session = req.getSession();
        int vehicleId = Integer.parseInt(req.getParameter(Constants.SessionParameters.VEHICLE_ID));
        String vehicleType = req.getParameter(Constants.SessionParameters.VEHICLE_TYPE);

        ClientBean clientBean = (ClientBean) session.getAttribute(Constants.SessionParameters.AUTHENTICATED_CLIENT);
        if(clientBean != null){
            Payment payment = paymentService.getByClient(clientBean.getClient());
            if(payment != null) {
                PaymentBean paymentBean = new PaymentBean(payment);
                session.setAttribute(Constants.SessionParameters.REMEMBERED_PAYMENT, paymentBean);
            }
        }
        RentalVehicle vehicle = getVehicleById(vehicleId, vehicleType);
        session.setAttribute(Constants.SessionParameters.SELECTED_VEHICLE_FOR_RENT, new VehicleBean(vehicle));

        navigationController.navigateToRent(req, resp);
    }

    private RentalVehicle getVehicleById(int vehicleId, String vehicleType){
        ServiceFactory serviceFactory = new MySqlServiceFactoryImpl();

        RentalVehicle vehicle = null;
        if(vehicleType.equals(VehicleType.Car.toString())){
            vehicle = serviceFactory.getElectricCarService().getById(vehicleId);
        }
        else if(vehicleType.equals(VehicleType.Bicycle.toString())){
            vehicle = serviceFactory.getElectricBicycleService().getById(vehicleId);
        }
        else{
            vehicle = serviceFactory.getElectricScooterService().getById(vehicleId);
        }

        return vehicle;
    }
}
