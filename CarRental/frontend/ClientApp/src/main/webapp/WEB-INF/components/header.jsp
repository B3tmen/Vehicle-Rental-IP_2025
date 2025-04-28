<%@ page import="org.unibl.etf.clientapp.util.Constants" %><%--
  Created by IntelliJ IDEA.
  User: Ivan
  Date: 1. 3. 2025.
  Time: 11:06
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="logoutUrl" value="${contextPath}/logout" />
<c:set var="avatarURL" value="${sessionScope.authClient.client.avatarImage.url}" />

<link rel="stylesheet" href="${contextPath}/resources/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/resources/fontawesome/css/all.min.css">
<link rel="stylesheet" href="${contextPath}/styles/header.css">

<%-- Form to call the car servlet --%>
<form id="carsForm" action="${contextPath}/cars" method="GET" style="display:none;">
</form>
<%-- Form to call the bicycles servlet --%>
<form id="bicyclesForm" action="${contextPath}/bicycles" method="GET" style="display:none;">
</form>
<%-- Form to call the scooter servlet --%>
<form id="scootersForm" action="${contextPath}/scooters" method="GET" style="display:none;">
</form>

<div class="header d-flex w-100">

    <%-- Left section, hamburger toggle button --%>
    <div class="header__toggle-section">
        <button class="navbar-toggler align-self-center" type="button" onclick="toggleMenu()">
            <i class="fas fa-bars"></i>
        </button>
    </div>

    <nav class="header__main-container w-100">
        <%-- Middle section, title and nav buttons --%>
        <div class="header__middle-section d-flex flex-column align-items-center w-100">
            <div class="header__middle-section__title d-flex justify-content-center gap-4 w-100">
                <h2 style="color: white; text-align: center">ETFBL_IP Vehicle Rentals</h2>
                <i class="fa-solid fa-car fa-3x" style="color: white"></i>
            </div>
            <div class="header__middle-section__navbar m-0 p-0">
                <!-- Navbar menu -->
                <div class="navbar-menu">
                    <button class="navbar-button" onclick="displayVehicles('scootersForm')">
                        <img src="${contextPath}/resources/images/electric-scooter.svg" alt="scooter" class="filter-white" style="width: 40px; height: 40px;" /> Scooters
                    </button>
                    <button class="navbar-button" onclick="displayVehicles('bicyclesForm')">
                        <img src="${contextPath}/resources/images/electric-bicycle.svg" alt="bicycle" class="filter-white" style="width: 40px; height: 40px;" /> Bicycles
                    </button>
                    <button class="navbar-button" onclick="displayVehicles('carsForm')">
                        <img src="${contextPath}/resources/images/electric-car.svg" alt="car" class="filter-white" style="width: 40px; height: 40px;" /> Cars
                    </button>
                </div>
            </div>
        </div>

        <%-- Right section, Profile and logout --%>
        <div class="header__profile-section d-flex flex-column gap-3">
            <div class="d-flex gap-1 align-items-center justify-content-center">
                <a href="${contextPath}/profile">
                    <img src="${avatarURL}" class="rounded-circle" style="width: 50px; height: 50px" alt="Avatar" />
                </a>

                <p class="align-self-center">Profile</p>
            </div>

            <button class="logout-button btn btn-link" onclick="logout(`${logoutUrl}`)">
                <i class="fa-solid fa-sign-out"></i> Logout
            </button>
        </div>
    </nav>
</div>


<script src="${contextPath}/scripts/header.js"></script>

