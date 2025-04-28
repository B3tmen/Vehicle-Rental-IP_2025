package org.unibl.etf.clientapp.database.dao.interfaces;

import org.unibl.etf.clientapp.model.dto.Location;

import java.math.BigDecimal;
import java.sql.SQLException;

public interface LocationDAO {
    Location insert(Location location) throws SQLException;
    int update(Location location) throws SQLException;
    Location getById(int id) throws SQLException;
    Location getByLatitudeLongitude(BigDecimal latitude, BigDecimal longitude) throws SQLException;
}
