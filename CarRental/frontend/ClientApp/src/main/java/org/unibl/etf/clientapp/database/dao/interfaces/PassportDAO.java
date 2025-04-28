package org.unibl.etf.clientapp.database.dao.interfaces;

import org.unibl.etf.clientapp.model.dto.Passport;

import java.math.BigDecimal;
import java.sql.SQLException;

public interface PassportDAO {
    int insert(Passport passport, int clientId) throws SQLException;
}
