package org.unibl.etf.clientapp.database.dao.interfaces;

import org.unibl.etf.clientapp.model.dto.RentalVehicle;

import java.sql.SQLException;
import java.util.List;

public interface RentalVehicleDAO<T extends RentalVehicle> {
    List<T> getAll() throws SQLException;
    T getById(int id) throws SQLException;
    int update(RentalVehicle vehicle) throws SQLException;
}
