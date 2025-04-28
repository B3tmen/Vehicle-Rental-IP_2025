import { useNavigate } from "@tanstack/react-router";

export const useNavigationHelper = () => {
    const navigate = useNavigate();

    return (from: string, to: string, replace: boolean = false) => {
        navigate({ from: from, to: to, replace: replace });
    };
};