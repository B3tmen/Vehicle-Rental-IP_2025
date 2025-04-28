import './ManufacturersPage.css'
import { useEffect, useState } from 'react';
import { faAdd } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Button } from "primereact/button";
import { FilterMatchMode } from 'primereact/api';

import ManufacturerInputDialog from '@components/dialogs/ManufacturerInputDialog';
import EditableTable from '@components/tables/EditableTable';
import PagesContainer from '@components/admin/PagesContainer';
import ColumnConfig from '@components/tables/CustomDataTable';
import Searchbar from '@components/Searchbar';
import { Manufacturer } from 'types/types';
import { API_MANUFACTURERS_URL } from '@utils/constants/ApiLinks';
import { getAll, insert, remove, update } from '@api/apiService';
import { useToast } from '@hooks/useToast';
import { emptyManufacturerObject } from '@utils/constants/emptyObjects';

function ManufacturersPage(){
    const emptyManufacturer: Manufacturer = emptyManufacturerObject;

    const { showSuccess, showError } = useToast();
    const [data, setData] = useState<Manufacturer[]>([]);
    const [manufacturer, setManufacturer] = useState<Manufacturer>(emptyManufacturer);
    const [addDialogVisible, setAddDialogVisible] = useState(false);
    const [updateDialogVisible, setUpdateDialogVisible] = useState(false);
    const [filters, setFilters] = useState({
        global: { value: null, matchMode: FilterMatchMode.CONTAINS },
        name: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
        state: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
        address: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
        phoneNumber: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
        fax: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
        email: { value: null, matchMode: FilterMatchMode.CONTAINS },
    });
    const [globalFilterValue, setGlobalFilterValue] = useState('');


    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchManufacturers = async () => {
            setLoading(true);
            try {
                const data = await getAll<Manufacturer>(API_MANUFACTURERS_URL);
                setData(data);  // Set the fetched data to state
            } catch (error) {
                setError("Error fetching manufacturers - " + error);
            } finally {
                setLoading(false);
            }
        };

        fetchManufacturers();
    }, []);  // Empty dependency array ensures this runs once when the component mounts

    if (loading) return <div>Loading...</div>;
    if (error) return <div>{error}</div>;



    const onGlobalFilterChange = (e) => {
        const value = e.target.value;
        const _filters = { ...filters };

        _filters['global'].value = value;

        setFilters(_filters);
        setGlobalFilterValue(value);
    };

    const addManufacturer = async (newManufacturer: Manufacturer) => {
        try {
            const response = await insert<Manufacturer, Manufacturer>(API_MANUFACTURERS_URL, newManufacturer);
            setData((prevData) => [...prevData, response]); // Append new manufacturer
            showSuccess("Success", "Successfully added manufacturer!");
        } 
        catch (error) {
            throw new Error(error.message)
        }
    };

    const updateManufacturer = async (updatedManufacturer: Manufacturer) => {
        const updated = await update<Manufacturer, Manufacturer>(API_MANUFACTURERS_URL, updatedManufacturer.id, updatedManufacturer);
        if(updated){
            setData((prevData) => prevData.map(m => (m.id === updated.id ? updated : m))); // Replace the updated manufacturer
            showSuccess("Success", "Successfully updated manufacturer!");
        }
        else{
            showError("Error", "Failed to update manufacturer");
        }

        setUpdateDialogVisible(false);
    }

    const deleteManufacturer = async (item: Manufacturer) => {
        const deleted = await remove(API_MANUFACTURERS_URL, item.id);
        if(deleted){
            showSuccess("Success", "Successfully deleted manufacturer!");
        }
        else{
            showError("Error", "Failed to delete manufacturer");
        }
    }

    const showUpdateModal = (manuf: Manufacturer) => {
        setManufacturer(manuf);
        setUpdateDialogVisible(true);
    }


    // ---------------------------------------------------------------------------------------------

    const renderHeaderTitleChildren = () => {
        return <h3>Manufacturers</h3>;
    }
    const headerTitleChildren = renderHeaderTitleChildren();

    const renderHeaderButtonsChildren = () => {
        return <Button label="Add" icon={ <FontAwesomeIcon icon={faAdd} /> } onClick={() => setAddDialogVisible(true)} />;
    }
    const headerButtonsChildren = renderHeaderButtonsChildren();

    const renderTableHeader = () => {
        return(
            <div className="manufacturers-container__table-header">
                <Searchbar filterValue={globalFilterValue} onFilterChange={onGlobalFilterChange} />
            </div>
        );
    }
    const tableHeader = renderTableHeader();

    const manufacturerColumns: ColumnConfig<Manufacturer>[] = [
        { field: "id", header: "ID" },
        { field: "name", header: "Name" },
        { field: "state", header: "State" },
        { field: "address", header: "Address" },
        { field: "phoneNumber", header: "Phone Number" },
        { field: "fax", header: "Fax" },
        { field: "email", header: "Email" },
    ];


    const renderTableChildren = () => {
        return (
            <EditableTable<Manufacturer>
                data={data}
                columns={manufacturerColumns}
                dataKey="id"
                header={tableHeader}
                filters={filters}
                onDelete={deleteManufacturer}
                onUpdate={updateManufacturer}
                onShowUpdateModal={showUpdateModal}
            />
        );
    }
    const tableChildren = renderTableChildren();

    const renderManufacturerModals = () => {
        return (
            <>
                <ManufacturerInputDialog 
                    visible={addDialogVisible}
                    onHide={() => setAddDialogVisible(false)}
                    header="Add a manufacturer"
                    onFinish={addManufacturer} // Pass the function to the modal
                />
                <ManufacturerInputDialog 
                    visible={updateDialogVisible}
                    onHide={() => setUpdateDialogVisible(false)}
                    header="Update a manufacturer"
                    manufacturer={manufacturer}
                    onFinish={updateManufacturer} // Pass the function to the modal
                />
            </>
            

        );
    }
    const addManufacturerModal = renderManufacturerModals();

    return(
        <>
            <PagesContainer headerTitleChildren={headerTitleChildren} headerButtonsChildren={headerButtonsChildren} mainContentChildren={tableChildren} />
            
            {addManufacturerModal}
        </>
    );
}

export default ManufacturersPage;