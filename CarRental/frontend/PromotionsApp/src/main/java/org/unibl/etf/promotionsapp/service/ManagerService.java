package org.unibl.etf.promotionsapp.service;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import org.unibl.etf.promotionsapp.model.dto.AuthenticationRequest;
import org.unibl.etf.promotionsapp.model.dto.AuthenticationResponse;

import java.io.*;
import java.net.URISyntaxException;

import static org.unibl.etf.promotionsapp.util.Constants.API_LOGIN_URL;

public class ManagerService {
    private final HttpService httpService;
    private final Gson gson;

    public ManagerService() {
        this.httpService = new HttpService();
        this.gson = new Gson();
    }

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        System.out.println("auth req: " + authenticationRequest);
        AuthenticationResponse authenticationResponse = null;

        try{
            authenticationResponse = httpService.post(API_LOGIN_URL, authenticationRequest, AuthenticationResponse.class);
            System.out.println(authenticationResponse);

            if(authenticationResponse != null)
                httpService.setAuthToken(authenticationResponse.getJwtToken());

        } catch (IOException | URISyntaxException e){
            e.printStackTrace();
        }
        finally {
            httpService.closeEverything();
        }

        return authenticationResponse;
    }

    private String getJson(AuthenticationRequest authenticationRequest){
        return gson.toJson(authenticationRequest);
    }
}
