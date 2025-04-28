import './ClientsPage.css'
import PagesContainer from "@components/admin/PagesContainer";
import { Client } from "types/types";
import { API_CLIENTS_URL } from "@utils/constants/ApiLinks";
import { useEffect, useState } from "react";
import { getAll, update } from "@api/apiService";
import AddableTable from "@components/tables/AddableTable";
import ColumnConfig from "@components/tables/CustomDataTable";
import { Image } from "primereact/image";
import { Tag } from "primereact/tag";
import { InputSwitch, InputSwitchChangeEvent } from "primereact/inputswitch";
import { useToast } from '@hooks/useToast';

function ClientsPage() {
    const { showSuccess, showError } = useToast();
    const [data, setData] = useState<Client[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchClients = async () => {
            setLoading(true);
            try {
                const _data = await getAll<Client>(API_CLIENTS_URL);
                
                setData(_data);
            } catch (error) {
                setError("Error fetching clients - " + error);
            } finally {
                setLoading(false);
            }
        };

        fetchClients();
    }, []);

    if(loading) return <p>Loading clients... Please wait</p>
    if(error) return <p>{error}</p>

    const avatarBodyTemplate = (rowData: Client) => {
        const imgFormat = rowData.avatarImage?.type.split("/")[1];

        return (
            <Image src={rowData.avatarImage?.url} alt={'img.' + imgFormat} width='60px' height='60px' preview />
        );
    }

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

    const statusBodyTemplate = (rowData: Client) => {
        let _color = 'var(--color-green)';  // Free
        if(!rowData.isActive) _color = 'var(--color-red)';

        return <Tag value={getStatus(rowData.isActive)} severity={getSeverity(rowData.isActive)} style={{ background: _color }} />
    };

    const switchActiveStatus = async (e: InputSwitchChangeEvent, client: Client) => {
        const status = e.value;
        client.isActive = status;
        try{
            const updatedClient = await update<Client, Client>(API_CLIENTS_URL, client.id, client);
            const statusUpdate = !status ? 'blocked' : 'activated';

            if(updatedClient){
                const updatedClients = data.map((c) =>
                    c.id === client.id ? { ...c, isActive: status } : c
                );
                setData(updatedClients);

                showSuccess("Success", `Successfully ${statusUpdate} client`);
            }
            else{
                showError("Error", "Failed to block/activate client");
            }
        } catch(error){
            showError("Error", "An error occured: " + error);
        }        
    };

    const renderHeaderTitle = () => {
        return <h3>Clients</h3>;
    }
    const headerTitleChildren = renderHeaderTitle();

    const renderActionsBody = (rowData: Client) => {
        return (
            <InputSwitch
                checked={ rowData.isActive ? true : false }
                onChange={(e: InputSwitchChangeEvent) => switchActiveStatus(e, rowData)}
                tooltip="Toggle active status"
            />
        );
    };

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
        { field: 'driversLicence', header: 'Drivers licence'},
        { header: 'Actions', body: renderActionsBody }
    ];

    const renderMainContent = () => {
        return (
            <AddableTable 
                data={data} 
                dataKey="id" 
                columns={clientColumns} 
                header={"Clients"}                
            />
        );
    }
    const mainContentChildren = renderMainContent();

    return (
        <>
            <PagesContainer headerTitleChildren={headerTitleChildren} mainContentChildren={mainContentChildren} />
        </>
    );
}

export default ClientsPage;