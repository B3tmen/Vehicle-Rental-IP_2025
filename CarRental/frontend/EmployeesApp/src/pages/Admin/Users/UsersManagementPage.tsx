import './UsersManagementPage.css'
import { useEffect, useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faAdd } from '@fortawesome/free-solid-svg-icons';
import { Button } from 'primereact/button';
import { Divider } from 'primereact/divider';
import { FilterMatchMode } from 'primereact/api';
import { Tag } from 'primereact/tag'; 
import { Image } from 'primereact/image';

import PagesContainer from '@components/admin/PagesContainer';
import Searchbar from '@components/Searchbar';
import ColumnConfig from '@components/tables/CustomDataTable';
import EditableTable from '@components/tables/EditableTable';
import { API_CLIENTS_URL, API_EMPLOYEES_URL } from '@utils/constants/ApiLinks';
import { getAll, insert, remove, update } from '@api/apiService';
import { UserDataEntryModal } from '@components/admin/UserDataEntryModal';
import { useToast } from '@hooks/useToast';
import { Client, Employee, User, UserSubType } from 'types/types';
import { isClient, isEmployee } from '@utils/typeChecker';
import { CustomError } from 'types/classes';
import { emptyUserObject } from '@utils/constants/emptyObjects';

function UsersManagementPage() {
    const emptyUser: UserSubType = emptyUserObject; 

    //const { clientsData } = useClients();
    const { showSuccess, showError } = useToast();

    // TODO: Make universal filters
    const [filtersClients, setFiltersClients] = useState({
        global: { value: null, matchMode: FilterMatchMode.CONTAINS },
        username: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
        firstName: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
        lastName: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
        personalCardNumber: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
    });
    const [globalFilterValueClients, setGlobalFilterValueClients] = useState('');
    const [filtersEmployees, setFiltersEmployees] = useState({
        global: { value: null, matchMode: FilterMatchMode.CONTAINS },
        username: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
        firstName: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
        lastName: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
        role: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
    });
    const [globalFilterValueEmployees, setGlobalFilterValueEmployees] = useState('');
    const [clientsData, setClientsData] = useState<Client[]>([]);
    const [employeesData, setEmployeesData] = useState<Employee[]>([]);
    const [userToUpdate, setUserToUpdate] = useState<User| null>(emptyUser);
    const [userModalVisible, setUserModalVisible] = useState<boolean>(false);
    const [userUpdateModalVisible, setUserUpdateModalVisible] = useState<boolean>(false);
    const [formData, setFormData] = useState<FormData>(new FormData());

    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);
    
    useEffect(() => {
        const fetchUsers = async () => {
            setLoading(true);
            try {
                const _clientsData = await getAll<Client>(API_CLIENTS_URL);
                const _employeesData = await getAll<Employee>(API_EMPLOYEES_URL);
                
                setClientsData(_clientsData);
                setEmployeesData(_employeesData);
            } catch (error) {
                setError("Error fetching users - " + error);
            } finally {
                setLoading(false);
            }
        };

        fetchUsers();
    }, []);

    if(loading) return <p>Loading users... Please wait</p>
    //if(error) return <p>{error}</p>

    const getSeverity = (value: boolean) => {
        if(value){
            return 'success';
        }
        return 'danger';
    };

    const getStatus = (value: boolean) => {
        if(value){
            return 'Active';
        }
        return 'Blocked';
    };

    const onGlobalFilterChangeClients = (e) => {
        const value = e.target.value;
        const _filters = { ...filtersClients };

        _filters['global'].value = value;

        setFiltersClients(_filters);
        setGlobalFilterValueClients(value);
    };

    const onGlobalFilterChangeEmployees = (e) => {
        const value = e.target.value;
        const _filters = { ...filtersEmployees };

        _filters['global'].value = value;

        setFiltersEmployees(_filters);
        setGlobalFilterValueEmployees(value);
    };

    const avatarBodyTemplate = (rowData: Client) => {
        const imgFormat = rowData.avatarImage?.type.split("/")[1];

        return (
            <Image src={rowData.avatarImage?.url} alt={'img.' + imgFormat} width='60px' height='60px' preview />
        );
    }

    const statusBodyTemplate = (rowData: User) => {
        let _color = 'var(--color-green)';  // Free
        if(!rowData.isActive) _color = 'var(--color-red)';

        return <Tag value={getStatus(rowData.isActive)} severity={getSeverity(rowData.isActive)} style={{ background: _color }} />
    };

    const renderHeaderTitle = () => {
        return(
            <h3>Users Management</h3>
        );
    }
    const headerTitle = renderHeaderTitle();

    const renderHeaderButtons = () => {
        return(
            <>
                <Button 
                    label="Add User" 
                    icon={<FontAwesomeIcon icon={faAdd} />}  
                    onClick={() => setUserModalVisible(true)}
                />
            </>
        );
    }
    const headerButtons = renderHeaderButtons();

    const renderTableHeaderClients = () => {
        return(
            <div className="users__table-header">
                <h3>Clients</h3>
                <Searchbar filterValue={globalFilterValueClients} onFilterChange={onGlobalFilterChangeClients} />
            </div>
        );
    }
    const renderTableHeaderEmployees = () => {
        return(
            <div className="users__table-header">
                <h3>Employees</h3>
                <Searchbar filterValue={globalFilterValueEmployees} onFilterChange={onGlobalFilterChangeEmployees} />
            </div>
        );
    }
    const tableHeaderClients = renderTableHeaderClients();
    const tableHeaderEmployees = renderTableHeaderEmployees();

    const clientColumns: ColumnConfig<Client>[] = [
        { field: 'id', header: 'ID' },
        { field: 'avatarImage', header: 'Avatar', body: avatarBodyTemplate},
        { field: 'username', header: 'Username'},
        { field: 'firstName', header: 'First name' },
        { field: 'lastName', header: 'Last name'},
        { field: 'personalCardNumber', header: 'JMBG'},
        { field: 'email', header: 'Email' },
        { field: 'phoneNumber', header: 'Phone number'},
        { field: 'isActive', header: 'Account status', body: statusBodyTemplate},
        { field: 'driversLicence', header: 'Drivers licence'}
    ];

    const employeeColumns: ColumnConfig<Employee>[] = [
        { field: 'id', header: 'ID' },
        { field: 'username', header: 'Username'},
        { field: 'firstName', header: 'First name' },
        { field: 'lastName', header: 'Last name'},
        { field: 'role', header: 'Role'},
        { field: 'isActive', header: 'Account status', body: statusBodyTemplate},
    ];

    const onShowUpdateModal = (item: UserSubType) => {
        // setUserToUpdate(item);
        // console.log("USER_UPD: ", item);
        // setUserUpdateModalVisible(true);
        const updatedUser = clientsData.find(user => user.id === item.id) || employeesData.find(user => user.id === item.id);
        setUserToUpdate(item);  // Ensure it's the latest reference
        setUserUpdateModalVisible(true);
    }

    // Add a user to the data table
    const addToClientTableData = (insertedClient: Client) => {
        setClientsData(prevClients => [...prevClients, insertedClient]);
    }
    const addToEmployeeTableData = (insertedEmployee: Employee) => {
        setEmployeesData(prevEmployees => [...prevEmployees, insertedEmployee]);
    }

    // Update existing user in data table
    const updateClientTableData = (updatedClient: Client) => {
        setClientsData(prevClients => prevClients.map(c => c.id === updatedClient.id 
            ? updatedClient 
            : c
        ));
    };
    const updateEmployeeTableData = (updatedEmployee: Employee) => {
        setEmployeesData(prevEmployees => prevEmployees.map(e => e.id === updatedEmployee.id 
            ? updatedEmployee 
            : e
        ));
    };

    const getEndpoint = (item: User) => {
        return isClient(item) ? API_CLIENTS_URL : API_EMPLOYEES_URL;
    }

    const addUser = async (item: User, formData?: FormData) => {
        const link = getEndpoint(item);
        let insertedUser = null;
        try{
            if(isEmployee(item)){
                insertedUser = await insert<Employee, Employee>(link, item);
            }
            else if(isClient(item)){
                if(formData !== undefined) 
                    insertedUser = await insert<FormData, Client>(link, formData);
            }

            if(insertedUser){
                showSuccess("Success", "Successfully added a user");
                if(isClient(insertedUser)){
                    addToClientTableData(insertedUser);
                }
                else if(isEmployee(insertedUser)){
                    addToEmployeeTableData(insertedUser);
                }
            }
            else{
                showError("Error", "Failed to add a user");
            }
        } catch(error){
            if(error instanceof CustomError && error.isAxiosError) {
                showError("Error", error.message);   
            }
        } 

        //setUserModalVisible(false);
    }

    const updateUser = async (item: User, formData?: FormData) => {
        const link = isEmployee(item) ? API_EMPLOYEES_URL : API_CLIENTS_URL;
        let updatedUser = null;

        try{
            if(isEmployee(item)){
                updatedUser = await update<Employee, Employee>(link, item.id, item);
            }
            else if(isClient(item)){
                if(formData !== undefined) updatedUser = await update<FormData, Employee>(link, item.id, formData);
            }
            
            if(updatedUser){
                showSuccess("Success", "Successfully updated user");
                if(isClient(updatedUser)){
                    updateClientTableData(updatedUser);
                }
                else if(isEmployee(updatedUser)){
                    updateEmployeeTableData(updatedUser);
                }
            }
            else{
                showError("Error", "Failed to update user");
            }
        } catch(error) {
            if(error instanceof CustomError && error.isAxiosError) {
                showError("Error", error.message);   
            }

        }

        //setUserUpdateModalVisible(false);
    }

    const deleteUser = async (item: UserSubType) => {
        const link = item.type === 'Employee' ? API_EMPLOYEES_URL : API_CLIENTS_URL;
        const deleted = await remove(link, item.id);
        if(deleted){
            showSuccess("Success", "Successfully deleted user!");
        }
        else{
            showError("Error", "Failed to delete user");
        }
    }
    

    const renderMainContent = () => {
        return (
            <>
                <div className='users__content-container'>
                    {/* Clients table */}
                    <EditableTable
                        data={clientsData}
                        columns={clientColumns}
                        dataKey="id"
                        header={tableHeaderClients}
                        filters={filtersClients}
                        //onUpdate={updateUser}
                        onDelete={deleteUser}
                        onShowUpdateModal={onShowUpdateModal}
                    />

                    <Divider />

                    {/* Employees table */}
                    <EditableTable
                        data={employeesData}
                        columns={employeeColumns}
                        dataKey="id"
                        header={tableHeaderEmployees}
                        filters={filtersEmployees}
                        //onUpdate={updateUser}
                        onDelete={deleteUser}
                        onShowUpdateModal={onShowUpdateModal}
                    />
                </div>
            </>
        );
    }
    const mainContent = renderMainContent();

    return (
        <>
            <PagesContainer headerTitleChildren={headerTitle} headerButtonsChildren={headerButtons} mainContentChildren={mainContent} />

            {/* Add modal */}
            <UserDataEntryModal visible={userModalVisible} onHide={() => setUserModalVisible(false)} formData={formData} onAdd={addUser}
            />

            {/* Update modal */}
            <UserDataEntryModal visible={userUpdateModalVisible} onHide={() => setUserUpdateModalVisible(false)} isUpdating={true} userToUpdate={userToUpdate} onUpdate={updateUser}
                formData={formData} 
            />
        </>
    );    
}

export default UsersManagementPage;