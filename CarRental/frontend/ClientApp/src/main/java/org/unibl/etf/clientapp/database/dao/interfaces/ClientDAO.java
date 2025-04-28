package org.unibl.etf.clientapp.database.dao.interfaces;

import org.unibl.etf.clientapp.model.dto.requests.AuthenticationRequest;
import org.unibl.etf.clientapp.model.dto.Client;

import java.sql.SQLException;

public interface ClientDAO {
    Client login(AuthenticationRequest authenticationRequest) throws SQLException;
    Client register(Client client) throws SQLException;
    Client changePassword(Client client, String newPasswordHash) throws SQLException;
    Client getById(int id) throws SQLException;
    int deactivate(int id) throws SQLException;
}
