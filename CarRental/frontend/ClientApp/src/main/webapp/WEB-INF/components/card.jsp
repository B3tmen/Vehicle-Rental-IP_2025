<%@ page import="org.unibl.etf.clientapp.util.Constants" %><%--
  Created by IntelliJ IDEA.
  User: Ivan
  Date: 1. 3. 2025.
  Time: 16:39
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="rentUrl" value="/rent" />
<c:set var="specialAttributeHeader" value="" />
<c:set var="tagClass" value="" />
<c:set var="disabledClass" value="" />

<c:if test="${param.rentalStatus eq 'Free'}">
    <c:set var="tagClass" value="text-bg-success" />
</c:if>
<c:if test="${param.rentalStatus eq 'Broken'}">
    <c:set var="tagClass" value="text-bg-danger" />
    <c:set var="disabledClass" value="disabled" />
</c:if>
<c:if test="${param.rentalStatus eq 'Rented'}">
    <c:set var="tagClass" value="text-bg-warning" />
    <c:set var="disabledClass" value="disabled" />
</c:if>

<c:if test="${param.vehicleType eq 'Car'}">
    <c:set var="specialAttributeIcon" value="fas fa-calendar-alt" />
    <c:set var="specialAttributeHeader" value="Purchase date:" />
</c:if>
<c:if test="${param.vehicleType eq 'Bicycle'}">
    <c:set var="specialAttributeIcon" value="fas fa-battery-full" />
    <c:set var="specialAttributeHeader" value="Riding autonomy:" />
</c:if>
<c:if test="${param.vehicleType eq 'Scooter'}">
    <c:set var="specialAttributeIcon" value="fas fa-tachometer-alt" />
    <c:set var="specialAttributeHeader" value="Max speed:" />
</c:if>

<link rel="stylesheet" href="${contextPath}/resources/fontawesome/css/all.min.css">
<link rel="stylesheet" href="${contextPath}/resources/bootstrap/css/bootstrap.min.css">
<style>
    .card-body__lower-section__special-attribute i {
        margin-bottom: 7px;
    }
</style>
<div class="card" style="width: 250px; height: 450px;">
    <img class="card-img-top" src="${param.vehicleImageURL}" alt="Vehicle" style="width: 100%; height: 200px">
    <div class="card-body">
        <div class="card-body__upper-section d-flex flex-column gap-3">
            <div class="card-body__upper-section__name">
                <h5>${param.model}</h5>
                <span class="badge rounded-pill ${tagClass}">${param.rentalStatus}</span>
            </div>
            <div class="card-body__upper-section__rent d-flex justify-content-between align-items-center">
                <form id="rentForm" action="${contextPath}<%=Constants.ServletURLs.RENT_URL%>" method="GET" target="_blank">
                    <input type="hidden" name="<%=Constants.SessionParameters.VEHICLE_ID%>" value="${param.vehicleId}">
                    <input type="hidden" name="<%=Constants.SessionParameters.VEHICLE_TYPE%>" value="${param.vehicleType}">
                    <button class="btn btn-link" type="submit" ${disabledClass} >Rent</button>
                </form>

                <h6>$${param.rentalPrice}/hr</h6>
            </div>
        </div>
        <hr />
        <div class="card-body__lower-section d-flex flex-column">
            <div class="card-body__lower-section__special-attribute d-flex gap-3 align-items-center" >
                <i class="${specialAttributeIcon}"></i>
                <h6>${specialAttributeHeader} ${param.specialAttribute}</h6>
            </div>
        </div>
    </div>
</div>