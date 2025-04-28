// ---------------------------------- backend APIs ----------------------------------

const address = "localhost";
const port = "9000"

export const API_BASE_URL = `http://${address}:${port}/api/v1`;

// Login links
export const API_LOGIN_URL = API_BASE_URL + "/auth/login";

// User links
export const API_USERS_URL = API_BASE_URL + "/users";
export const API_USERS_QUANTITY_URL = API_USERS_URL + "/quantity";
export const API_CLIENTS_URL = API_USERS_URL + "/clients";
export const API_EMPLOYEES_URL = API_USERS_URL + "/employees";

// Manufacturer links
export const API_MANUFACTURERS_URL = API_BASE_URL + "/manufacturers";
export const API_MANUFACTURERS_QUANTITY_URL = API_MANUFACTURERS_URL + "/quantity";

// Vehicles links
export const API_VEHICLES_URL = API_BASE_URL + "/vehicles";
export const API_VEHICLES_CSV_URL = API_VEHICLES_URL + "/upload";
export const API_VEHICLES_QUANTITY_URL = API_VEHICLES_URL + "/quantity";
export const API_CARS_URL = API_VEHICLES_URL + "/cars";
export const API_BICYCLES_URL = API_VEHICLES_URL + "/bicycles";
export const API_SCOOTERS_URL = API_VEHICLES_URL + "/scooters";
export const API_RENTAL_STATUSES_URL = API_BASE_URL + "/rental-statuses";

// Rental links
export const API_RENTALS_URL = API_BASE_URL + "/rentals";
export const API_RENTALS_BY_VEHICLE_URL = API_RENTALS_URL + "/vehicle";
export const API_MAP_VEHICLES_URL = API_RENTALS_URL + "/map-vehicles";

// Malfunction links
export const API_MALFUNCTIONS_URL = API_BASE_URL + "/malfunctions";
export const API_VEHICLE_MALFUNCTIONS_URL = API_BASE_URL + "/vehicle-malfunctions";

// Promotions & announcements
export const API_PROMOTIONS_URL = API_BASE_URL + "/promotions";
export const API_ANNOUNCEMENTS_URL = API_BASE_URL + "/announcements";

// Statistics
const API_STATISTICS_URL = API_BASE_URL + "/statistics";
export const API_STATISTICS_MONTHLY_INCOME_URL = API_STATISTICS_URL + "/income/monthly";
export const API_STATISTICS_VEHICLE_TYPE_INCOME_URL = API_STATISTICS_URL + "/income/vehicle-type";
export const API_STATISTICS_VEHICLE_TYPES_URL = API_STATISTICS_URL + "/vehicle-types";
export const API_STATISTICS_USER_TYPES_URL = API_STATISTICS_URL + "/user-types";
export const API_STATISTICS_EMPLOYEE_ROLES_URL = API_STATISTICS_URL + "/employee-roles";
export const API_STATISTICS_MALFUNCTIONS_BY_VEHICLE_URL = API_STATISTICS_URL + "/malfunctions/vehicle";

// Images
export const API_IMAGES_URL = API_BASE_URL + "/images";

// ---------------------------------- backend APIs ----------------------------------

// ---------------------------------- OpenStreetMap APIs ----------------------------------
export const API_OPEN_STREET_MAP_REVERSE_URL = `https://nominatim.openstreetmap.org/reverse`;
export const API_OPEN_STREET_MAP_TILE_URL = "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png";