<%@ page import="org.unibl.etf.clientapp.util.Constants" %><%--
  Created by IntelliJ IDEA.
  User: Ivan
  Date: 26. 2. 2025.
  Time: 16:01
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
    <link href="${contextPath}/resources/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/resources/fontawesome/css/all.min.css" rel="stylesheet">ž
    <link rel="icon" type="image/png" href="${contextPath}/resources/images/vehicle-rent-icon.png">
    <link href="${contextPath}/styles/register.css" rel="stylesheet">
    <title>Registration</title>
</head>
<body>
    <%-- Background Video --%>
    <video class="video-background" autoplay muted loop>
        <source src="${contextPath}/resources/videos/electric-car_charging.mp4" type="video/mp4">
        Your browser does not support the video tag.
    </video>
    
    <%-- Transparent Overlay with Login Form --%>
    <div class="overlay">

        <%-- Registration form container --%>
        <div class="registration-form position-absolute top-50 start-50 translate-middle border rounded border-primary
            px-4 py-4 d-flex flex-column gap-5">
            <div class="registration-form__header d-flex flex-column align-items-center justify-content-center">
                <h2>Register</h2>
                <h4>Create an Account</h4>
            </div>

            <div class="registration-form__content d-flex flex-column gap-5">
                <%-- Register form --%>
                <form id="registerForm" class="d-flex gap-3 flex-column needs-validation"
                      method="POST" action="${contextPath}<%= Constants.ServletURLs.REGISTER_URL %>" enctype="multipart/form-data" novalidate>
                    <%-- Username --%>
                    <div class="form-group w-100">
                        <label for="username">Username <span class="required-field-star">*</span></label>
                        <input id="username" name="username" type="text" class="form-control" required>
                        <div class="invalid-feedback">
                            Please enter your username.
                        </div>
                    </div>

                    <%-- Password/Repeat password --%>
                    <div class="passwords-container d-flex gap-4 w-100">
                        <div class="form-group password-container w-100">
                            <label for="password">Password <span class="required-field-star">*</span></label>
                            <div class="password-container__input w-100">
                                <input id="password" type="password" name="password" class="form-control" minlength="8" required>
                                <i class="fas fa-eye" id="toggle-password"></i>
                                <div class="invalid-feedback">
                                    Your Password must be at least 8 characters long.
                                </div>
                            </div>
                        </div>
                        <div class="form-group password-container w-100">
                            <label for="confirm-password">Confirm password <span class="required-field-star">*</span></label>
                            <div class="password-container__input w-100">
                                <input id="confirm-password" type="password" name="confirm-password" class="form-control" minlength="8" required>
                                <i class="fas fa-eye trailing" id="toggle-confirm-password"></i>
                                <div class="invalid-feedback">
                                    Your Password must be at least 8 characters long.
                                </div>
                            </div>
                        </div>
                    </div>

                    <%-- First/Last name --%>
                    <div class="names-container d-flex gap-4 w-100">
                        <div class="form-group w-100">
                            <label for="firstName">First name <span class="required-field-star">*</span></label>
                            <input id="firstName" type="text" name="firstName" class="form-control" required>
                        </div>

                        <div class="form-group w-100">
                            <label for="lastName">Last name <span class="required-field-star">*</span></label>
                            <input id="lastName" type="text" name="lastName" class="form-control" required>
                        </div>
                    </div>

                    <%-- Personal card ID --%>
                    <div class="form-group">
                        <label for="personalCardNumber">Personal Card ID <span class="required-field-star">*</span></label>
                        <input id="personalCardNumber" type="text" name="personalCardNumber" class="form-control" minlength="13" maxlength="13" required>
                        <div class="invalid-feedback">
                            Your personal card number must be 13 characters long.
                        </div>
                    </div>

                    <%-- Phone number --%>
                    <div class="form-group">
                        <label for="phoneNumber">Phone number <span class="required-field-star">*</span></label>
                        <input id="phoneNumber" type="text" name="phoneNumber" class="form-control" required>
                        <div class="invalid-feedback">
                            Please enter your phone number.
                        </div>
                    </div>

                    <%-- Email --%>
                    <div class="form-group">
                        <label for="email">Email <span class="required-field-star">*</span></label>
                        <input id="email" type="email" name="email" class="form-control" required>
                        <div class="invalid-feedback">
                            Please enter your email.
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="citizenshipSelect">Citizenship <span class="required-field-star">*</span></label>
                        <select class="form-select" id="citizenshipSelect" name="citizenshipSelect" required>
                            <option selected disabled value="">Select a value</option>
                            <option value="local">Local</option>
                            <option value="foreigner">Foreigner</option>
                        </select>
                        <div class="invalid-feedback">
                            Please select your citizenship type.
                        </div>
                    </div>

                    <%-- Avatar --%>
                    <div>
                        <div class="d-flex justify-content-center mb-4">
                            <img id="selectedAvatar" src="https://mdbootstrap.com/img/Photos/Others/placeholder-avatar.jpg"
                                 class="rounded-circle" style="width: 200px; height: 200px; object-fit: cover;" alt="Avatar image" />
                        </div>
                        <div class="d-flex justify-content-center">
                            <div class="btn btn-primary btn-rounded">
                                <label class="form-label text-white m-1 pe-auto" for="avatarImageFile">Choose avatar</label>
                                <input type="file" id="avatarImageFile" name="avatarImageFile" class="form-control d-none"  onchange="uploadImage(event, 'selectedAvatar')" />
                            </div>
                        </div>
                    </div>
                </form>
                <%-- Register button --%>
                <button class="btn btn-primary w-100" onclick="registerUser()">
                    <i class="fa-solid fa-check"></i> Register
                </button>

                <h4 class="align-self-center">Already have an Account?
                    <span><a class="pe-auto" href="${contextPath}<%= Constants.ServletURLs.LOGIN_URL %>">Login</a></span>
                </h4>
            </div>
        </div>
    </div>

    <script type="text/javascript" src="${contextPath}/scripts/authentication.js"></script>
</body>
</html>
