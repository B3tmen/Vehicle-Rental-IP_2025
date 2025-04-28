<%--
  Created by IntelliJ IDEA.
  User: Ivan
  Date: 7. 3. 2025.
  Time: 10:19
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="driveBean" value="${sessionScope.driveBean}" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="${contextPath}/resources/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${contextPath}/resources/fontawesome/css/all.min.css">
    <link rel="stylesheet" href="${contextPath}/styles/drive.css">
    <link rel="icon" type="image/png" href="${contextPath}/resources/images/vehicle-rent-icon.png">
    <title>Drive</title>
</head>
<body>
    <!-- Background Video -->
    <video class="video-background" autoplay muted loop>
        <source src="${contextPath}/resources/videos/driving_video.mp4" type="video/mp4">
        Your browser does not support the video tag.
    </video>

    <div class="overlay">
        <div id="root" class="d-flex flex-column w-100 h-100 justify-content-center align-items-center p-3">
            <div class="drive-container border border-secondary-subtle rounded d-flex flex-column gap-5 align-items-center w-75 h-75 ">
                <div class="drive-container__duration d-flex flex-column align-items-center justify-content-center border-bottom border-secondary w-100 h-25">
                    <c:if test="${driveBean != null}">
                        <h3 id="timer">Duration ${driveBean.duration}:00:00</h3>
                        <h4 id="driveFinishedHeading" hidden>Drive finished!</h4>
                    </c:if>
                </div>
                <div class="d-flex flex-column w-100 h-100">
                    <div class="d-flex align-items-center justify-content-center w-100 h-100">
                        <c:choose>
                            <c:when test="${driveBean == null}">
                                <h4>You haven't rented any vehicles so far</h4>
                            </c:when>
                            <c:otherwise>
                                <h4>Paid price: ${driveBean.totalPrice}</h4>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="drive-container__button d-flex justify-content-end w-100 p-4">
                        <button id="finishButton" type="button" class="btn btn-primary w-100" > <%-- onclick is set in drive.js --%>
                            <c:choose>
                                <c:when test="${driveBean == null}">
                                    <i class="fas fa-arrow-left"></i> Go Back
                                </c:when>
                                <c:otherwise>
                                    <i class="fas fa-check"></i> Finish
                                </c:otherwise>
                            </c:choose>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        const driveActive = ${driveBean != null ? true : false}
        const initialDriveHours = ${driveBean != null ? driveBean.duration : 0};
    </script>
    <script src="${contextPath}/scripts/drive.js"></script>
</body>
</html>
