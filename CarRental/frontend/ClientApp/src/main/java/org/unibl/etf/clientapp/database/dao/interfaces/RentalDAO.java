package org.unibl.etf.clientapp.database.dao.interfaces;

import org.unibl.etf.clientapp.model.dto.Rental;

import java.sql.SQLException;
import java.util.List;

public interface RentalDAO {
    Rental insert(Rental rental) throws SQLException;
}
