import { getAll } from "@api/apiService";
import AddableTable from "@components/tables/AddableTable";
import PagesContainer from "@components/admin/PagesContainer";
import ColumnConfig from "@components/tables/CustomDataTable";
import { faPen } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Vehicle } from "types/types";
import { API_VEHICLES_URL } from "@utils/constants/ApiLinks";
import { Button } from "primereact/button";
import { useEffect, useState } from "react";
import { RentalPriceDialog } from "@components/manager/RentalPriceDialog";

function RentalPricesPage() {
    const [data, setData] = useState<Vehicle[]>([]);
    const [selectedVehicle, setSelectedVehicle] = useState<Vehicle | null>(null);
    const [priceDialogVisible, setPriceDialogVisible] = useState<boolean>(false);

    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchVehicles = async () => {
            setLoading(true);
            try {
                const data = await getAll<Vehicle>(API_VEHICLES_URL);
                
                setData(data);  // Set the fetched data to state
            } catch (error) {
                setError("Error fetching vehicles - " + error);
            } finally {
                setLoading(false);
            }
        };

        fetchVehicles();
    }, []);

    const updateList = (vehicle: Vehicle) => {
        setData(prevVehicles =>
            data.map((v) => v.id === vehicle.id ? vehicle : v)
        );

        setPriceDialogVisible(false);
    }

    const showPriceDialog = (rowData: Vehicle) => {
        setSelectedVehicle( {...rowData});
        
        setPriceDialogVisible(true);
    }
    
    const actionsBodyTemplate = (rowData: Vehicle) => {
        return (
            <div className="action-buttons">
                <Button 
                    icon={ <FontAwesomeIcon icon={faPen} /> }
                    tooltip="Edit rental price"
                    text
                    onClick={() => showPriceDialog(rowData)}
                />
            </div> 
        );
    };

    const vehicleColumns: ColumnConfig<Vehicle>[] = [
        { field: 'id', header: 'ID' },
        { field: 'model', header: 'Model' },
        { field: 'rentalPrice', header: 'Price' },
        { field: 'type_', header: 'Type' },
        { header: 'Actions', body: actionsBodyTemplate }
    ];

    const renderHeaderTitle = () => {
        return <h3>Rental prices management</h3>;
    }
    const headerTitleChildren = renderHeaderTitle();

    const renderMainContent = () => {
        return (
            <>
                <AddableTable 
                    data={data} 
                    dataKey="id"
                    columns={vehicleColumns} 
                    header={undefined} 
                />

            </>
        );
    }
    const mainContentChildren = renderMainContent();

    return (
        <>
            <PagesContainer headerTitleChildren={headerTitleChildren} mainContentChildren={mainContentChildren} />

            <RentalPriceDialog vehicle={selectedVehicle} title="Update rental price" visible={priceDialogVisible} onHide={() => setPriceDialogVisible(false)} onListUpdate={updateList}  />
        </>
    );
}

export default RentalPricesPage;