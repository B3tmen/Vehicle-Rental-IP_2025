package org.unibl.etf.promotionsapp.model.beans;

import lombok.Data;
import org.unibl.etf.promotionsapp.model.dto.AuthenticationRequest;
import org.unibl.etf.promotionsapp.model.dto.AuthenticationResponse;
import org.unibl.etf.promotionsapp.model.dto.Manager;
import org.unibl.etf.promotionsapp.service.ManagerService;

import java.io.IOException;
import java.io.Serializable;

@Data
public class ManagerBean implements Serializable {
    private Manager manager;
    private ManagerService managerService;
    private String authToken;
    private boolean isAuthenticated;

    public ManagerBean(){
        this.managerService = new ManagerService();
        this.authToken = "";
    }

    public boolean login(AuthenticationRequest authenticationRequest){
        AuthenticationResponse authenticationResponse = managerService.login(authenticationRequest);

        if(authenticationResponse != null){
            this.manager = authenticationResponse.getUser();
            this.authToken = authenticationResponse.getJwtToken();
            isAuthenticated = true;
            return true;
        }
        return false;
    }

    public void logout() {
        this.authToken = "";
        this.isAuthenticated = false;
    }
}
