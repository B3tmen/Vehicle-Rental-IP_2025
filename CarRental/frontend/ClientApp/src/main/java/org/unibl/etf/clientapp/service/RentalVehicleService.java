package org.unibl.etf.clientapp.service;

import org.unibl.etf.clientapp.database.dao.interfaces.RentalVehicleDAO;
import org.unibl.etf.clientapp.model.dto.RentalVehicle;

import java.sql.SQLException;

public class RentalVehicleService {
    private RentalVehicleDAO<RentalVehicle> dao;

    public RentalVehicleService(RentalVehicleDAO<RentalVehicle> dao) {
        this.dao = dao;
    }

    public int update(RentalVehicle rentalVehicle) {
        int updated = 0;
        try{
            updated = dao.update(rentalVehicle);
        } catch (Exception e){
            e.printStackTrace();
        }

        return updated;
    }
}
