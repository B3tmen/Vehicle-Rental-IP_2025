<%@ page import="org.unibl.etf.promotionsapp.util.Constants" %><%--
  Created by IntelliJ IDEA.
  User: Ivan
  Date: 24. 2. 2025.
  Time: 17:48
  To change this template use File | Settings | File Templates.
--%>

<%
    String authToken = (String) session.getAttribute(Constants.SessionAttributes.AUTH_TOKEN);

    String modalType = request.getParameter("type");
    boolean isPromotion = "promotions".equals(modalType) || modalType == null;
    String modalTitle = isPromotion ? "Add a Promotion" : "Add an Announcement";
    String fieldName = isPromotion ? "description" : "content";
    String placeholder = isPromotion ? "Description" : "Content";
%>

<div class="modal fade" id="customModal" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="modalLabel"><%= modalTitle %></h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="itemForm" method="POST">
                    <div class="mb-3">
                        <label for="titleInput" class="form-label">Title</label>
                        <input type="text" class="form-control" id="titleInput" name="title" placeholder="Title" required>
                    </div>
                    <div class="mb-3">
                        <label for="contentInput" class="form-label"><%= placeholder %></label>
                        <textarea class="form-control" id="contentInput" name="<%= fieldName %>"
                                  placeholder="<%= placeholder %>" rows="3" required></textarea>
                    </div>
                    <% if (isPromotion) { %>
                    <div class="mb-3">
                        <label for="durationInput" class="form-label">Duration</label>
                        <input type="date" class="form-control" id="durationInput" name="duration" required>
                    </div>
                    <% } %>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-primary" form="itemForm">Save</button>
            </div>
        </div>
    </div>
</div>

<script>
    // Initialize modal if needed
    document.addEventListener('DOMContentLoaded', function() {
        const itemForm = document.getElementById('itemForm');
        if (itemForm) {
            itemForm.onsubmit = async function(e) {
                e.preventDefault();

                const formData = new FormData(e.target);
                const jsonData = JSON.stringify(Object.fromEntries(formData));

                const isPromotion = '<%= isPromotion %>' === 'true';
                const apiUrl = isPromotion ? '<%= Constants.API_PROMOTIONS_URL %>' : '<%= Constants.API_ANNOUNCEMENTS_URL %>';
                const itemType = isPromotion ? 'promotion' : 'announcement';

                try {
                    console.log("To send: " + jsonData);
                    const response = await fetch(apiUrl, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': 'Bearer <%= authToken %>'
                        },
                        body: jsonData
                    });

                    if (!(response.status === 201)) {
                        throw new Error(`Failed to save ${itemType}`);
                    }

                    // Hide modal using Bootstrap's method
                    const modal = bootstrap.Modal.getInstance(document.getElementById('customModal'));
                    if (modal) {
                        modal.hide();
                    }

                    window.location.reload();
                    alert(`Successfully added ${itemType}`);
                } catch (error) {
                    console.log('Error: ', error);
                    alert(`Failed to add ${itemType}`);
                }
            };
        }
    });

    // Function to show the modal (can be called from other scripts)
    function showModal() {
        const modal = document.getElementById('customModal');
        modal.show();
    }

    // Function to hide the modal
    function hideModal() {
        const modal = bootstrap.Modal.getInstance(document.getElementById('customModal'));
        if (modal) {
            modal.hide();
        }
    }
</script>
