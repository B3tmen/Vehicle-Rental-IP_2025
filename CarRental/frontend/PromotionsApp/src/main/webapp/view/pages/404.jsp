<%@ page import="org.unibl.etf.promotionsapp.util.Constants" %>
<%@ page import="org.unibl.etf.promotionsapp.model.beans.ManagerBean" %>
<%@ page import="org.unibl.etf.promotionsapp.model.dto.Manager" %>
<%@ page isErrorPage="true" %>
<%--
  Created by IntelliJ IDEA.
  User: Ivan
  Date: 25. 2. 2025.
  Time: 21:39
  To change this template use File | Settings | File Templates.
--%>


<%
    Object beanObject = session.getAttribute(Constants.SessionAttributes.MANAGER_BEAN);
    ManagerBean managerBean = null;
    if(beanObject != null) {
        managerBean = (ManagerBean) beanObject;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/404.css">
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

            <img src="${pageContext.request.contextPath}/resources/images/wonder-404.jpg" alt="wonder404" width="200px" height="200px" class="rounded-circle" >
            <br />

            <%
                if(managerBean != null && managerBean.isAuthenticated()) {

            %>
                <a href="${pageContext.request.contextPath}<%=Constants.Pages.INDEX_PAGE%>">Go Home</a>
            <%
                }
                else {
            %>
                <a href="${pageContext.request.contextPath}<%=Constants.Pages.LOGIN_PAGE%>">Go Home</a>
            <%
                }
            %>
        </div>
    </div>
</body>
</html>
