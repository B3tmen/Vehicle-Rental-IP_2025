<%@ page import="org.unibl.etf.promotionsapp.model.beans.ManagerBean" %>
<%@ page import="org.unibl.etf.promotionsapp.model.dto.AuthenticationRequest" %>
<%@ page import="org.unibl.etf.promotionsapp.util.Constants" %><%--
  Created by IntelliJ IDEA.
  User: Ivan
  Date: 9. 2. 2025.
  Time: 11:09
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<%
    String errorMessage = (String) session.getAttribute(Constants.SessionAttributes.ERROR_MESSAGE);

    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String logoutParameter = request.getParameter("logoutParameter");
    if(username != null && password != null){
        AuthenticationRequest authRequest = new AuthenticationRequest(username, password);
        ManagerBean managerBean = new ManagerBean();
        System.out.println("PING LOGIN");
        if(managerBean.login(authRequest)){
            session.setAttribute(Constants.SessionAttributes.MANAGER_BEAN, managerBean);
            session.setAttribute(Constants.SessionAttributes.AUTH_TOKEN, managerBean.getAuthToken());

            response.sendRedirect(Constants.Pages.INDEX_PAGE);
        }
        else{
            errorMessage = "Invalid username/password. Please try again.";
            session.setAttribute(Constants.SessionAttributes.ERROR_MESSAGE, errorMessage);
            response.sendRedirect(Constants.Pages.LOGIN_PAGE);
        }
    }

    if("logout".equals(logoutParameter)){
        ManagerBean managerBean = (ManagerBean) session.getAttribute(Constants.SessionAttributes.MANAGER_BEAN);
        managerBean.logout();
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/fontawesome/css/all.min.css" >
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/login.css">
    <title>Promotions & Announcements</title>
</head>
<body>
<div class="login">
    <div class="login__container">
        <header class="login__header">
            <h1 class="login__title">Login</h1>
        </header>

        <%
            if(errorMessage != null && !errorMessage.isEmpty()){
        %>
        <div class="alert alert-danger mt-3">
            <strong>Error!</strong>&nbsp;<%= errorMessage %>
        </div>
        <%
            }
        %>

        <form class="login__form" method="POST">
            <div class="input-group d-flex gap-3">
                <div class="input-group__title">
                    <i class="fas fa-user"></i>
                    <label class="label">Username</label>
                </div>
                <input autocomplete="off" name="username" id="username" class="input form-control" type="text" required>
            </div>
            <div class="input-group d-flex gap-3">
                <div class="input-group__title">
                    <i class="fas fa-lock"></i>
                    <label class="label">Password</label>
                </div>
                <input autocomplete="off" name="password" id="password" class="input form-control" type="password" required>
            </div>

            <button class="login-button">
                <i class="fa-solid fa-sign-in"></i>
                Login
            </button>
        </form>
    </div>
</div>
</body>
</html>
