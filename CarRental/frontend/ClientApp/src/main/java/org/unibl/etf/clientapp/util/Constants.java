package org.unibl.etf.clientapp.util;

public class Constants {
    public static final String HIDDEN_CARD_NUMBER = "************";  // The rest of 4 '*' is visible
    public static final String PDF_EXTENSION = ".pdf";

    public static class SessionParameters {
        public static final String AUTHENTICATED_CLIENT = "authClient";
        public static final String ERROR_MESSAGE = "errorMessage";
        public static final String ELECTRIC_VEHICLES = "electricVehicles";
        public static final String VEHICLE_ID = "vehicleId";
        public static final String VEHICLE_TYPE = "vehicleType";
        public static final String SELECTED_VEHICLE_FOR_RENT = "selectedVehicleForRent";

        public static final String CLIENT_INVOICES = "clientInvoices";

        public static final String DRIVE_BEAN = "driveBean";
        public static final String REMEMBERED_PAYMENT = "rememberedPayment";
    }

    public static class Pages {
       public static final String LOGIN_PAGE = "/WEB-INF/pages/login.jsp";
       public static final String REGISTER_PAGE = "/WEB-INF/pages/register.jsp";
       public static final String LOGOUT_PAGE = "/WEB-INF/pages/logout.jsp";
       public static final String HOME_PAGE = "/WEB-INF/pages/home.jsp";
       public static final String RENT_PAGE = "/WEB-INF/pages/rent.jsp";
       public static final String PROFILE_PAGE = "/WEB-INF/pages/profile.jsp";
       public static final String DRIVE_PAGE = "/WEB-INF/pages/drive.jsp";

    }

    public static class Components {
        public static final String HEADER_COMPONENT = "/WEB-INF/components/header.jsp";
        public static final String CARD_COMPONENT = "/WEB-INF/components/card.jsp";
        public static final String FOOTER_COMPONENT = "/WEB-INF/components/footer.jsp";
        public static final String DATA_MODAL_COMPONENT = "/WEB-INF/components/modal.jsp";
        public static final String CONFIRM_MODAL_COMPONENT = "/WEB-INF/components/confirmModal.jsp";
        public static final String CUSTOM_TOAST_COMPONENT = "/WEB-INF/components/customToast.jsp";
        public static final String PROFILE_RENTAL_TABLE_COMPONENT = "/WEB-INF/components/profileRentalTable.jsp";
    }

    public static class ServletURLs {
        public static final String LOGIN_URL = "/login";
        public static final String LOGOUT_URL = "/logout";
        public static final String REGISTER_URL = "/register";
        public static final String HOME_URL = "/home";
        public static final String CARS_URL = "/cars";
        public static final String BICYCLES_URL = "/bicycles";
        public static final String SCOOTERS_URL = "/scooters";
        public static final String PAYMENT_URL = "/payment";
        public static final String RENT_URL = "/rent";
        public static final String PROFILE_URL = "/profile";
        public static final String DRIVE_URL = "/drive";
    }
}
