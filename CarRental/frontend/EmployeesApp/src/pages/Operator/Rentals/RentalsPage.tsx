import { useEffect, useState } from "react";
import { faEye } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Button } from "primereact/button";

import { getAll } from "@api/apiService";
import ColumnConfig, { CustomDataTable } from "@components/tables/CustomDataTable";
import { API_RENTALS_URL } from "@utils/constants/ApiLinks";
import { Rental } from "types/types";
import InfoDisplayDialog from "@components/dialogs/InfoDisplayDialog";
import { locationDisplayConfig, clientDisplayConfig, vehicleDisplayConfig } from "@utils/constants/objectConfigs";


function RentalsPage(){
    const [rentalData, setRentalData] = useState<Rental[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);

    const [selectedData, setSelectedData] = useState<any | null>(null);
    const [modalTitle, setModalTitle] = useState<string>("");
    const [modalVisible, setModalVisible] = useState<boolean>(false);
    
    
    useEffect(() => {
        const fetchRentals = async () => {
            try {
                const rentals = await getAll<Rental>(API_RENTALS_URL);
                setRentalData(rentals);
            } catch (error) {
                setError("Error fetching rentals - " + error);
            } finally {
                setLoading(false);
            }
        };

        fetchRentals();
    }, []);

    if(loading) return <p>Loading rentals... Please wait</p>
    if(error) return <p>{error}</p>

    const openDetailsModal = (data: any, title: string) => {
        setSelectedData(data);
        setModalTitle(title);
        setModalVisible(true);
    };

    const detailsButtonTemplate = (rowData: Rental, field: string, title: string) => {
        return (
            <Button icon={<FontAwesomeIcon icon={faEye} style={{ color: "var(--color-secondary)" }} />}
                text outlined tooltip="View Details"
                onClick={() => openDetailsModal(rowData[field as keyof Rental], title)}
            />
        );
    };

    const rentalColumns: ColumnConfig<Rental>[] = [
        { field: 'id', header: 'ID' },
        { field: 'vehicle', header: 'Vehicle', body: (rowData) => detailsButtonTemplate(rowData, "vehicle", "Vehicle Details") },
        { field: 'client', header: 'Client', body: (rowData) => detailsButtonTemplate(rowData, "client", "Client Details") },
        { field: 'rentalDateTime', header: 'Time of rental' },
        { field: 'duration', header: 'Duration (hours)' },
        { field: 'pickupLocation', header: 'Pickup location', body: (rowData) => detailsButtonTemplate(rowData, "pickupLocation", "Pickup location")},
        { field: 'dropoffLocation', header: 'Dropoff location', body: (rowData) => detailsButtonTemplate(rowData, "dropoffLocation", "Dropoff location")},
    ];

    const getDisplayConfig = () => {
        if (!selectedData) return [];
        
        // Check based on the presence of type-specific fields
        if ('username' in selectedData) {
            return clientDisplayConfig;
        } else if ('model' in selectedData) {
            return vehicleDisplayConfig;
        } else if ('latitude' in selectedData) {
            return locationDisplayConfig;
        }
        return [];
    }
    
    return(
        <>
            <CustomDataTable 
                dataKey="id" 
                value={rentalData} 
                columns={rentalColumns}   
                header="Rentals" 
                paginator={true}        
            />

            <InfoDisplayDialog
                header={modalTitle}
                visible={modalVisible}
                onHide={() => setModalVisible(false)}
                data={selectedData}
                config={getDisplayConfig()}
            />
        </>
    );
}

export default RentalsPage;