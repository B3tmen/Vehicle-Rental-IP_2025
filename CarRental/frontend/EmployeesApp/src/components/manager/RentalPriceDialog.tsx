import { Vehicle } from "types/types";
import { Dialog } from "primereact/dialog";
import { useEffect, useState } from "react";
import { InputText } from "primereact/inputtext";
import { Button } from "primereact/button";
import { useToast } from "@hooks/useToast";
import { update } from "@api/apiService";
import { API_BICYCLES_URL, API_CARS_URL, API_SCOOTERS_URL } from "@utils/constants/ApiLinks";

interface Props{
    vehicle: Vehicle | null;
    title: string;
    visible: boolean;
    onHide: () => void;
    onListUpdate: (vehicle: Vehicle) => void;
}

export const RentalPriceDialog: React.FC<Props> = ({ vehicle: vehicleProp, title, visible, onHide, onListUpdate }) => {
    const { showSuccess, showError } = useToast();
    const [vehicle, setVehicle] = useState<Vehicle | null>(vehicleProp);

    useEffect(() => {
        setVehicle(vehicleProp);
    }, [vehicleProp]);


    const onFinish = async () => {
        if(vehicle && vehicle?.rentalPrice){
            try{
                const link = vehicle.type_ === 'Car' ? API_CARS_URL : vehicle.type_ === 'Bicycle' ? API_BICYCLES_URL : API_SCOOTERS_URL

                const response = await update<Vehicle, Vehicle>(link, vehicle.id, vehicle);

                if(response){
                    onListUpdate(response);
                    showSuccess("Success", "Successfully updated rental price");
                }
                else{
                    showError("Error", `Couldn't update rental price: ${response}`);
                }
            } catch(error){
                showError("Error", `An error occured: ${error}`);
            }
        }
    }

    const onRentalPriceChange = (value: string) => {
        if(vehicle !== null)
            setVehicle({ ...vehicle, rentalPrice: value })
    }

    return (
        <Dialog header={title} visible={visible} draggable={false} onHide={onHide} style={{ width: '30%' }} resizable={false} >
            <div className="dialog-body" style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
                <InputText
                    placeholder="Rental price"
                    value={vehicle?.rentalPrice}
                    onChange={(e) => onRentalPriceChange(e.target.value)}
                />

                <div className="dialog-body__buttons" style={{ display: 'flex', justifyContent: 'space-between' }}>
                    <Button 
                        label='Cancel'
                        icon="pi pi-times"
                        className="p-button-danger"
                        onClick={onHide}
                    />

                    <Button 
                        label='Finish'
                        icon="pi pi-check"
                        className="p-button-primary"
                        onClick={onFinish}
                    />
                </div>
            </div>
        </Dialog>
    );
}