import axiosInstance from "@api/axiosInstance";
import { API_STATISTICS_MONTHLY_INCOME_URL, API_STATISTICS_VEHICLE_TYPE_INCOME_URL } from "@utils/constants/ApiLinks";
import { MonthlyIncome, VehicleTypeIncome } from "types/types";

export const getMonthlyIncomeData = async (): Promise<MonthlyIncome[]> => {
    const response = await axiosInstance.get<MonthlyIncome[]>(API_STATISTICS_MONTHLY_INCOME_URL);
    if(response.status === 200) {
        const monthlyIncomeList: MonthlyIncome[] = response.data;

        return monthlyIncomeList;
    }

    return [];
}

export const getVehicleTypeIncomeData = async (): Promise<VehicleTypeIncome[]> => {
    const response = await axiosInstance.get<VehicleTypeIncome[]>(API_STATISTICS_VEHICLE_TYPE_INCOME_URL);
    if(response.status === 200) {
        const vehicleTypeIncomeList: VehicleTypeIncome[] = response.data;

        return vehicleTypeIncomeList;
    }

    return [];
}