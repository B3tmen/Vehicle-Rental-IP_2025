<%--
  Created by IntelliJ IDEA.
  User: Ivan
  Date: 5. 3. 2025.
  Time: 18:01
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>

<fmt:formatDate var="currentYear" value="<%= new java.util.Date() %>" pattern="yyyy" />

<footer class="d-flex align-items-center justify-content-center" style="height: 50px">
    <div class="d-flex align-items-center justify-content-center gap-3">
        <i class="fas fa-car" style="color: white; font-size: 1.5rem"></i>
        <p style="color: var(--color-light-gray); margin-bottom: 0;">&copy; ${currentYear} ETFBL_IP. All rights reserved</p>
    </div>
</footer>
