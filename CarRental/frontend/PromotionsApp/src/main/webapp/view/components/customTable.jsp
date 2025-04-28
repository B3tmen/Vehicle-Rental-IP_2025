<%@ page import="org.unibl.etf.promotionsapp.model.dto.Promotion" %>
<%@ page import="java.util.List" %>
<%@ page import="org.unibl.etf.promotionsapp.model.beans.PromotionBean" %>
<%@ page import="org.unibl.etf.promotionsapp.model.beans.AnnouncementBean" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.unibl.etf.promotionsapp.model.dto.Announcement" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="org.unibl.etf.promotionsapp.util.Constants" %><%--
  Created by IntelliJ IDEA.
  User: Ivan
  Date: 21. 2. 2025.
  Time: 19:27
  To change this template use File | Settings | File Templates.
--%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/custom-table.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/fontawesome/css/all.min.css">

<%
    String token = (String) session.getAttribute(Constants.SessionAttributes.AUTH_TOKEN);
    String type = request.getParameter("type");
    String searchCriteria = request.getParameter("search");
    System.out.println("criteria: " + searchCriteria);

    PromotionBean promotionBean = null;         List<Promotion> promotions = null;
    AnnouncementBean announcementBean = null;   List<Announcement> announcements = null;
    if("promotions".equals(type) || type == null){
        promotionBean = new PromotionBean(token);
        promotions = promotionBean.getPromotions();

        if(searchCriteria != null && !searchCriteria.isEmpty()){
            promotions = promotions.stream()
                    .filter(p -> p.getDescription().toLowerCase().contains(searchCriteria.toLowerCase()))
                    .collect(Collectors.toList());
            System.out.println("Searched: " + promotions);
        }
    }
    else{
        announcementBean = new AnnouncementBean(token);
        announcements = announcementBean.getAnnouncements();

        if(searchCriteria != null && !searchCriteria.isEmpty() ){
            announcements = announcements.stream()
                    .filter(a -> a.getContent().toLowerCase().contains(searchCriteria.toLowerCase()))
                    .collect(Collectors.toList());
            System.out.println("Searched: " + announcements);
        }
    }

%>


<!-- custom table -->
<!-- Bootstrap table -->
<div class="table-responsive">
    <table class="table table-striped table-hover table-bordered">
        <thead class="thead-dark">
        <tr>
            <%
                if(promotions != null && !promotions.isEmpty()){
            %>
            <th scope="col">ID</th>
            <th scope="col">Title</th>
            <th scope="col">Description</th>
            <th scope="col">Duration</th>
            <%
            }
            else if(announcements != null && !announcements.isEmpty()){
            %>
            <th scope="col">ID</th>
            <th scope="col">Title</th>
            <th scope="col">Content</th>
            <%
                }
            %>
        </tr>
        </thead>
        <tbody>
        <%-- Promotions --%>
        <%
            if(promotions != null && !promotions.isEmpty()) {
                for (Promotion promotion : promotions) {
        %>
        <tr>
            <td><%= promotion.getId() %></td>
            <td><%= promotion.getTitle() %></td>
            <td><%= promotion.getDescription() %></td>
            <td><%= promotion.getDuration() %></td>
        </tr>
        <%
                }
            }
        %>

        <%-- Announcements --%>
        <%
            if(announcements != null && !announcements.isEmpty()) {
                for (Announcement announcement : announcements) {
        %>
        <tr>
            <td><%= announcement.getId() %></td>
            <td><%= announcement.getTitle() %></td>
            <td><%= announcement.getContent() %></td>
        </tr>
        <%
                }
            }
        %>
        </tbody>
    </table>
</div>
