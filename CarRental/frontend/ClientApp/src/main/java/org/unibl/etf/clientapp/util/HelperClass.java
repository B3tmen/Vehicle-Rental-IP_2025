package org.unibl.etf.clientapp.util;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.unibl.etf.clientapp.model.beans.ClientBean;
import org.unibl.etf.clientapp.model.dto.Client;

import java.io.IOException;

public class HelperClass {
    private static final ConfigReader configReader = ConfigReader.getInstance();
    private static final int BCRYPT_STRENGTH = configReader.getBCryptPasswordStrength();

    public static void forwardResponse(String page, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher(page);
        if(rd != null) {
            rd.forward(request, response);
        }
    }

    public static boolean isClientAuthenticated(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object object = session.getAttribute(Constants.SessionParameters.AUTHENTICATED_CLIENT);
        if(object == null){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        ClientBean clientBean = (ClientBean) object;
        if(!clientBean.isAuthenticated()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }

    public static boolean verifyPassword(String plainPassword, String passwordHash){
        return BCrypt.verifyer().verify(plainPassword.toCharArray(), passwordHash).verified;
    }

    public static String getBCryptHash(String text) {
        return BCrypt.withDefaults().hashToString(BCRYPT_STRENGTH, text.toCharArray());
    }
}
