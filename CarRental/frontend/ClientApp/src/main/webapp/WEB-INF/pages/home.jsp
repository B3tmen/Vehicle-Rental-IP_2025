<%@ page import="org.unibl.etf.clientapp.util.Constants" %><%--
  Created by IntelliJ IDEA.
  User: Ivan
  Date: 1. 3. 2025.
  Time: 11:10
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
    <link rel="stylesheet" href="${contextPath}/styles/home-page.css">
    <link rel="stylesheet" href="${contextPath}/styles/global.css">
    <link rel="icon" type="image/png" href="${contextPath}/resources/images/vehicle-rent-icon.png">
    <title>Vehicle Rental</title>
</head>
<body>
    <div id="root" class="d-flex flex-column h-100 w-100">
        <div>
            <jsp:include page="<%=Constants.Components.HEADER_COMPONENT%>" />
        </div>

        <div class="d-flex flex-column justify-content-between vh-100">
            <div class="d-flex flex-wrap gap-5 p-5" >
                <c:forEach var="vehicle" items="${electricVehicles}">
                    <c:set var="specialAttribute" value="" />   <%-- We can't use c:if in jsp:include, only jsp:param. So this is a workaround --%>
                    <c:set var="vehicleType" value="" />
                    <c:set var="vehicleId" value="${vehicle.id}" />

                    <c:if test="${vehicle.runtimeVehicleType eq 'ElectricCar'}">
                        <c:set var="vehicleType" value="Car" />
                        <c:set var="specialAttribute" value="${vehicle.purchaseDate}" />
                    </c:if>
                    <c:if test="${vehicle.runtimeVehicleType eq 'ElectricBicycle'}">
                        <c:set var="vehicleType" value="Bicycle" />
                        <c:set var="specialAttribute" value="${vehicle.ridingAutonomy} km" />
                    </c:if>
                    <c:if test="${vehicle.runtimeVehicleType eq 'ElectricScooter'}">
                        <c:set var="vehicleType" value="Scooter" />
                        <c:set var="specialAttribute" value="${vehicle.maxSpeed} km/h" />
                    </c:if>

                    <jsp:include page="<%=Constants.Components.CARD_COMPONENT%>">
                        <jsp:param name="vehicleImageURL" value="${vehicle.image.url}" />
                        <jsp:param name="vehicleId" value="${vehicleId}" />
                        <jsp:param name="vehicleType" value="${vehicleType}" />
                        <jsp:param name="rentalStatus" value="${vehicle.rentalStatus.status}" />
                        <jsp:param name="model" value="${vehicle.model}" />
                        <jsp:param name="rentalPrice" value="${vehicle.rentalPrice}" />
                        <jsp:param name="specialAttribute" value="${specialAttribute}" />
                    </jsp:include>
                </c:forEach>
            </div>

            <div>
                <jsp:include page="<%=Constants.Components.FOOTER_COMPONENT%>" />
            </div>
        </div>
    </div>

    <c:if test="${empty electricVehicles}">
        <script>
            window.onload = function() {
                document.getElementById('scootersForm').submit();
            };
        </script>
    </c:if>
</body>
</html>
