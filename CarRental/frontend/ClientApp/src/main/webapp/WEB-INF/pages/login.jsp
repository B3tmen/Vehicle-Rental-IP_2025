<%@ page import="org.unibl.etf.clientapp.util.Constants" %><%--
  Created by IntelliJ IDEA.
  User: Ivan
  Date: 25. 2. 2025.
  Time: 21:41
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="${contextPath}/resources/bootstrap/css/bootstrap.min.css" >
    <link rel="stylesheet" href="${contextPath}/resources/fontawesome/css/all.min.css" >
    <link rel="stylesheet" href="${contextPath}/styles/login.css" >
    <title>Login</title>
</head>
<body>
    <!-- Background Video -->
    <video class="video-background" autoplay muted loop>
        <source src="${contextPath}/resources/videos/electric-car_charging.mp4" type="video/mp4">
        Your browser does not support the video tag.
    </video>

    <!-- Transparent Overlay with Login Form -->
    <div class="overlay">
        <div class="login-form position-absolute top-50 start-50 translate-middle border rounded border-primary
        px-4 py-4 d-flex flex-column gap-5">
            <div class="login-form__header d-flex flex-column align-items-center justify-content-center">
                <i class="car-logo fa-solid fa-car fa-4x"></i>
                <h1>ETFBL_IP</h1>
                <h2>Vehicle Rental - Sign in</h2>


                <%-- If incorrect username/password, display error msg (set in controller/LoginServlet) --%>
                <c:if test="${not empty sessionScope.errorMessage}">
                    <div class="alert alert-danger mt-3">
                        <strong>Error!</strong>&nbsp;${sessionScope.errorMessage}
                    </div>
                </c:if>
            </div>

            <div class="login-form__content d-flex flex-column gap-5">
                <form class="d-flex flex-column gap-3" action="${contextPath}/login" method="POST">
                    <div class="form-group">
                        <label for="username">Username</label>
                        <input id="username" type="text" class="form-control" name="username" required>
                    </div>
                    <div class="form-group password-container">
                        <label for="password">Password</label>
                        <div class="password-container__input">
                            <input type="password" class="form-control" id="password" name="password" required>
                            <i class="fas fa-eye" id="toggle-password"></i>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-primary w-100">
                        <i class="fa-solid fa-sign-in"></i> Login
                    </button>

                    <div class="d-flex justify-content-center align-items-center w-100">
                        <h4>Don't have an Account?
                            <span><a class="pe-auto" href="${contextPath}/register">Register</a></span>
                        </h4>
                    </div>

                </form>

            </div>
        </div>
    </div>

    <script type="text/javascript" src="${contextPath}/scripts/authentication.js"></script>
</body>
</html>
