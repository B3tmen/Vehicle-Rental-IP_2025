<%@ page import="org.unibl.etf.clientapp.util.Constants" %><%--
  Created by IntelliJ IDEA.
  User: Ivan
  Date: 4. 3. 2025.
  Time: 21:45
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
    <link rel="stylesheet" href="${contextPath}/resources/bootstrap/css/bootstrap.min.css">
    <title>Logout</title>
</head>
<body>
    <div class="d-flex align-items-center justify-content-center w-100">
        <div class="text-center mb-4">
            <p class="fs-2">
                <span class="text-secondary"><strong>Goodbye!</strong></span>
                <br />
                Logout was successful.
            </p>
            <a href="${contextPath}<%=Constants.ServletURLs.LOGIN_URL%>" class="fs-5">Go Home</a>
        </div>
    </div>
</body>
</html>
