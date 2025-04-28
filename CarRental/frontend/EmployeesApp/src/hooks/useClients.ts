import { useEffect, useState } from "react";
import { getAllClients } from "../api/services/clientsService";
import { Client } from "../types/types";

export const useClients = () => {
    const [clientsData, setClientsData] = useState<Client[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetch = async () => {
            try{
                const data = await getAllClients();

                setClientsData(data);
                
            } catch(error){
                console.error("Failed to fetch clients: " + error);
                //setError("Could not fetch clients");
            } finally{
                setLoading(false);
            }
        }

        fetch();
    }, []);

    return { clientsData, loading, error };
}