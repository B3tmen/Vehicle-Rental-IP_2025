import { createContext, useEffect, useState } from "react";
import { Employee, JwtPayload } from "../types/types";
import { AUTH_TOKEN } from "@utils/constants/storageKeys";
import { jwtDecode } from 'jwt-decode';
import { emptyEmployeeObject } from "@utils/constants/emptyObjects";


interface AuthContextType {
    user: Employee | null;
    token: string | null;
    setLoginParameters: (token: string) => void;
    logout: () => void;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [user, setUser] = useState<Employee>(emptyEmployeeObject);
    const [token, setToken] = useState<string | null>(null);

    // Initialize auth state from token in sessionStorage
    useEffect(() => {
        const storedToken = sessionStorage.getItem(AUTH_TOKEN);
        if (storedToken) {
            try {
                const decoded = jwtDecode<JwtPayload>(storedToken);
                setToken(storedToken);
                setUser(prev => ({ ...prev, username: decoded.sub, role: decoded.role }))
            } catch (error) {
                console.error("Invalid token: ", error);
                logout(); // Clear invalid token
            }
        }
    }, []);

    const setLoginParameters = (token: string) => {
        const decoded = jwtDecode<JwtPayload>(token);
        setToken(token);
        setUser(prev => ({ ...prev, username: decoded.sub, role: decoded.role }));
        sessionStorage.setItem(AUTH_TOKEN, token);
    };

    const logout = () => {
        setToken(null);
        setUser(emptyEmployeeObject);
        sessionStorage.removeItem(AUTH_TOKEN);
    };

    return (
        <AuthContext.Provider value={{ user, token, setLoginParameters, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export default AuthContext;