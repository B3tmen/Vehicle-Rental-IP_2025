import './MalfunctionsPage.css'
import { useEffect, useState } from "react";
import { Image } from "primereact/image";
import { Tag } from "primereact/tag";
import { Button } from "primereact/button";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faAdd, faEye } from "@fortawesome/free-solid-svg-icons";

import { getAll, insert } from "@api/apiService";
import AddableTable from "@components/tables/AddableTable";
import PagesContainer from "@components/admin/PagesContainer";
import ColumnConfig, { CustomDataTable } from "@components/tables/CustomDataTable";
import { AddMalfunctionDialog } from "@components/operator/AddMalfunctionDialog";
import { Malfunction, RentalStatus, Vehicle, VehicleMalfunction } from "types/types";
import { API_VEHICLE_MALFUNCTIONS_URL, API_VEHICLES_URL } from "@utils/constants/ApiLinks";
import { Dialog } from 'primereact/dialog';
import { emptyVehicleObject } from '@utils/constants/emptyObjects';
import { useToast } from '@hooks/useToast';

function MalfunctionsPage(){
    const emptyVehicle: Vehicle = emptyVehicleObject;
    const { showSuccess, showError } = useToast();
    const [malfunctionModalVisible, setMalfunctionModalVisible] = useState<boolean>(false);
    const [allMalfunctionsModalVisible, setAllMalfunctionsModalVisible] = useState<boolean>(false);
    const [malfunctionsData, setMalfunctionsData] = useState<Malfunction[]>([]);
    const [vehiclesData, setVehiclesData] = useState<Vehicle[]>([]);
    const [selectedVehicle, setSelectedVehicle] = useState<Vehicle>(emptyVehicle);
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);

    const fetchVehicleMalfunctions = async (vehicleId: number) => {
        setLoading(true);
        try {
            const data = await getAll<Malfunction>(`${API_VEHICLE_MALFUNCTIONS_URL}/${vehicleId}`);
            setMalfunctionsData(data);
        } catch (error) {
            setError("Error fetching malfunctions - " + error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        const fetchVehicles = async () => {
            setLoading(true);
            try {
                const data = await getAll<Vehicle>(API_VEHICLES_URL);
                
                setVehiclesData(data);  // Set the fetched data to state
            } catch (error) {
                setError("Error fetching vehicles - " + error);
            } finally {
                setLoading(false);
            }
        };

        fetchVehicles();
    }, []);

    if(loading) return <p>Loading vehicles... Please wait</p>
    if(error) return <p>{error}</p>


    const getSeverity = (value: RentalStatus) => {
        switch(value.status){
            case 'Free': return 'success';
            case 'Rented': return 'warning';
            case 'Broken': return 'danger';
            default: return null;
        }
    };

    const showMalfunctionsModal = (rowData: Vehicle) => {
        setSelectedVehicle(rowData);
        setMalfunctionModalVisible(true);
    }

    const showAllMalfunctionsModal = async (rowData: Vehicle) => {
        setSelectedVehicle(rowData);
        if (rowData.id) {
            await fetchVehicleMalfunctions(rowData.id);
        }
        setAllMalfunctionsModalVisible(true);
    }

    const addMalfunction = async (vehicleMalfunction: VehicleMalfunction) => {
        try {
            const response = await insert<VehicleMalfunction, VehicleMalfunction>(API_VEHICLE_MALFUNCTIONS_URL, vehicleMalfunction);
            if (response) {
                showSuccess("Success", "Successfully added malfunction for specified vehicle");

                const malfunction: Malfunction = response.malfunction;
                setMalfunctionsData(prevMalfunctions => [...prevMalfunctions, malfunction]);
                setMalfunctionModalVisible(false);
            }
            else {
                showError("Error", "Couldn't add malfunction. Invalid fields");
            }
        } catch (error) {
            showError("Error", `Error adding malfunction: ${error}`);
        }
    }
    

    const imageBodyTemplate = (rowData: Vehicle) => {
        return <Image src={rowData.image?.url} alt={rowData.model + '_img'} width='60px' height='60px' preview />
    };

    const statusBodyTemplate = (rowData: Vehicle) => {
        let _color = 'var(--color-green)';  // Free
        if(rowData.rentalStatus.status === 'Rented') _color = 'var(--color-orange)';
        else if(rowData.rentalStatus.status === 'Broken') _color = 'var(--color-red)';

        return <Tag value={rowData.rentalStatus.status} severity={getSeverity(rowData.rentalStatus)} style={{ background: _color }} />
    };

    const vehicleActionsBodyTemplate = (rowData: Vehicle) => {
        return (
            <div className="action-buttons">
                <Button 
                    icon={ <FontAwesomeIcon icon={faAdd} /> }
                    tooltip="Add malfunction"
                    rounded outlined
                    onClick={() => showMalfunctionsModal(rowData)}
                />
                <Button 
                    icon={ <FontAwesomeIcon icon={faEye} /> }
                    tooltip="View malfunctions"
                    rounded outlined
                    onClick={() => showAllMalfunctionsModal(rowData)}
                />
            </div> 
        );
    };

    const vehicleColumns: ColumnConfig<Vehicle>[] = [
        { field: 'id', header: 'ID' },
        { field: 'image', header: 'Image', body: imageBodyTemplate },
        { field: 'model', header: 'Model' },
        { field: 'price', header: 'Price' },
        { header: 'Status', body: statusBodyTemplate },
        { field: 'type_', header: 'Type' },
        { header: 'Malfunctions', body: vehicleActionsBodyTemplate }
    ];

    const renderHeaderTitle = () => {
        return <h3>Malfunctions</h3>;
    }
    const headerTitleChildren = renderHeaderTitle();

    const renderMainContent = () => {
        return (
            <>
                <AddableTable 
                    data={vehiclesData} 
                    dataKey="id"
                    columns={vehicleColumns} 
                    header={undefined} 
                />

            </>
        );
    }
    const mainContentChildren = renderMainContent();

    const malfunctionColumns: ColumnConfig<Malfunction>[] = [
        { field: 'id', header: 'ID' },
        { field: 'reason', header: 'Reason' },
        { 
            field: 'timeOfMalfunction', 
            header: 'Time of Malfunction',
            body: (rowData: Malfunction) => {
                return new Date(rowData.timeOfMalfunction).toLocaleString();
            }
        }, 
    ];

    return (
        <>
            <PagesContainer headerTitleChildren={headerTitleChildren} mainContentChildren={mainContentChildren} />

            <AddMalfunctionDialog visible={malfunctionModalVisible}  vehicle={selectedVehicle} onHide={() => setMalfunctionModalVisible(false)} onAdd={addMalfunction} />

            <Dialog 
                visible={allMalfunctionsModalVisible}
                onHide={() => setAllMalfunctionsModalVisible(false)}
                header="All malfunctions of vehicle"
                draggable={false} 
            >
                {loading ? (
                    <p>Loading malfunctions...</p>
                ) : error ? (
                    <p>{error}</p>
                ) : malfunctionsData.length === 0 ? (
                    <p>No malfunctions recorded for this vehicle.</p>
                ) : (
                    <CustomDataTable 
                        value={malfunctionsData}
                        dataKey="id"
                        columns={malfunctionColumns}
                        paginator
                        rows={5}
                        rowsPerPageOptions={[5, 10, 25]}
                    />
                    
                )}
            </Dialog>
        </>
    );
}

export default MalfunctionsPage;

