import './VehicleDetailsPage.css'
import { useEffect, useState } from 'react';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faAdd, faExternalLink, faEye, faMagnifyingGlass, faTrashAlt } from "@fortawesome/free-solid-svg-icons";
import { Image } from "primereact/image";
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import { InputTextarea } from 'primereact/inputtextarea';

import PagesContainer from "@components/admin/PagesContainer";
import { Bicycle, Car, Scooter, Vehicle, Malfunction, Rental, VehicleMalfunction } from 'types/types';
import { RowTitleContentContainer } from '@components/RowTitleContentContainer';
import { getAll, getById, insert, remove } from '@api/apiService';
import { API_BICYCLES_URL, API_CARS_URL, API_MALFUNCTIONS_URL, API_RENTALS_BY_VEHICLE_URL, API_SCOOTERS_URL, API_VEHICLE_MALFUNCTIONS_URL, API_VEHICLES_URL } from '@utils/constants/ApiLinks';
import { isBicycle, isCar, isScooter } from '@utils/typeChecker';
import { emptyMalfunctionObject, emptyVehicleObject } from '@utils/constants/emptyObjects';
import InfoDisplayDialog from '@components/dialogs/InfoDisplayDialog';
import { clientDisplayConfig, locationDisplayConfig, manufacturerDisplayConfig, vehicleDisplayConfig } from '@utils/constants/objectConfigs';
import ColumnConfig, { CustomDataTable } from '@components/tables/CustomDataTable';
import { AddMalfunctionDialog } from '@components/operator/AddMalfunctionDialog';
import ConfirmationDialog from '@components/dialogs/ConfirmationDialog';
import { useToast } from '@hooks/useToast';

function VehicleDetailsPage() {
    const { showSuccess, showError } = useToast(); 
    const emptyVehicle = emptyVehicleObject;
    const icon = (<FontAwesomeIcon icon={faMagnifyingGlass} />);
    const [vehicle, setVehicle] = useState<Vehicle>(emptyVehicle);
    const [malfunctionsData, setMalfunctionsData] = useState<Malfunction[]>([]);
    const [selectedMalfunction, setSelectedMalfunction] = useState<Malfunction>(emptyMalfunctionObject);
    const [manufacturerModalVisible, setManufacturerModalVisible] = useState<boolean>(false);
    const [malfunctionModalVisible, setMalfunctionModalVisible] = useState<boolean>(false);
    const [deleteMalfunctionModalVisible, setDeleteMalfunctionModalVisible] = useState<boolean>(false);

    const [rentalData, setRentalData] = useState<Rental[]>([]);
    const [selectedRentalChildData, setSelectedRentalChildData] = useState<any | null>(null);
    const [rentalDetailsModalTitle, setRentalDetailsModalTitle] = useState<string>("");
    const [rentalDetailsModalVisible, setRentalDetailsModalVisible] = useState<boolean>(false);

    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);

    const currentPath = window.location.pathname;
    const vehicleId = parseInt(currentPath.split("/")[3], 10);
    
    useEffect(() => {
        const fetchRentals = async () => {
            try {
                const rentals = await getAll<Rental>(API_RENTALS_BY_VEHICLE_URL + `/${vehicleId}`);
                setRentalData(rentals);
            } catch (error) {
                setError("Error fetching rentals - " + error);
            } finally {
                setLoading(false);
            }
        };

        const fetchVehicleById = async () => {
            setLoading(false);
            try{
                let data = await getById<Vehicle>(API_VEHICLES_URL, vehicleId);
                if(data){
                    if(isCar(data)){
                        data = await getById<Car>(API_CARS_URL, vehicleId);
                        if(data.purchaseDate) {
                            data.purchaseDate = new Date(data.purchaseDate);
                        }
                    }
                    else if(isBicycle(data)){
                        data = await getById<Bicycle>(API_BICYCLES_URL, vehicleId);
                    }
                    else if(isScooter(data)){
                        data = await getById<Scooter>(API_SCOOTERS_URL, vehicleId);
                    }
                    console.log(JSON.stringify(data));
                    setVehicle(data as Car);
                }
        
            } catch(error){
                setError("Error fetching vehicle - " + error);
            } finally{
                setLoading(false);
            }

        }

        const fetchMalfunctionsByVehicle = async () => {
            const data = await getAll<Malfunction>(`${API_VEHICLE_MALFUNCTIONS_URL}/${vehicleId}`);
            setMalfunctionsData(data);
        }


        fetchVehicleById();
        fetchMalfunctionsByVehicle();
        fetchRentals();
    }, []);
    if (loading) return <div>Loading... Please wait...</div>;
    if (error) return <div>{error}</div>;


    const openManufacturerModal = () => {
        setManufacturerModalVisible(true);
    };

    const renderHeaderTitleChildren = () => {
        return <h3>Vehicle details</h3>;
    }
    const headerTitleChildren = renderHeaderTitleChildren();

    const renderAdditionalFields = () => {
        if(isCar(vehicle)) {
            return (
                <div key={vehicle.carId}>
                    <h4>Car ID</h4>
                    <InputText value={vehicle.carId} readOnly={true} className='input-container--input' />
                    <h4>Purchase date</h4>
                    <InputText id="purchaseDate" value={vehicle.purchaseDate} readOnly className='input-container--input' />
                    <h4>Car ID</h4>
                    <InputTextarea value={vehicle.description} readOnly={true} rows={5} cols={30} className='input-container--input' />
                </div>
            );
        }
        else if(isBicycle(vehicle)) {
            return (
                <div key={vehicle.bicycleId}>
                    <h4>Bicycle ID</h4>
                    <InputText value={vehicle.bicycleId} readOnly={true} className='input-container--input' />
                    <h4>Riding autonomy</h4>
                    <InputText value={vehicle.ridingAutonomy} readOnly={true} className='input-container--input' />
                </div>
            );
            
        }
        else if (isScooter(vehicle)){
            return (
                <div key={vehicle.scooterId}>
                    <h4>Scooter ID</h4>
                    <InputText value={vehicle.scooterId} readOnly={true} className='input-container--input' />
                    <h4>Max speed</h4>
                    <InputText value={vehicle.maxSpeed} readOnly={true} className='input-container--input' />
                </div>
            );
            
        }
    };

    const renderBasicDetailsContent = () => {
        return (
            <>
                <div className="basic-details__content__image">
                    <Image src={vehicle.image?.url} indicatorIcon={icon} alt="Image" style={{ width: '100%', height: '100%', objectFit: 'cover' }}  preview />
                </div>
                <div className="basic-details__content__fields">
                    {/* ID */}
                    <div className="basic-details__content__fields--field">
                        <label htmlFor="id" className="font-bold">ID</label>
                        <InputText id="id" value={vehicle.id?.toString()} readOnly />
                    </div>

                    {/* Model */}
                    <div className="basic-details__content__fields--field">
                        <label htmlFor="model" className="font-bold">Model</label>
                        <InputText id="model" value={vehicle?.model} readOnly />
                    </div>

                    {/* Price */}
                    <div className="basic-details__content__fields--field">
                        <label htmlFor="price" className="font-bold">Price</label>
                        <InputText id="price" value={vehicle?.price} readOnly />
                    </div>

                    {/* Manufacturer */}
                    <div className="basic-details__content__fields--field">
                        <label htmlFor="manufacturer" className="font-bold">Manufacturer</label>
                        <Button icon={<FontAwesomeIcon icon={faExternalLink} style={{ color: "var(--color-secondary)" }} />} 
                            text outlined tooltip="Details" onClick={openManufacturerModal} />
                    </div>

                    {/* Status */}
                    <div className="basic-details__content__fields--field">
                        <label htmlFor="status" className="font-bold">Status</label>
                        <InputText id="status" value={vehicle?.rentalStatus.status} readOnly />
                    </div>

                    {isCar(vehicle) && (
                        <div>
                            <InputText id="purchaseDate" value={vehicle.purchaseDate} readOnly />
                        </div>
                    )}

                    {/* TODO: Promotions, Acitivities */}
                </div>
            </>
        );
    }
    const basicDetailsContent = renderBasicDetailsContent();

    const renderAdditionalDetailsContent = () => {
        return (
            <>
                {/* Additional Details */}
                <div className='input-container'>
                    {renderAdditionalFields()}
                </div>
            </>
        );
    }
    const additionalDetailsContent = renderAdditionalDetailsContent();

    const showMalfunctionsModal = () => {
        setMalfunctionModalVisible(true);
    }

    const onDeleteMalfunction = (rowData: Malfunction) => {
        setSelectedMalfunction(rowData);
        setDeleteMalfunctionModalVisible(true);
    }

    const handleDeleteMalfunction = async (rowData: Malfunction) => {
        try{
            const deleted = await remove(API_MALFUNCTIONS_URL, rowData.id);
            if(deleted) {
                setMalfunctionsData(prev => prev.filter(m => m.id !== rowData.id));
                showSuccess("Deleted malfunction", "Malfunction was successfully deleted");
            }
            else{
                showError("Error", "Error while deleting malfunction for vehicle");
            }
        } catch(error) {
            showError("Error", `An error occured: ${error}`);
        } 

        setDeleteMalfunctionModalVisible(false);
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

    const actionsBodyTemplate = (rowData: Malfunction) => {
        return (
            <div className="action-buttons">
                
                <Button 
                    icon={ <FontAwesomeIcon icon={faTrashAlt}  /> }
                    tooltip="Delete malfunction"
                    rounded outlined
                    style={{ color: 'var(--color-red)' }}
                    onClick={() => onDeleteMalfunction(rowData)}
                />
            </div> 
        );
    };

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
        { header: 'Actions', body: actionsBodyTemplate }
    ];

    const openDetailsModal = (data: any, title: string) => {
        setSelectedRentalChildData(data);
        setRentalDetailsModalTitle(title);
        setRentalDetailsModalVisible(true);
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
        { field: 'client', header: 'Client', body: (rowData) => detailsButtonTemplate(rowData, "client", "Client Details") },
        { field: 'rentalDateTime', header: 'Time of rental' },
        { field: 'duration', header: 'Duration (hours)' },
        { field: 'pickupLocation', header: 'Pickup location', body: (rowData) => detailsButtonTemplate(rowData, "pickupLocation", "Pickup location")},
        { field: 'dropoffLocation', header: 'Dropoff location', body: (rowData) => detailsButtonTemplate(rowData, "dropoffLocation", "Dropoff location")},
    ];

    const getDisplayConfig = () => {
        if (!selectedRentalChildData) return [];
        
        // Check based on the presence of type-specific fields
        if ('username' in selectedRentalChildData) {
            return clientDisplayConfig;
        } else if ('model' in selectedRentalChildData) {
            return vehicleDisplayConfig;
        } else if ('latitude' in selectedRentalChildData) {
            return locationDisplayConfig;
        }
        return [];
    }

    const renderMainContent = () => {
            return (
                <>
                    <div className="vehicle-details">
                        <RowTitleContentContainer title='Basic details' content={basicDetailsContent} />

                        <RowTitleContentContainer title='Additional details' content={additionalDetailsContent} />
                    </div>

                    <div>
                        <CustomDataTable 
                            value={malfunctionsData}
                            dataKey="id"
                            columns={malfunctionColumns}
                            header={
                                <div style={{ display: 'flex', gap: '10px', justifyContent: 'center', alignItems: 'center' }}>
                                    <h4>Malfunctions of vehicle</h4>
                                    <Button 
                                        icon={ <FontAwesomeIcon icon={faAdd} /> }
                                        tooltip="Add malfunction"
                                        rounded outlined
                                        onClick={() => showMalfunctionsModal()}
                                    />
                                </div>
                            }
                            paginator
                            rows={5}
                            rowsPerPageOptions={[5, 10, 25]}
                        />

                        <AddMalfunctionDialog visible={malfunctionModalVisible} onHide={() => setMalfunctionModalVisible(false)} vehicle={vehicle} onAdd={addMalfunction} />
                        <ConfirmationDialog visible={deleteMalfunctionModalVisible} onHide={() => setDeleteMalfunctionModalVisible(false)} header="Delete malfunction" children="Are you sure you want to delete this malfunction?" 
                            onNoAction={() => setDeleteMalfunctionModalVisible(false)} onYesAction={() => handleDeleteMalfunction(selectedMalfunction)} />
                    </div>

                    <div>
                        <CustomDataTable 
                            dataKey="id" 
                            value={rentalData} 
                            columns={rentalColumns}   
                            header="Rentals by vehicle" 
                            paginator={true}        
                        />
                        <InfoDisplayDialog 
                            header={rentalDetailsModalTitle}
                            visible={rentalDetailsModalVisible}
                            onHide={() => setRentalDetailsModalVisible(false)}
                            data={selectedRentalChildData}
                            config={getDisplayConfig()}
                        />
                    </div>

                    <InfoDisplayDialog 
                        header="Manufacturer details"
                        visible={manufacturerModalVisible}
                        onHide={() => setManufacturerModalVisible(false)}
                        data={vehicle.manufacturer}
                        config={manufacturerDisplayConfig}
                    />
                </>
            );
        }
        const mainContent = renderMainContent();


    return(
        <PagesContainer headerTitleChildren={headerTitleChildren} headerButtonsChildren={undefined} mainContentChildren={mainContent} />
    );
}

export default VehicleDetailsPage;