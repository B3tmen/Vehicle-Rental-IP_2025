import { useEffect, useState } from "react";
import CustomMap from "../../../components/operator/CustomMap";
import { MapVehicle } from "types/types";
import { getAll } from "@api/apiService";
import { API_MAP_VEHICLES_URL } from "@utils/constants/ApiLinks";

function VehicleMapPage(){
    const [data, setData] = useState<MapVehicle[]>([]);
    const [loading, setLoading] = useState<boolean>();
    const [error, setError] = useState<string>();

    useEffect(() => {
        const fetchVehicles = async () => {
            setLoading(true);
            try {
                const _data = await getAll<MapVehicle>(API_MAP_VEHICLES_URL);
                console.log(_data);
                setData(_data);  // Set the fetched data to state
            } catch (error) {
                setError("Error fetching map vehicles - " + error);
            } finally {
                setLoading(false);
            }
        };

        fetchVehicles();
    }, []);

    if(loading) return <p>Loading map... please wait</p>
    if(error) return <p>{error}</p>
   
    return(
        <>
            <div className="vehicle-map__header">
                 <h3>Vehicle Map</h3>
            </div>

            <div className="vehicle-map__map">
                <CustomMap mapVehicles={data}  />
            </div>
        </>
    );
}

export default VehicleMapPage;