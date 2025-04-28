<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.net.HttpURLConnection, java.net.URL, java.io.BufferedReader, java.io.InputStreamReader" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ page import="java.util.List" %>
<%@ page import="org.unibl.etf.promotionsapp.model.dto.Promotion" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.net.URI" %>
<%@ page import="org.unibl.etf.promotionsapp.model.beans.PromotionBean" %>
<%@ page import="org.unibl.etf.promotionsapp.util.Constants" %>
<%@ page import="org.unibl.etf.promotionsapp.model.beans.ManagerBean" %>


<%
    ManagerBean managerBean = (ManagerBean) session.getAttribute(Constants.SessionAttributes.MANAGER_BEAN);
    System.out.println("BEAN: " + managerBean);

    if(managerBean == null || !managerBean.isAuthenticated()){
        String errorMessage = "Invalid username/password. Please try again.";
        session.setAttribute(Constants.SessionAttributes.ERROR_MESSAGE, errorMessage);
        response.sendRedirect(request.getContextPath() + Constants.Pages.LOGIN_PAGE);
        return;
    }

    session.setAttribute(Constants.SessionAttributes.ERROR_MESSAGE, null);
    PromotionBean promotionBean = new PromotionBean(managerBean.getAuthToken());
    List<Promotion> promotions = promotionBean.getPromotions();

    request.setAttribute("promotions", promotions);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/fontawesome/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/index.css">
    <title>Promotions & Announcements</title>
</head>
    <body>
        <div id="root">
            <%-- navbar --%>
            <jsp:include page="<%=Constants.Components.NAVBAR_COMPONENT%>" />
            
            <%-- content --%>
            <div class="content">
                <div class="content__table-container">
                    <div class="content__table-container__header">
                        <div class="content__table-container__header__search">
                            <form method="get" action="" class="d-flex justify-content-center align-items-center">
                                <input type="hidden" name="type" value="<%= request.getParameter("type") %>" />
                                <input class="form-control" type="text" placeholder="Search..." name="search" value="<%= request.getParameter("search") != null ? request.getParameter("search") : "" %>">
                                <button class="btn btn-outline-primary" type="submit"><i class="fa fa-search"></i></button>
                            </form>
                        </div>

                        <div class="content__table-container__header__title">
                            <h4>
                                <%
                                    String type = request.getParameter("type");
                                    if(type == null){
                                %>
                                    Promotions
                                <%
                                    }
                                    else if("promotions".equals(type)){
                                %>
                                    Promotions
                                <%
                                    }
                                    else if("announcements".equals(type)){
                                %>
                                    Announcements
                                <%
                                    }
                                %>
                            </h4>
                        </div>

                        <div class="content__table-container__header__add-button">
                            <button class="btn btn-primary" onclick="showModal()" data-bs-toggle="modal" data-bs-target="#customModal">
                                <i class="fas fa-add"></i> Add
                            </button>
                        </div>
                    </div>

                    <div class="content__table-container__table">
                        <jsp:include page="<%=Constants.Components.CUSTOM_TABLE_COMPONENT%>">
                            <jsp:param name="items" value="${promotions}" />
                        </jsp:include>
                    </div>
                </div>

                <%-- Modal --%>
                <%@ include file="/view/components/modal.jsp" %>
            </div>
        </div>

        <script src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.bundle.min.js"></script>
    </body>
</html>