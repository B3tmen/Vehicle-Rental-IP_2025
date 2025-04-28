<%@ page import="org.unibl.etf.promotionsapp.util.Constants" %>
<%@ page import="org.unibl.etf.promotionsapp.model.beans.ManagerBean" %><%--
  Created by IntelliJ IDEA.
  User: Ivan
  Date: 21. 2. 2025.
  Time: 15:17
  To change this template use File | Settings | File Templates.
--%>

<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/fontawesome/css/all.min.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css"/>
<nav class="navbar">
    <div class="navbar__list">
        <ul>
            <li><a href="${pageContext.request.contextPath}/view/pages/index.jsp?type=promotions">Promotions</a></li>
            <li><a href="${pageContext.request.contextPath}/view/pages/index.jsp?type=announcements">Announcements</a></li>
        </ul>
    </div>
    <div class="navbar__logout">
        <button onclick="logout()"><i class="fa-solid fa-sign-out"></i> Logout</button>
    </div>
</nav>

<script>
    function logout(){
        const confirmedLogout = confirm("Are you sure you want to logout?");
        if(confirmedLogout){
            window.location.replace('<%=Constants.Pages.LOGIN_PAGE%>?logout');
        }
    }
</script>