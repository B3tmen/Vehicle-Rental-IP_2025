import { useEffect, useState } from "react";
import { Dialog } from "primereact/dialog";

import { VehicleDataEntryForm } from './VehicleDataEntryForm';
import { VehicleSubType, Vehicle, RentalStatus } from 'types/types';
import { API_RENTAL_STATUSES_URL } from '@utils/constants/ApiLinks';
import { useToast } from '@hooks/useToast';
import { getAll } from '@api/apiService';
import { emptyVehicleObject } from "@utils/constants/emptyObjects";
import { isCar } from "@utils/typeChecker";

interface Props {
    header: string;
    visible: boolean;
    onHide: () => void;
    onAdd?: (item: Vehicle, formData?: FormData) => void;
    onUpdate?: (item: Vehicle, formData?: FormData) => void;
    isUpdating?: boolean;
    vehicleToUpdate?: VehicleSubType | null;
    formData?: FormData;
}

export const VehicleDataEntryModal: React.FC<Props> = ({ header, visible, onHide, onUpdate, onAdd, isUpdating = false, vehicleToUpdate = null, formData }) => {
    const emptyVehicle: VehicleSubType = emptyVehicleObject;

    const { showSuccess, showError } = useToast();
    const vehicle = isUpdating === false ? emptyVehicle : vehicleToUpdate;
    const [rentalStatuses, setRentalStatuses] = useState<RentalStatus[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);
    
    useEffect(() => {
        const fetchStatuses = async () => {
            setLoading(true);
            try {
                const statuses = await getAll<RentalStatus>(API_RENTAL_STATUSES_URL);
                
                setRentalStatuses(statuses);  // Set the fetched data to state
            } catch (error) {
                setError("Error fetching rental statuses - " + error);
            } finally {
                setLoading(false);
            }
        };

        fetchStatuses();
    }, []);

    if(loading){
        return <p>Loading vehicles... Please wait</p>
    }
    // if(error){
    //     return <p>{error}</p>
    // }

    const onSave = async (item: Vehicle, formData?: FormData) => {
        //const completeVehicle = { ...vehicle, ...additionalDetails }; // Combine main and additional details
        const templateText = isUpdating ? "update" : "add";
        if(item.isActive == null) item.isActive = true;
        
        const isValid = item.model &&
            item.price &&
            //completeVehicle.imageUrl &&
            item.manufacturer &&
            item.rentalStatus &&
            (item.type_ === "Car"
                ? "carId" in item && "purchaseDate" in item && item.carId && item.purchaseDate
                : true) &&
            (item.type_ === "Bicycle"
                ? "bicycleId" in item && "ridingAutonomy" in item && item.bicycleId && item.ridingAutonomy
                : true) &&
            (item.type_ === "Scooter"
                ? "scooterId" in item && "maxSpeed" in item && item.scooterId && item.maxSpeed
                : true);
        
        try{
            if(isValid){
                if(isUpdating){
                    if(isCar(item)){
                        const dateString = item.purchaseDate;
                        item.purchaseDate = new Date(dateString);
                    }
                    console.log("UPDATING: " + JSON.stringify(item)); 

                    const blob = new Blob([JSON.stringify(item)], { type: "application/json" });
                    formData?.set('vehicle', blob);
                    let updatedWithImageUrl = false;

                    if(item.image !== null && item.image.url !== '') {
                        onUpdate?.(item, formData);
                        updatedWithImageUrl = true;
                    }
                    
                    if(!updatedWithImageUrl){
                        onUpdate?.(item, formData);
                    }
                }
                else{
                    item.rentalStatus = rentalStatuses.find(rs => rs.status === 'Free')!;
                    const blob: Blob = new Blob([JSON.stringify(item)], { type: "application/json" });
                    formData?.set('vehicle', blob);  // Make sure vehicleDTO is serialized

                    onAdd?.(item, formData);
                }
            }
            else{
                throw new Error(`Failed to ${templateText} a vehicle. Fields missing.`)
            }
        } catch(error) {
            showError("Error", `${error}`);
        }
        
        //setVehicle(completeVehicle);
    }

    return (
        <Dialog visible={visible} onHide={onHide} header={header} className='add-dialog' draggable={false}>
            <VehicleDataEntryForm vehicleProp={vehicle} onHide={onHide} onSave={onSave} isUpdating={isUpdating} formData={formData} />
        </Dialog>
    );
};