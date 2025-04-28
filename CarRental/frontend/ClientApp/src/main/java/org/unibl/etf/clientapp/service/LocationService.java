package org.unibl.etf.clientapp.service;

import org.unibl.etf.clientapp.database.dao.interfaces.LocationDAO;
import org.unibl.etf.clientapp.model.dto.Location;

import java.math.BigDecimal;
import java.sql.SQLException;

public class LocationService {
    private LocationDAO dao;

    public LocationService(LocationDAO dao) {
        this.dao = dao;
    }

    public Location insert(Location location) {
        Location insertedLocation = null;
        try{
            insertedLocation = dao.insert(location);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return insertedLocation;
    }

    public int update(Location location) {
        int updated = 0;
        try{
            updated = dao.update(location);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return updated;
    }

    public Location getById(int id) {
        Location location = null;
        try{
            location = dao.getById(id);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return location;
    }

    public Location getByLatitudeLongitude(BigDecimal latitude, BigDecimal longitude) {
        Location location = null;
        try{
            location = dao.getByLatitudeLongitude(latitude, longitude);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return location;
    }
}
