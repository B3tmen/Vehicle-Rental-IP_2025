import axios from "axios";
import { AuthenticationResponse, LoginRequest } from "types/types";
import { API_LOGIN_URL } from "@utils/constants/ApiLinks";
import axiosInstance from "../axiosInstance";
import { AUTH_TOKEN, LOGIN_TOAST_SHOWN } from "@utils/constants/storageKeys";



export const login = async (credentials: LoginRequest): Promise<AuthenticationResponse | null> => {
    try {
        const response = await axiosInstance.post<AuthenticationResponse>(API_LOGIN_URL, credentials);

        // Store the token securely (sessionStorage for short-lived sessions)
        if (response.data.jwtToken) {
            sessionStorage.setItem(AUTH_TOKEN, response.data.jwtToken);
        }

        return response.data;
    } catch (error) {
        handleLoginError(error);
        return null;
    }
};

export const logout = (): void => {
    // Clear the token and any user-related data
    sessionStorage.removeItem(AUTH_TOKEN);
    sessionStorage.removeItem(LOGIN_TOAST_SHOWN);
};

// --- Helper Functions ---
const handleLoginError = (error: unknown): void => {
    if (axios.isAxiosError(error)) {
        switch (error.response?.status) {
            case 401:
                console.error("Unauthorized: Invalid credentials");
                break;
            case 403:
                console.error("Forbidden: Access denied");
                break;
            case 500:
                console.error("Server error: Please try again later");
                break;
            default:
                console.error(`Request failed: ${error.message}`);
        }
    } else {
        console.error("Unknown error during login:", error);
    }
};