package org.unibl.etf.clientapp.database.dao.interfaces;

import org.unibl.etf.clientapp.model.dto.Image;
import org.unibl.etf.clientapp.model.dto.RentalVehicle;

import java.sql.SQLException;

public interface VehicleImageDAO<T extends RentalVehicle> {
    Image getImageByEntity(T entity) throws SQLException;
}
