<%@ page import="org.unibl.etf.clientapp.util.Constants" %>
<%@ page import="static org.unibl.etf.clientapp.util.Constants.Components.CUSTOM_TOAST_COMPONENT" %>
<%--
  Created by IntelliJ IDEA.
  User: Ivan
  Date: 3. 3. 2025.
  Time: 16:19
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="clientBean" value="${sessionScope.authClient}" />
<c:set var="citizenType" value="${clientBean.client.citizenType}" />
<c:set var="vehicle" value="${sessionScope.selectedVehicleForRent.vehicle}" />
<c:set var="rememberedPaymentBean" value="${sessionScope.rememberedPayment}" />
<c:set var="isReadOnly" value="${sessionScope.rememberedPayment != null ? 'readonly' : ''}"/>
<c:set var="hiddenCardNumber" value="<%=Constants.HIDDEN_CARD_NUMBER%>" />
<c:set var="specialAttributeHeader" value="" />
<c:set var="specialAttributeValue" value="" />
<c:set var="cardType" value="${rememberedPaymentBean.payment.type}" />
<c:set var="expiryMonth">
    <fmt:formatDate value="${rememberedPaymentBean.payment.expiryDate}" pattern="MM" />
</c:set>
<c:set var="expiryYear">
    <fmt:formatDate value="${rememberedPaymentBean.payment.expiryDate}" pattern="yyyy" />
</c:set>
<fmt:formatDate var="currentDate" value="<%= new java.util.Date() %>" pattern="yyyy-MM-dd" />
<fmt:formatDate var="currentYear" value="<%= new java.util.Date() %>" pattern="yyyy" />

<c:if test="${vehicle.runtimeVehicleType eq 'ElectricCar'}">
    <c:set var="specialAttributeHeader" value="Purchase date:" />
    <c:set var="specialAttributeValue" value="${vehicle.purchaseDate}" />
</c:if>
<c:if test="${vehicle.runtimeVehicleType eq 'ElectricBicycle'}">
    <c:set var="specialAttributeHeader" value="Riding autonomy:" />
    <c:set var="specialAttributeValue" value="${vehicle.ridingAutonomy}" />
</c:if>
<c:if test="${vehicle.runtimeVehicleType eq 'ElectricScooter'}">
    <c:set var="specialAttributeHeader" value="Max speed:" />
    <c:set var="specialAttributeValue" value="${vehicle.maxSpeed}" />
</c:if>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="${contextPath}/resources/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${contextPath}/resources/leaflet/leaflet.css" />
    <link rel="icon" type="image/png" href="${contextPath}/resources/images/vehicle-rent-icon.png">
    <link rel="stylesheet" href="${contextPath}/styles/rent.css">

    <title>Rent</title>
</head>
<body>
    <div id="root" class="d-flex flex-column vh-100 w-100">
        <div>
            <jsp:include page="<%=Constants.Components.HEADER_COMPONENT%>" />
        </div>

        <div class="rent-container "> <%-- content d-flex justify-content-between gap-3 w-100 h-100 p-5 --%>
            <div class="rent-container__vehicle-info" >
                <div class="rent-container__vehicle-info__image">
                    <img class="align-self-center" src="${vehicle.image.url}" alt="Vehicle" >
                </div>

                <div class="rent-container__vehicle-info__info">
                    <h4>Manufacturer: ${vehicle.manufacturer.name}</h4>
                    <h5>Model: ${vehicle.model}</h5>
                    <h6>Price: <span>$${vehicle.rentalPrice}/hr</span></h6>

                    <hr style="width: 90%; align-self: center"/>

                    <h6>${specialAttributeHeader} <span>${specialAttributeValue}</span></h6>
                </div>
            </div>


            <div class="rent-container__payment-container d-flex flex-column gap-3">
                <div class="rent-container__payment-container__location d-flex gap-5 align-items-center">
                    <h4>Starting location:</h4>
                    <div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="inlineRadioOptions" id="radioBtnByHandStart">
                            <label class="form-check-label" for="radioBtnByHandStart">By hand</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="inlineRadioOptions" id="radioBtnAutomaticStart">
                            <label class="form-check-label" for="radioBtnAutomaticStart">Automatic</label>
                        </div>
                    </div>
                </div>
                <div class="rent-container__payment-container__location d-flex gap-5 align-items-center">
                    <h4>Dropoff location:</h4>
                    <div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" name="inlineRadioOptions" id="radioBtnByHandEnd">
                            <label class="form-check-label" for="radioBtnByHandEnd">By hand</label>
                        </div>
                    </div>
                </div>

                <%-- Map container --%>
                <div class="rent-container__payment-container__map d-flex flex-column">
                    <h4>Your location:</h4>

                    <div id="map" style="height: 400px; position: relative;"></div>*
                </div>

                <%-- Citizenship container --%>
                <div class="rent-container__payment-container__citizenship">
                    <h4>Citizenship:</h4>

                    <div class="rent-container__payment-container__citizenship__info" id="citizenshipInfoDisplay">
                        <c:choose>
                            <c:when test="${citizenType eq 'Foreigner'}">
                                <%-- Foreigner citizen form --%>
                                <form id="foreignerForm" class="needs-validation" method="POST" action="${contextPath}<%=Constants.ServletURLs.PAYMENT_URL%>" novalidate>
                                    <h5>Enter your passport</h5>
                                    <div class="form-group has-validation">
                                        <label for="passportNumber">Passport number: </label>
                                        <input type="text" id="passportNumber" name="passportNumber" class="form-control" minlength="8" maxlength="8" required />
                                        <div class="invalid-feedback">
                                            Your passport number must be 8 characters long.
                                        </div>
                                    </div>

                                    <div class="form-group has-validation">
                                        <label for="country">Country: </label>
                                        <input type="text" id="country" name="country" class="form-control" required />
                                    </div>

                                    <div class="form-group has-validation">
                                        <label for="validFrom">Valid from: </label>
                                        <input type="date" id="validFrom" name="validFrom" class="form-control" max="${currentDate}" required />
                                    </div>

                                    <div class="form-group has-validation">
                                        <label for="validTo">Valid to: </label>
                                        <input type="date" id="validTo" name="validTo" class="form-control" min="${currentDate}" required />
                                    </div>

                                    <div class="form-group has-validation">
                                        <label for="driversLicenceForeigner">Driver's licence: </label>
                                        <input type="text" id="driversLicenceForeigner" name="driversLicenceForeigner" class="form-control" minlength="9" maxlength="9" required />
                                        <div class="invalid-feedback">
                                            Your driver's licence number must be 9 characters long.
                                        </div>
                                    </div>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <%-- Local citizen form --%>
                                <form id="localForm" class="needs-validation" method="POST" action="${contextPath}<%=Constants.ServletURLs.PAYMENT_URL%>" novalidate>
                                    <h5>Enter personal ID card</h5>

                                    <div class="form-group has-validation">
                                        <label for="personalCardNumber">ID: </label>
                                        <input type="text" id="personalCardNumber" name="personalCardNumber" class="form-control" minlength="13" maxlength="13" required />
                                        <div class="invalid-feedback">
                                            Your personal card number must be 13 characters long.
                                        </div>
                                    </div>

                                    <div class="form-group has-validation">
                                        <label for="driversLicenceLocal">Driver's licence: </label>
                                        <input type="text" id="driversLicenceLocal" name="driversLicenceLocal" class="form-control" minlength="9" maxlength="9" required />
                                        <div class="invalid-feedback">
                                            Your driver's licence number must be 9 characters long.
                                        </div>
                                    </div>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <%-- Payment container --%>
                <div class="rent-container__payment-container__payment">
                    <div class="custom-card-payment">
                        <span class="custom-title">Payment</span>
                        <div class="custom-card-payment__content">
                            <form id="paymentForm" class="custom-card-payment__content__payment-fields d-flex flex-column gap-4 needs-validation" method="POST"
                                  action="${contextPath}<%=Constants.ServletURLs.PAYMENT_URL%>" novalidate>
                                <input type="hidden" id="startLat" name="startLatitude">
                                <input type="hidden" id="startLng" name="startLongitude">
                                <input type="hidden" id="endLat" name="endLatitude">
                                <input type="hidden" id="endLng" name="endLongitude">

                                <%-- Rent duration --%>
                                <div class="form-group has-validation">
                                    <label for="rentDurationInput">Rent duration (hours)</label>
                                    <input class="form-control" id="rentDurationInput" type="number" name="rentDuration" maxlength="8" min="1" max="72" required>
                                    <div class="invalid-feedback">
                                        Your rental duration must be at least an hour and at most 72 hours long.
                                    </div>
                                </div>

                                <%-- Card type --%>
                                <div>
                                    <select id="paymentType" class="form-select" name="cardType" required>
                                        <c:choose>
                                            <c:when test="${rememberedPaymentBean != null}">
                                                <c:if test="${cardType == null}">
                                                    <option selected disabled hidden value="">Choose payment method</option>
                                                </c:if>
                                                <c:choose>
                                                    <c:when test="${cardType.equals('Visa')}">
                                                        <option selected value="visa">Visa</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option selected value="mastercard">MasterCard</option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>
                                            <c:otherwise>
                                                <option selected disabled value="">Choose payment method</option>
                                                <option selected value="visa">Visa</option>
                                                <option selected value="mastercard">MasterCard</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </select>
                                    <div class="invalid-feedback">
                                        Please select a valid card.
                                    </div>
                                </div>

                                <%-- Card number --%>
                                <div class="form-group has-validation">
                                    <label for="cardNumber">Card number</label>
                                    <input type="text" class="form-control" id="cardNumber" name="cardNumber" maxlength="16" minlength="16" required >
                                    <div class="valid-feedback">
                                        Looks good!
                                    </div>
                                    <div class="invalid-feedback">
                                        Your card number must be 16 characters long.
                                    </div>
                                </div>

                                <%-- Expiry date --%>
                                <div class="form-group">
                                    <label>Expiration date</label>
                                    <div class="d-flex gap-3">
                                        <select class="form-control" id="expDateMonth" name="expiryMonth" style="width: 40%"
                                        >
                                            <c:forEach var="i" begin="1" end="12">
                                                <option value="${i}" ${rememberedPaymentBean != null && expiryMonth  == i ? 'selected' : ''}>${i}</option>
                                            </c:forEach>
                                        </select>
                                        <select class="form-control" id="expDateYear" name="expiryYear"
                                        >
                                            <c:forEach var="i" begin="${currentYear}" end="${currentYear+10}">
                                                <option value="${i}" ${rememberedPaymentBean != null && expiryYear == i ? 'selected' : ''}>${i}</option>
                                            </c:forEach>
                                        </select>
                                        <%-- ${sessionScope.rememberedPayment != null ? 'disabled' : ''}
    ${sessionScope.rememberedPayment != null ? 'disabled' : ''} --%>
                                    </div>
                                </div>

                                <%-- CVV --%>
                                <div class="form-group w-25">
                                    <label for="cvv">CVV</label>
                                    <div class="d-flex gap-3 align-items-center justify-content-center">
                                        <div>
                                            <input type="text" class="form-control" id="cvv" name="cvv" maxlength="3" required
                                            >
                                            <div class="invalid-feedback">
                                                Please enter your CVV.
                                            </div>
                                        </div>
                                        <span tabindex="0"
                                              class="cvv-help"
                                              data-bs-toggle="popover"
                                              data-bs-trigger="hover focus"
                                              data-bs-placement="right"
                                              data-bs-html="true"
                                              title="What's a CVV?"
                                              data-bs-content="
                                          <div class='d-flex flex-column'>
                                            <img src='${contextPath}/resources/images/cvv-help.webp' alt='CVV Example' style='width:200px; height:200px;'>
                                          </div>">
                                    ?
                                    </span>
                                    </div>

                                </div>

                                <%-- first and last name --%>
                                <div class="d-flex gap-3 align-items-center">
                                    <div class="form-group w-50">
                                        <label for="firstName">First name</label>
                                        <input type="text" class="form-control" id="firstName" name="holderFirstName"
                                               value="${rememberedPaymentBean != null ? rememberedPaymentBean.payment.holderFirstName : ''}"
                                        ${isReadOnly} required>
                                    </div>
                                    <div class="form-group w-50">
                                        <label for="lastName">Last name</label>
                                        <input type="text" class="form-control" id="lastName" name="holderLastName"
                                               value="${rememberedPaymentBean != null ? rememberedPaymentBean.payment.holderLastName : ''}"
                                        ${isReadOnly} required>
                                    </div>
                                </div>
                            </form>
                            <div class="custom-card-payment__content__allowed-cards d-flex gap-4">
                                <img src="${contextPath}/resources/images/VISA-Logo.png" style="width: 40px; height: 40px;" alt="visa"> <!--  -->
                                <img src="${contextPath}/resources/images/Mastercard_Logo.png" style="width: 40px; height: 40px;" alt="mastercard">
                            </div>
                        </div>
                        <button class="btn btn-primary" onclick="payRental()">Finish</button>
                    </div>
                </div>
            </div>
        </div>

    </div>

    <script src="${contextPath}/resources/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="${contextPath}/resources/leaflet/leaflet.js"></script>
    <script src="${contextPath}/scripts/rent.js"></script>
    <script src="${contextPath}/scripts/leafletMap.js"></script>
</body>
</html>
