package org.unibl.etf.clientapp.model.beans;

import lombok.Data;
import org.unibl.etf.clientapp.model.dto.Client;

import java.io.Serializable;

@Data
public class ClientBean implements Serializable {
    private Client client;
    private boolean isAuthenticated;

    public ClientBean() {
        this.client = new Client();
        isAuthenticated = false;
    }

    public ClientBean(Client client) {
        this.client = client;
        isAuthenticated = false;
    }

}
