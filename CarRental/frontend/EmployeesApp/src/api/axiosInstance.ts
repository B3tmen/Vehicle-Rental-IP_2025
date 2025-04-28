import axios from "axios";
import { API_BASE_URL } from "@utils/constants/ApiLinks";

const axiosInstance = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
    withCredentials: true
});

// For the request interceptor, we need to send a token as authorization,
// so we put it in the header
axiosInstance.interceptors.request.use(
    (config) => {
        const token = sessionStorage.getItem('authToken');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }

        // Handle FormData content type dynamically
        if (config.data instanceof FormData) {
            config.headers["Content-Type"] = "multipart/form-data";
        }

        return config;
    },
    (error) => Promise.reject(error)
);

// For the response interceptor, we need to check if token was valid in case of errors,
// so we check for 401 status and send the client back to log in
axiosInstance.interceptors.response.use(
    (response) => response,
    async (error) => {
        const { response } = error;

        // Handle 401 (Unauthorized) or 403 (Forbidden)
        if (response?.status === 401 || response?.status === 403) {
            //sessionStorage.removeItem('authToken');
            
            // Redirect to login (avoid hard reload if using SPA)
            //window.location.href = '/login';
        }

        return Promise.reject(error);
    }
);

export default axiosInstance;