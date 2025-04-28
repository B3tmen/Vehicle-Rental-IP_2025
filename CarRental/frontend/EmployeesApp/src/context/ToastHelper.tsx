import { Toast } from "primereact/toast";   // Why the heck did they call it 'Toast' for a message popup???
import React, { useRef } from "react";

export interface ToastHelperProps {
    children: React.ReactNode;
}

export const ToastHelper: React.FC<ToastHelperProps> = ({ children }) => {
    const duration = 3000;
    const toastRef = useRef<Toast>(null);

    const showSuccess = (summary: string, detail: string) => {
        toastRef.current?.show({ severity: 'success', summary, detail, life: duration });
    };

    const showError = (summary: string, detail: string) => {
        toastRef.current?.show({ severity: 'error', summary, detail, life: duration });
    };

    const showInfo = (summary: string, detail: string) => {
        toastRef.current?.show({ severity: 'info', summary, detail, life: duration });
    };

    const showWarn = (summary: string, detail: string) => {
        toastRef.current?.show({ severity: 'warn', summary, detail, life: duration });
    };

    // Expose toast actions via context
    const toastActions = {
        showSuccess,
        showError,
        showInfo,
        showWarn,
    };

    return (
        <ToastHelperContext.Provider value={toastActions}>
            <Toast ref={toastRef} />
            {children}
        </ToastHelperContext.Provider>
    );
};

// Context to access toast helper
export const ToastHelperContext = React.createContext<{
    showSuccess: (summary: string, detail: string) => void;
    showError: (summary: string, detail: string) => void;
    showInfo: (summary: string, detail: string) => void;
    showWarn: (summary: string, detail: string) => void;
} | null>(null);