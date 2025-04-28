import { useEffect, useState } from "react";
import { getAll } from "../api/apiService";

type UseFetchResult<T> = {
    data: T[];
    error: string | null;
    loading: boolean;
};
  
export const useFetchData = <T>(url: string): UseFetchResult<T> => {
    const [data, setData] = useState<T[]>([]);
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);
                const response = await getAll<T>(url);
                setData(response);
            } catch (err) {
                setError(err instanceof Error ? err.message : 'An error occurred');
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [url]);

    return { data, error, loading };
};