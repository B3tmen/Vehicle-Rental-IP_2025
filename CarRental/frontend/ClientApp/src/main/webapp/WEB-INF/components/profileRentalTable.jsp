<%--
  Created by IntelliJ IDEA.
  User: Ivan
  Date: 25. 3. 2025.
  Time: 09:04
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="invoices" value="${clientInvoices}" />

<div class="table-responsive">
    <table class="table table-striped table-bordered table-hover ${param.tableClass}">
        <thead class="thead-dark">
        <tr>
            <th>Rental Date</th>
            <th>Vehicle Type</th>
            <th>Manufacturer</th>
            <th>Model</th>
            <th>Price/hr</th>
            <th>Duration (hours)</th>
            <th>Total Price</th>
            <th>View Invoice <i class="fas fa-file-pdf" style="color: var(--color-red)"></i></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${invoices}" var="invoice">
            <c:set var="rental" value="${invoice.rental}" />

            <tr>
                <%-- Rental Date --%>
                <td>
                    ${rental.rentalDateTime.format(DateTimeFormatter.ofPattern('dd.MM.yyyy HH:mm:ss'))}
                </td>

                <%-- Vehicle Type --%>
                <td>${rental.vehicle.type}</td>

                <%-- Manufacturer --%>
                <td>${rental.vehicle.manufacturer.name}</td>

                <%-- Model --%>
                <td>${rental.vehicle.model}</td>

                <%-- Price per hr --%>
                <td>
                    <fmt:formatNumber value="${rental.vehicle.rentalPrice}"/>
                </td>

                <%-- Duration --%>
                <td>${rental.duration}</td>

                <%-- Total Price --%>
                <td>
                    <fmt:formatNumber value="${rental.vehicle.rentalPrice * rental.duration}"/>
                </td>

                <%-- Invoice URL --%>
                <td>
                    <a href="${invoice.invoiceURL}">Invoice.pdf</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<c:if test="${empty invoices}">
    <div class="alert alert-info">
        You haven't rented any vehicles so far.
    </div>
</c:if>
