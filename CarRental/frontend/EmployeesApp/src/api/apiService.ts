import axios, { AxiosError } from "axios";
import axiosInstance from "./axiosInstance";
import { CustomError } from "types/classes";

export const getAll = async <T>(url: string): Promise<T[]> => {
    const response = await axiosInstance.get(url);
    return response.data;
};

export const getById = async <T>(url: string, id: number): Promise<T> => {
    const response = await axiosInstance.get(`${url}/${id}`);
    return response.data;
}

/** T - input type, R - return type */
export const insert = async <T, R>(url: string, data: T): Promise<R> => {
    try {
        const isFormData = data instanceof FormData;
        const response = await axiosInstance.post(url, isFormData ? data : JSON.stringify(data), {
            headers: {
                "Content-Type": isFormData ? "multipart/form-data" : "application/json",
            },
        });
        return response.data;
    } 
    catch (error) {
        if (axios.isAxiosError(error)) {
            // Extract server error message if available
            const statusCode = error.response?.status || 500;
            const serverMessage = error.response?.data || error.message;

            console.log("ERROR: " + error.response?.data)
            
            // Throw a structured error with status code and message
            throw new CustomError(statusCode, serverMessage, true);
        } else {
            // Non-Axios errors (e.g., network failure)
            throw new CustomError(500, "An unexpected error occurred", false);
        }
    }
};

/** T - input type, R - return type */
export const update = async <T, R>(url: string, id: number | null, data: T): Promise<R> => {
    try {
        const isFormData = data instanceof FormData;
        const response = await axiosInstance.put(`${url}/${id}`, isFormData ? data : JSON.stringify(data), {
            headers: {
                "Content-Type": isFormData ? "multipart/form-data" : "application/json",    // For handling sending of objects and files (e.g. csv for vehicles)
            },
        });

        return response.data;
    } 
    catch (error) {
        if (axios.isAxiosError(error)) {
            // Extract server error message if available
            const statusCode = error.response?.status || 500;
            const serverMessage = error.response?.data || error.message;

            // Throw a structured error with status code and message
            throw new CustomError(statusCode, serverMessage, true);
        } else {
            // Non-Axios errors (e.g., network failure)
            throw new CustomError(500, "An unexpected error occurred", false);
        }
    }
};

export const remove = async (url: string, id: number | null): Promise<boolean> => {
    const response = await axiosInstance.delete(`${url}/${id}`);
    
    if(response.status == 204){
        return true;
    }
    return false;
};