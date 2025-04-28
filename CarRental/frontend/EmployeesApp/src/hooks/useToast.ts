import React from "react";
import { ToastHelperContext } from "../context/ToastHelper";

// Custom hook to access toast actions
export const useToast = () => {
    const context = React.useContext(ToastHelperContext);
    if (!context) {
        throw new Error('useToast must be used within a ToastHelper');
    }
    return context;
};