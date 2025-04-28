import { StatisticsMapData } from "types/types";
import { API_MANUFACTURERS_QUANTITY_URL, API_STATISTICS_EMPLOYEE_ROLES_URL, API_STATISTICS_MALFUNCTIONS_BY_VEHICLE_URL, API_STATISTICS_USER_TYPES_URL, API_STATISTICS_VEHICLE_TYPES_URL, API_USERS_QUANTITY_URL, API_VEHICLES_QUANTITY_URL } from "../../utils/constants/ApiLinks";
import axiosInstance from "../axiosInstance";

export const getVehiclesQuantity = async (): Promise<number> => {
    const response = await axiosInstance.get(API_VEHICLES_QUANTITY_URL);
    return response.data;
}

export const getManufacturersQuantity = async (): Promise<number> => {
    const response = await axiosInstance.get(API_MANUFACTURERS_QUANTITY_URL);
    return response.data;
}

export const getUsersQuantity = async (): Promise<number> => {
    const response = await axiosInstance.get(API_USERS_QUANTITY_URL);
    return response.data;
}

// -----------------------------------------------

export const getVehicleTypesDistributionMap = async (): Promise<StatisticsMapData> => {
    const response = await axiosInstance.get(API_STATISTICS_VEHICLE_TYPES_URL);
    return response.data;
}

export const getUserTypesDistributionMap = async (): Promise<StatisticsMapData> => {
    const response = await axiosInstance.get(API_STATISTICS_USER_TYPES_URL);
    return response.data;
}

export const getEmployeeRolesDistributionMap = async (): Promise<StatisticsMapData> => {
    const response = await axiosInstance.get(API_STATISTICS_EMPLOYEE_ROLES_URL);
    return response.data;
}

export const getMalfunctionsByVehicleMap = async (): Promise<StatisticsMapData> => {
    const response = await axiosInstance.get(API_STATISTICS_MALFUNCTIONS_BY_VEHICLE_URL);
    return response.data;
}
