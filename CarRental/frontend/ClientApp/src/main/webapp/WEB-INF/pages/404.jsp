<%@ page import="org.unibl.etf.clientapp.util.Constants" %><%--
  Created by IntelliJ IDEA.
  User: Ivan
  Date: 25. 2. 2025.
  Time: 21:39
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="clientBean" value="${sessionScope.authClient}" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="${contextPath}/resources/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${contextPath}/styles/404.css">
    <title>Error - 404</title>
</head>
<body>
    <div class="container-404 w-100">
        <div class="d-flex flex-column justify-content-center align-items-center">
            <h1>404</h1>
            <p>
                <span>Oops!</span> Sorry, we can't find that page.
            </p>
            <p>This is not the page you are looking for.</p>

            <img src="${contextPath}/resources/images/wonder-404.jpg" alt="wonder404" width="200px" height="200px" class="rounded-circle" >
            <br />
            <c:choose>
                <c:when test="${clientBean eq null}">
                    <a href="${contextPath}<%=Constants.ServletURLs.LOGIN_URL%>">Go Home</a>
                </c:when>
                <c:otherwise>
                    <a href="${contextPath}<%=Constants.ServletURLs.HOME_URL%>">Go Home</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>
