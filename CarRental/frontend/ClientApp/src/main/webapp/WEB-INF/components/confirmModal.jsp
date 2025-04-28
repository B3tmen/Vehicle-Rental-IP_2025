<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Ivan
  Date: 9. 3. 2025.
  Time: 11:15
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="bodyIcon" value="${param.bodyIcon}" />
<c:set var="modalTitle" value="${param.modalTitle}" />
<c:set var="modalBody" value="${param.modalBody}" />

<!-- Confirm Modal -->
<div class="modal fade" id="confirmModal" tabindex="-1" aria-labelledby="confirmModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="confirmModalLabel">${modalTitle}</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body text-center">
                <i class="${bodyIcon} fa-3x" style="color: var(--color-red)"></i>
                <br />
                <div>
                    ${modalBody}
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">No</button>
                <button type="button" id="confirmModalYesButton" class="btn btn-primary">Yes</button>
            </div>
        </div>
    </div>
</div>