import './VehicleManagementPage.css'
import { useEffect, useRef, useState } from "react";
import { faAdd, faExternalLink, faEye } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Button } from "primereact/button";
import { FileUpload, FileUploadHandlerEvent } from "primereact/fileupload";
import { FilterMatchMode } from "primereact/api";
import { Dropdown, DropdownChangeEvent } from "primereact/dropdown";
import { Tag } from "primereact/tag";
import { Image } from 'primereact/image';

import carImage from "@assets/electric-car.png";
import bicycleImage from "@assets/electric-bicycle.png";
import scooterImage from "@assets/electric-scooter.png";
import { getAll, insert, remove, update } from '@api/apiService';
import ColumnConfig from "@components/tables/CustomDataTable";
import { Bicycle, Car, RentalStatus, Scooter, Vehicle, VehicleType } from "types/types";
import Searchbar from "@components/Searchbar";
import PagesContainer from "@components/admin/PagesContainer";
import { VehicleDataEntryModal } from '@components/admin/VehicleDataEntryModal';
import EditableTable from '@components/tables/EditableTable';
import { API_BICYCLES_URL, API_CARS_URL, API_SCOOTERS_URL, API_VEHICLES_CSV_URL, API_VEHICLES_URL } from '@utils/constants/ApiLinks';
import { useToast } from '@hooks/useToast';
import { isBicycle, isCar, isScooter } from '@utils/typeChecker';
import { emptyVehicleObject } from '@utils/constants/emptyObjects';
import InfoDisplayDialog from '@components/dialogs/InfoDisplayDialog';
import { manufacturerDisplayConfig } from '@utils/constants/objectConfigs';
import { CustomError } from 'types/classes';
import { format } from 'date-fns';

function VehicleManagementPage() {
    const { showSuccess, showError } = useToast();
    const numberOfRows = 5;
    const rowsPerPage = [5, 10, 15, 20];
    const emptyVehicle = emptyVehicleObject;
    const vehicleTypes: VehicleType[] = [
        { name: "All vehicles", imgUrl: '' },
        { name: "Car", imgUrl: carImage  },
        { name: "Bicycle", imgUrl: bicycleImage },
        { name: "Scooter", imgUrl: scooterImage },
    ];
    
    // ------- TABLE CONSTANTS BEGIN ------- 
    const [filters, setFilters] = useState({
        global: { value: null, matchMode: FilterMatchMode.CONTAINS },
        model: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
        price: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
        type_: { value: null, matchMode: FilterMatchMode.EQUALS }
    });
    const [selectedVehicleData, setSelectedVehicleData] = useState<Vehicle | null>(emptyVehicle);
    const [selectedVehicleType, setVehicleType] = useState<VehicleType | null>(null);
    const [allVehiclesData, setAllVehiclesData] = useState<Vehicle[]>([]);
    const [vehiclesByTypeData, setVehiclesByTypeData] = useState<Vehicle[]>([]);
    const [globalFilterValue, setGlobalFilterValue] = useState('');
    const [manufacturerModalVisible, setManufacturerModalVisible] = useState<boolean>(false);
    const [addVehicleModalVisible, setAddVehicleModalVisible] = useState<boolean>(false);
    const [updateVehicleModalVisible, setUpdateVehicleModalVisible] = useState<boolean>(false);
    const [formData, setFormData] = useState<FormData>(new FormData());
    const fileUploadRef = useRef<FileUpload>(null);

    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchVehicles = async () => {
            setLoading(true);
            try {
                const data = await getAll<Vehicle>(API_VEHICLES_URL);
                const _tempData = data.map(vehicle => {
                    if(isCar(vehicle)){
                        return { ...vehicle, purchaseDate: new Date(vehicle.purchaseDate) } as Car;
                    } 
                    else if(isBicycle(vehicle)) return vehicle as Bicycle;
                    else return vehicle as Scooter;
                });
                
                setAllVehiclesData(_tempData);  // Set the fetched data to state
                setVehiclesByTypeData(_tempData);
            } catch (error) {
                setError("Error fetching vehicles - " + error);
            } finally {
                setLoading(false);
            }
        };

        fetchVehicles();
    }, []);

    if(loading) return <p>Loading vehicles... Please wait</p>
    // if(error){
    //     return <p>{error}</p>
    // }

    const navigateToVehicleDetails = (rowData: Vehicle) => {
        const currentPath = window.location.pathname;
        const url = `${currentPath}/${rowData.id}/details`; // Construct the route
        window.open(url, '_blank');
    }

    const getSeverity = (value: RentalStatus) => {
        switch(value.status){
            case 'Free': return 'success';
            case 'Rented': return 'warning';
            case 'Broken': return 'danger';
            default: return null;
        }
    };

    const onGlobalFilterChange = (e) => {
        const value = e.target.value;
        const _filters = { ...filters };
        
        _filters['global'].value = value;

        setFilters(_filters);
        setGlobalFilterValue(value);
    };

    const vehicleOptionsTemplate = (option: VehicleType) => {
        return (
            <div style={{ display: "flex", alignItems: "center" }}>
                { option.imgUrl !== '' && (
                    <img
                        alt={option.name}
                        src={option.imgUrl}
                        style={{ width: "20px", height: "20px", marginRight: "8px" }}
                    />
                )}
                <span>{option.name}</span>
            </div>
        );
    };

    const onChangeVehicleType = (e: DropdownChangeEvent) => {
        const selectedType = e.value;
        setVehicleType(selectedType);

        if(selectedType?.name !== 'All vehicles') {
            const filteredData = allVehiclesData.filter(v => v.type_ === selectedType?.name);
            setVehiclesByTypeData(filteredData);
        }
        else {
            setVehiclesByTypeData(allVehiclesData);
        }
        
    }

    const renderTableHeader = () => {
        return (
            <div className="vehicles-container__table-header">
                <div className="vehicles-container__table-header_combo-box">
                    <Dropdown value={selectedVehicleType} onChange={(e: DropdownChangeEvent) => onChangeVehicleType(e)}
                        options={vehicleTypes} optionLabel="name" placeholder="Select a type" className="w-full md:w-14rem"
                        itemTemplate={vehicleOptionsTemplate}
                    />
                </div>
                <Searchbar filterValue={globalFilterValue} onFilterChange={onGlobalFilterChange} />
            </div>
        );
    }
    const tableHeader = renderTableHeader();

    

    const openManufacturerModal = (rowData: Vehicle) => {
        setSelectedVehicleData(rowData);
        setManufacturerModalVisible(true);
    };

    
    const imageBodyTemplate = (rowData: Vehicle) => {
        const imgFormat = rowData.image?.type.split("/")[1];

        return <Image src={rowData.image?.url} alt={'img.' + imgFormat} width='60px' height='60px' preview />
    };
    
    const manufacturerCellBodyTemplate = (rowData: Vehicle) => {
        return (
            <Button icon={<FontAwesomeIcon icon={faEye} style={{ color: "var(--color-secondary)" }} />} 
                text 
                outlined 
                tooltip="View Details" 
                onClick={() => openManufacturerModal(rowData)} 
            /> 
        );
    };
        
    const statusBodyTemplate = (rowData: Vehicle) => {
        let _color = 'var(--color-green)';  // Free
        if(rowData.rentalStatus.status === 'Rented') _color = 'var(--color-orange)';
        else if(rowData.rentalStatus.status === 'Broken') _color = 'var(--color-red)';

        return <Tag value={rowData.rentalStatus.status} severity={getSeverity(rowData.rentalStatus)} style={{ background: _color }} />
    };

    const vehicleDetailsBodyTemplate = (rowData: Vehicle) => {
        return(
            <div>
                <Button icon={ <FontAwesomeIcon icon={faExternalLink} /> } 
                    text outlined tooltip="View Details" style={{ color: "var(--color-secondary)" }} onClick={() => navigateToVehicleDetails(rowData)} 
                />
            </div>
        );
    }

    const vehicleColumns: ColumnConfig<Vehicle>[] = [
        { field: 'id', header: 'ID' },
        { field: 'image', header: 'Image', body: imageBodyTemplate },
        { field: 'model', header: 'Model' },
        { field: 'price', header: 'Price' },
        { field: 'rentalPrice', header: 'Rental price'},
        { field: 'manufacturer', header: 'Manufacturer', body: manufacturerCellBodyTemplate },
        { header: 'Rental status', body: statusBodyTemplate },
        { field: 'type_', header: 'Type' },
        { header: 'Details', body: vehicleDetailsBodyTemplate }
    ];

    // ------- TABLE CONSTANTS END -------

    const clearFileUploadRef = () => {
        if (fileUploadRef.current) {
            fileUploadRef.current.clear();
        }
    }

    const uploadFileToServer = async (file: File) => {
        const formData = new FormData();
        formData.set("csv", file);

        try {
            const response = await insert<FormData, Vehicle[]>(API_VEHICLES_CSV_URL, formData);

            setAllVehiclesData(prev => [...prev, ...response]);     // For all vehicles
            setVehiclesByTypeData(prev => [...prev, ...response]);  // For table

            showSuccess("Success!", "Added vehicle(s) from CSV file");

            // Clearing the file selection after successful upload
            clearFileUploadRef();
        } catch (error) {
            showError("Error", `${error}`);
            clearFileUploadRef();
        }
    };

    const onUploadCsv = (event: FileUploadHandlerEvent) => {
        const file = event.files[0];
        if (file) {
            uploadFileToServer(file);
        }
    };

    const showAddVehicleModal = () => {
        setAddVehicleModalVisible(true);
    }

    const showUpdateVehicleModal = (item: Vehicle) => {
        console.log("sel item: ", item);
        const updatedVehicle = allVehiclesData.find(v => v.id === item.id);
        if(updatedVehicle) setSelectedVehicleData(updatedVehicle);
        setUpdateVehicleModalVisible(true);
    }

    const renderHeaderTitleChildren = () => {
        return <h3>Vehicle Management</h3>;
    }
    const headerTitleChildren = renderHeaderTitleChildren();

    const renderHeaderButtonsChildren = () => {
        return (
            <>
                <FileUpload ref={fileUploadRef} chooseLabel="CSV" mode="basic" name="file" accept=".csv" customUpload uploadHandler={onUploadCsv}
                    maxFileSize={1000000}  />
                <Button label="Add" icon={ <FontAwesomeIcon icon={faAdd} /> } onClick={showAddVehicleModal} />
            </>
        ); 
    }
    const headerButtonsChildren = renderHeaderButtonsChildren();

    // Update existing vehicle in data table
    const updateVehicleTableData = (updatedVehicle: Vehicle) => {
        setAllVehiclesData(prevVehicles => prevVehicles.map(v => v.id === updatedVehicle.id 
            ? updatedVehicle 
            : v
        ));
        setVehiclesByTypeData(prevVehicles => prevVehicles.map(v => v.id === updatedVehicle.id 
            ? updatedVehicle 
            : v
        ));
        setUpdateVehicleModalVisible(false);
    };

    const addVehicleTableData = (insertedVehicle: Vehicle) => {
        setAllVehiclesData(prevVehicles => [ ...prevVehicles, insertedVehicle ]);
        setVehiclesByTypeData(prevVehicles => [ ...prevVehicles, insertedVehicle ]);
    }

    const getEndpoint = (item: Vehicle) => {
        let link = '';
        if(isCar(item)) link = API_CARS_URL;
        else if(isBicycle(item)) link = API_BICYCLES_URL;
        else if(isScooter(item)) link = API_SCOOTERS_URL;

        return link;
    }

    const addVehicle = async (item: Vehicle, formData?: FormData) => {
        item.isActive = true;
        const url = getEndpoint(item);
        let insertedVehicle = null;
        
        try{
            if(formData !== undefined){
                if(isCar(item)){
                    insertedVehicle = await insert<FormData, Car>(url, formData);
                }
                else if(isBicycle(item)){
                    insertedVehicle = await insert<FormData, Bicycle>(url, formData);
                }
                else if(isScooter(item)){
                    insertedVehicle = await insert<FormData, Scooter>(url, formData);
                }
            }
            
            if(insertedVehicle){
                showSuccess("Success", "Successfully added a vehicle");

                console.log("IMAGE: " + JSON.stringify(insertedVehicle.image));

                addVehicleTableData(insertedVehicle);
            }
            else{
                showError("Error", "Failed to add a vehicle");
            }
        } catch(error){
            if(error instanceof CustomError && error.isAxiosError) {
                showError("Error", error.message);   
            }
        } 
    }

    const updateVehicle = async (item: Vehicle, formData?: FormData) => {
        const url = getEndpoint(item);
        let updatedVehicle = null;

        try{
            if(formData != null){
                if(isCar(item)){
                    const newItem = {
                        ...item,
                        purchaseDate: format(item.purchaseDate, 'yyyy-MM-dd HH:mm:ss'),
                    }
                    formData?.set('vehicle', new Blob([JSON.stringify(newItem)], { type: "application/json" }));
                    updatedVehicle = await update<FormData, Car>(url, item.id, formData);
                }
                else if(isBicycle(item)){
                    updatedVehicle = await update<FormData, Bicycle>(url, item.id, formData);
                }
                else if(isScooter(item)){
                    updatedVehicle = await update<FormData, Scooter>(url, item.id, formData);
                }
            }

            if(updatedVehicle){
                showSuccess("Success", "Successfully updated vehicle");
                updateVehicleTableData(updatedVehicle);
            }
            else{
                showError("Error", "Failed to update vehicle");
            }
        } catch(error){
            if(error instanceof CustomError && error.isAxiosError) {
                showError("Error", error.message);   
            }
            else {
                showError("Error", `${error}`);   
            }
        }
    }

    const deleteVehicle = async (item: Vehicle) => {
        try{
            const deleted = await remove(API_VEHICLES_URL, item.id);
            if(deleted){
                showSuccess("Success", "Successfully deleted vehicle!");
            }
            else{
                showError("Error", "Failed to delete vehicle");
            }
        } catch(error){
            showError("Error", `${error}`);
        }
        
    }

    const renderTableChildren = () => {
        return (
            <>
                <EditableTable 
                    data={vehiclesByTypeData}
                    columns={vehicleColumns}
                    dataKey="id"
                    header={tableHeader}
                    rows={numberOfRows}
                    rowsPerPageOptions={rowsPerPage}
                    filters={filters}
                    onDelete={deleteVehicle}
                    onShowUpdateModal={showUpdateVehicleModal}
                />
                {/* <CustomDataTable 
                    value={vehiclesData} dataKey="id" columns={vehicleColumns}
                    header={tableHeader}
                    paginator={true} rows={numberOfRows} rowsPerPageOptions={rowsPerPage}
                    filters={filters}
                    emptyMessage="No vehicles found"
                /> */}

                <InfoDisplayDialog 
                    visible={manufacturerModalVisible} 
                    onHide={() => setManufacturerModalVisible(false)} 
                    header="Manufacturer details" 
                    data={selectedVehicleData?.manufacturer} 
                    config={manufacturerDisplayConfig}                
                />
                
                {/* Add modal */}
                <VehicleDataEntryModal 
                    header='Add a vehicle' 
                    visible={addVehicleModalVisible} 
                    onHide={() => setAddVehicleModalVisible(false)} 
                    isUpdating={false} 
                    onAdd={addVehicle} 
                    formData={formData} 
                />
                
                {/* Update modal */}
                <VehicleDataEntryModal 
                    header='Update vehicle' 
                    visible={updateVehicleModalVisible} 
                    onHide={() => setUpdateVehicleModalVisible(false)} 
                    isUpdating={true} 
                    vehicleToUpdate={selectedVehicleData} 
                    onUpdate={updateVehicle}
                    formData={formData}
                />
            </>
        );
    }
    const tableChildren = renderTableChildren();

    return(
        <PagesContainer headerTitleChildren={headerTitleChildren} headerButtonsChildren={headerButtonsChildren} mainContentChildren={tableChildren} />
    );
}
   
export default VehicleManagementPage;