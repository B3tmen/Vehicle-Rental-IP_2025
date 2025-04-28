import { Navigate } from "@tanstack/react-router";
import { useAuth } from "@hooks/useAuth";

type ProtectedRouteProps = {
    children: React.ReactNode;
    allowedRoles: string[];
};

const ProtectedRoute = ({ children, allowedRoles }: ProtectedRouteProps) => {
    const { user, token } = useAuth();

    if (!token || user?.username === '') {
        return <Navigate to="/login" replace />;
    }

    const userRole = user?.role;

    if (!allowedRoles.includes(userRole!)) {
        return <Navigate to="/login" replace />;
    }

    return (
        <>
            {children}
        </>
    );
}

export default ProtectedRoute;

