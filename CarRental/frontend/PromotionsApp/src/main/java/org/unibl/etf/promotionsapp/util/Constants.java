package org.unibl.etf.promotionsapp.util;

public class Constants {

    public static final String API_LOGIN_URL = "http://localhost:9000/api/v1/auth/login";
    public static final String API_PROMOTIONS_URL = "http://localhost:9000/api/v1/promotions";
    public static final String API_ANNOUNCEMENTS_URL = "http://localhost:9000/api/v1/announcements";

    public static class Pages {
        public static final String INDEX_PAGE = "/view/pages/index.jsp";
        public static final String LOGIN_PAGE = "/view/pages/login.jsp";
    }

    public static class Components {
        public static final String CUSTOM_TABLE_COMPONENT = "/view/components/customTable.jsp";
        public static final String MODAL_COMPONENT = "/view/components/modal.jsp";
        public static final String NAVBAR_COMPONENT = "/view/components/navbar.jsp";
    }

    public static class SessionAttributes {
        public static final String MANAGER_BEAN = "managerBean";
        public static final String AUTH_TOKEN = "authToken";
        public static final String ERROR_MESSAGE = "errorMessage";
    }
}
