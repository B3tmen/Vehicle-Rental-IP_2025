package org.unibl.etf.clientapp.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.unibl.etf.clientapp.database.dao.impl.RentalVehicleDAOImpl;
import org.unibl.etf.clientapp.database.dao.interfaces.RentalVehicleDAO;
import org.unibl.etf.clientapp.factory.impl.MySqlServiceFactoryImpl;
import org.unibl.etf.clientapp.factory.interfaces.ServiceFactory;
import org.unibl.etf.clientapp.model.beans.DriveBean;
import org.unibl.etf.clientapp.model.beans.VehicleBean;
import org.unibl.etf.clientapp.model.dto.MessageResponse;
import org.unibl.etf.clientapp.model.dto.RentalVehicle;
import org.unibl.etf.clientapp.model.enums.RentalStatusEnum;
import org.unibl.etf.clientapp.service.RentalVehicleService;
import org.unibl.etf.clientapp.util.Constants;
import org.unibl.etf.clientapp.util.HelperClass;

import java.io.IOException;
import java.math.BigDecimal;

import static org.unibl.etf.clientapp.util.Constants.ServletURLs.DRIVE_URL;

@WebServlet(DRIVE_URL)
public class DriveServlet extends HttpServlet {
    public NavigationController navigationController;
    private RentalVehicleService rentalVehicleService;

    public DriveServlet() {
        navigationController = new NavigationController();
        rentalVehicleService = new RentalVehicleService(new RentalVehicleDAOImpl());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!HelperClass.isClientAuthenticated(req, resp)){
            navigationController.redirectToLogin(req, resp);
            return;
        }

        HttpSession session = req.getSession();
        //DriveBean bean = new DriveBean(1, new BigDecimal("123"));
        //session.setAttribute(Constants.SessionParameters.DRIVE_BEAN, bean);
        Object driveBeanObject = session.getAttribute(Constants.SessionParameters.DRIVE_BEAN);
        if(driveBeanObject instanceof DriveBean) {
            session.setAttribute(Constants.SessionParameters.DRIVE_BEAN, driveBeanObject);
        }

        navigationController.navigateToDrive(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Object object = session.getAttribute(Constants.SessionParameters.SELECTED_VEHICLE_FOR_RENT);
        try{
            if(object != null) {
                VehicleBean vehicleBean = (VehicleBean) object;
                RentalVehicle vehicle = vehicleBean.getVehicle();
                System.out.println("UPDATING STATUS: " + vehicle.getRentalStatus());

                if(vehicle.getRentalStatus().getStatus().equals(RentalStatusEnum.Rented.toString())) {
                    vehicle.getRentalStatus().setId(RentalStatusEnum.Free.ordinal() + 1);   // TODO: fetch status id from database
                    vehicle.getRentalStatus().setStatus(RentalStatusEnum.Free.toString());

                    int updated = rentalVehicleService.update(vehicle);
                    if(updated > 0) {
                        resp.setStatus(HttpServletResponse.SC_OK);
                        session.removeAttribute(Constants.SessionParameters.SELECTED_VEHICLE_FOR_RENT);
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        HttpSession session = req.getSession();
        Object driveBeanObject = session.getAttribute(Constants.SessionParameters.DRIVE_BEAN);
        if(driveBeanObject != null) {
            Gson gson = new Gson();
            session.removeAttribute(Constants.SessionParameters.DRIVE_BEAN);
            resp.setStatus(HttpServletResponse.SC_OK);

            MessageResponse msg = new MessageResponse("Drive was ended successfully.");
            resp.getWriter().write(gson.toJson(msg));
        }
    }
}
