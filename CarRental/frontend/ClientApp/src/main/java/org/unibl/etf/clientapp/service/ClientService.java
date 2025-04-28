package org.unibl.etf.clientapp.service;

import jakarta.servlet.http.Part;
import org.unibl.etf.clientapp.database.dao.interfaces.ClientDAO;
import org.unibl.etf.clientapp.exception.UserNotActiveException;
import org.unibl.etf.clientapp.model.beans.ClientBean;
import org.unibl.etf.clientapp.model.dto.requests.AuthenticationRequest;
import org.unibl.etf.clientapp.model.dto.requests.ChangePasswordRequest;
import org.unibl.etf.clientapp.model.dto.Client;
import org.unibl.etf.clientapp.util.CustomLogger;
import org.unibl.etf.clientapp.util.HelperClass;

import java.sql.SQLException;

public class ClientService {
    private final CustomLogger logger = CustomLogger.getInstance(ClientService.class);
    private ClientDAO dao;

    public ClientService(ClientDAO dao) {
        this.dao = dao;
    }

    public ClientBean login(AuthenticationRequest request) throws UserNotActiveException {
        Client client = null;
        try{
            client = dao.login(request);
        } catch (SQLException e){
            e.printStackTrace();
        }

        ClientBean bean = null;
        if(client != null){
            if(client.getIsActive()){
                bean = new ClientBean(client);
                bean.setAuthenticated(true);
            }
            else{
                throw new UserNotActiveException();
            }
        }

        return bean;
    }

    public Client register(Client client){
        Client registeredClient = null;
        try{
            registeredClient = dao.register(client);
        } catch (SQLException e){
            logger.error(e.getMessage());
        }

        return registeredClient;
    }

    public Client changePassword(Client client, ChangePasswordRequest passwordRequest){
        String oldPassword = passwordRequest.getOldPassword();
        String newPassword = passwordRequest.getNewPassword();

        Client updatedClient = null;
        boolean verified = HelperClass.verifyPassword(oldPassword, client.getPasswordHash());
        if(verified){
            try{
                String newPasswordHash = HelperClass.getBCryptHash(newPassword);
                updatedClient = dao.changePassword(client, newPasswordHash);
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }

        return updatedClient;
    }

    public int deactivate(int id) {
        int deactivated = 0;
        try{
            deactivated = dao.deactivate(id);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return deactivated;
    }

    public void logout(ClientBean bean) {
        bean.setAuthenticated(false);
        bean.setClient(null);
    }

}
