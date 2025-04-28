<%@ page import="org.unibl.etf.clientapp.util.Constants" %><%--
  Created by IntelliJ IDEA.
  User: Ivan
  Date: 5. 3. 2025.
  Time: 20:55
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="client" value="${sessionScope.authClient.client}" />
<c:set var="avatarURL" value="${sessionScope.authClient.client.avatarImage.url}" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="${contextPath}/resources/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${contextPath}/resources/fontawesome/css/all.min.css">
    <link rel="icon" type="image/png" href="${contextPath}/resources/images/vehicle-rent-icon.png">
    <link rel="stylesheet" href="${contextPath}/styles/global.css">
    <link rel="stylesheet" href="${contextPath}/styles/profile.css">
    <title>Profile</title>
</head>
<body>
    <div>
        <jsp:include page="<%= Constants.Components.HEADER_COMPONENT %>" />
    </div>
    <div class="profile-container d-flex flex-column gap-5 p-3 w-100 border-top-0 rounded">
        <h3>My Profile</h3>

        <div class="profile-container__basic-info d-flex p-3 gap-4 rounded-2 w-100 border">
            <div class="profile-container__upper-section__avatar">
                <img src="${avatarURL}" alt="avatar" class="rounded-circle" style="width: 150px; height: 150px">
            </div>
            <div class="profile-container__upper-section__right d-flex flex-column">
                <div class="d-flex justify-content-between">
                    <div class="d-flex flex-column">
                        <h6>${client.firstName} ${client.lastName}</h6>
                        <p class="custom-paragraph">Client</p>
                    </div>

                    <div class="button-container d-flex gap-3">
                        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#customModal" onclick="loadModalBody()">
                            <i class="fas fa-pen"></i> Edit
                        </button>
                        <button class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#confirmModal">
                            <i class="fas fa-ban"></i> Deactivate
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <div class="profile-container__personal-info d-flex flex-column justify-content-start p-3 rounded-2 gap-4 w-100 border">
            <h4>
                Personal information <i class="fas fa-user" style="color: var(--color-secondary)"></i>
            </h4>
            <div class="p-3">
                <div class="row">
                    <div class="col">
                        <h6 class="row">First Name</h6>
                        <p class="custom-paragraph row">${client.firstName}</p>
                    </div>
                    <div class="col">
                        <h6 class="row">Last Name</h6>
                        <p class="custom-paragraph row">${client.lastName}</p>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <h6 class="row">Email</h6>
                        <p class="custom-paragraph row">${client.email}</p>
                    </div>
                    <div class="col">
                        <h6 class="row">Phone number</h6>
                        <p class="custom-paragraph row">${client.phoneNumber}</p>
                    </div>
                </div>
                <div class="row">
                    <div class="col">
                        <h6 class="row">Personal ID</h6>
                        <p class="custom-paragraph row">${client.personalCardNumber}</p>
                    </div>
                </div>
            </div>
        </div>

        <div class="profile-container__personal-info d-flex flex-column justify-content-start p-3 rounded-2 gap-4 w-100 border">
            <h4>
                My rentals <i class="fas fa-tag" style="color: var(--color-golden)"></i>
            </h4>

            <jsp:include page="<%=Constants.Components.PROFILE_RENTAL_TABLE_COMPONENT%>">
                <jsp:param name="tableClass" value="table-sm"/>
            </jsp:include>
        </div>
    </div>

    <jsp:include page="<%= Constants.Components.DATA_MODAL_COMPONENT %>" />
    <jsp:include page="<%= Constants.Components.CONFIRM_MODAL_COMPONENT %>">
        <jsp:param name="bodyIcon" value="fas fa-warning" />
        <jsp:param name="modalTitle" value="Deactivation confirmation"/>
        <jsp:param name="modalBody" value="You are about to deactivate your account, you can not undo this action. Are you sure you want to do this?"/>
    </jsp:include>

    <script src="${contextPath}/resources/bootstrap/js/jquery-3.7.1.min.js"></script>
    <script src="${contextPath}/resources/bootstrap/js/bootstrap.min.js"></script>
    <script src="${contextPath}/scripts/profile.js"></script>
</body>
</html>
