package org.unibl.etf.clientapp.database.dao.interfaces;

import org.unibl.etf.clientapp.model.dto.Client;
import org.unibl.etf.clientapp.model.dto.Payment;

import java.sql.SQLException;

public interface PaymentDAO {
    Payment insert(Payment payment) throws SQLException;
    Payment getByToken(String token) throws SQLException;
    Payment getByClient(Client client) throws SQLException;
    Payment getById(int id) throws SQLException;
}
