package org.unibl.etf.clientapp.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NoArgsConstructor;
import org.unibl.etf.clientapp.util.Constants;
import org.unibl.etf.clientapp.util.CustomLogger;
import org.unibl.etf.clientapp.util.HelperClass;

import java.io.IOException;

@NoArgsConstructor
public class NavigationController {
    private final CustomLogger logger = CustomLogger.getInstance(NavigationController.class);

    // For redirects (changes URL)
    public void redirectToLogin(HttpServletRequest req, HttpServletResponse resp) {
        try {
            HttpSession session = req.getSession();
            session.setAttribute(Constants.SessionParameters.ERROR_MESSAGE, "You must sign in first to access that page.");
            resp.sendRedirect(req.getContextPath() + Constants.ServletURLs.LOGIN_URL);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    // For forwards (keeps URL, just renders the page)
    public void navigateToLogin(HttpServletRequest req, HttpServletResponse resp) {
        try{
            HelperClass.forwardResponse(Constants.Pages.LOGIN_PAGE, req, resp);
        } catch (IOException | ServletException e) {
            logger.error(e.getMessage());
        }
    }

    public void navigateToRegistration(HttpServletRequest req, HttpServletResponse resp){
        try{
            HelperClass.forwardResponse(Constants.Pages.REGISTER_PAGE, req, resp);
        } catch (IOException | ServletException e) {
            logger.error(e.getMessage());
        }
    }

    public void navigateToLogout(HttpServletRequest req, HttpServletResponse resp){
        try{
            HelperClass.forwardResponse(Constants.Pages.LOGOUT_PAGE, req, resp);
        } catch (IOException | ServletException e) {
            logger.error(e.getMessage());
        }
    }

    public void navigateToHome(HttpServletRequest req, HttpServletResponse resp){
        try{
            HelperClass.forwardResponse(Constants.Pages.HOME_PAGE, req, resp);
        } catch (IOException | ServletException e) {
            logger.error(e.getMessage());
        }
    }

    public void navigateToRent(HttpServletRequest req, HttpServletResponse resp){
        try{
            HelperClass.forwardResponse(Constants.Pages.RENT_PAGE, req, resp);
        } catch (IOException | ServletException e){
            logger.error(e.getMessage());
        }
    }

    public void navigateToProfile(HttpServletRequest req, HttpServletResponse resp){
        try{
            HelperClass.forwardResponse(Constants.Pages.PROFILE_PAGE, req, resp);
        } catch (IOException | ServletException e){
            logger.error(e.getMessage());
        }
    }

    public void navigateToDrive(HttpServletRequest req, HttpServletResponse resp){
        try{
            HelperClass.forwardResponse(Constants.Pages.DRIVE_PAGE, req, resp);
        } catch (IOException | ServletException e){
            logger.error(e.getMessage());
        }
    }
}
