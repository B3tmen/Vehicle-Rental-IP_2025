package org.unibl.etf.carrentalbackend.util;

public class Constants {
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"; //"yyyy-MM-dd HH:mm:ss"
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static class ImagesRelativePaths {
        private static final String VEHICLES_PATH = "vehicles/";

        public static final String CARS_PATH = VEHICLES_PATH + "cars/";
        public static final String BICYCLES_PATH = VEHICLES_PATH + "bicycles/";
        public static final String SCOOTERS_PATH = VEHICLES_PATH + "scooters/";
        public static final String CLIENTS_PATH = "clients/";
    }

    public static class EndpointUrls {
        public static final String SERVER_DOMAIN_URL = "http://localhost:9000/";
        // Endpoint form is: api/v1/...
        private static final String API_VERSION = "v1";
        private static final String API_BASE_URL = "api/" + API_VERSION;

        public static final String API_ANNOUNCEMENTS_URL = API_BASE_URL + "/announcements";
        public static final String API_AUTH_URL = API_BASE_URL + "/auth";
        public static final String API_USERS_URL = API_BASE_URL + "/users";
        public static final String API_CLIENTS_URL = API_USERS_URL + "/clients";
        public static final String API_EMPLOYEES_URL = API_USERS_URL + "/employees";
        public static final String API_VEHICLES_URL = API_BASE_URL + "/vehicles";
        public static final String API_BICYCLES_URL = API_VEHICLES_URL + "/bicycles";
        public static final String API_CARS_URL = API_VEHICLES_URL + "/cars";
        public static final String API_SCOOTERS_URL = API_VEHICLES_URL + "/scooters";
        public static final String API_IMAGES_URL = API_BASE_URL + "/images";
        public static final String API_MALFUNCTIONS_URL = API_BASE_URL + "/malfunctions";
        public static final String API_MANUFACTURERS_URL = API_BASE_URL + "/manufacturers";
        public static final String API_PROMOTIONS_URL = API_BASE_URL + "/promotions";
        public static final String API_RENTALS_URL = API_BASE_URL + "/rentals";
        public static final String API_RENTAL_STATUSES_URL = API_BASE_URL + "/rental-statuses";
        public static final String API_RSS_URL = API_BASE_URL + "/rss";
        public static final String API_STATISTICS_URL = API_BASE_URL + "/statistics";
        public static final String API_VEHICLE_MALFUNCTIONS_URL = API_BASE_URL + "/vehicle-malfunctions";

        public static final String RESOURCES_VEHICLE_IMAGES_URL = "images/vehicles/**";
        public static final String RESOURCES_CLIENT_IMAGES_URL = "images/clients/**";
    }
}
