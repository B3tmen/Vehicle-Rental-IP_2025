import '@styles/AddMalfunctionDialog.css'
import { Malfunction, Vehicle, VehicleMalfunction } from "types/types";
import { Button } from 'primereact/button';
import { Calendar } from "primereact/calendar";
import { Dialog } from "primereact/dialog";
import { InputTextarea } from "primereact/inputtextarea";
import { useState } from "react";
import { useToast } from '@hooks/useToast';

interface Props{
    visible: boolean;
    vehicle: Vehicle;
    onHide: () => void;
    onAdd: (vehicleMalfunction: VehicleMalfunction) => void;
}

export const AddMalfunctionDialog: React.FC<Props> = ({ visible, onHide, vehicle, onAdd }) => {
    const { showSuccess, showError } = useToast();
    const emptyMalfunction: Malfunction = {
        id: null,
        reason: "",
        timeOfMalfunction: new Date(),
    };
    const [malfunction, setMalfunction] = useState<Malfunction>(emptyMalfunction); 

    const handleDateChange = (e: { value: Date | null }) => {
        setMalfunction((prev) => ({
            ...prev,
            timeOfMalfunction: e.value || new Date() // Fallback to current date if null
        }));
    };

    const handleReasonChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        setMalfunction((prev) => ({
            ...prev,
            reason: e.target.value
        }));
    };
    
    const onFinish = async () => {
        if(malfunction && vehicle){
            const isValid = malfunction.timeOfMalfunction && malfunction.reason;

            if(isValid){
                const vehicleMalfunction: VehicleMalfunction = {
                    vehicle: vehicle,
                    malfunction: malfunction
                }

                await onAdd(vehicleMalfunction);
            }
            else{
                showError("Error", "Couldn't add malfunction. Invalid fields");
            }
        }
    }

    return (
        <Dialog visible={visible} onHide={onHide} header="Malfunction report" draggable={false} className='malfunction-dialog'>
            <div className="malfunction-container">
                <div className="malfunction-container__content">
                    <div className="malfunction-container__content__date">
                        <label htmlFor='timeOfMalfunction'>Time of malfunction</label>
                        <Calendar id="timeOfMalfunction" value={malfunction?.timeOfMalfunction} onChange={handleDateChange} showTime hourFormat="24" showIcon />
                    </div>
                    <div className="malfunction-container__content__reason">
                        <label htmlFor='malfunctionReason'>Reason</label>
                        <InputTextarea 
                            id='malfunctionReason'
                            value={malfunction?.reason}
                            onChange={handleReasonChange}
                            rows={5}
                            cols={15}
                        />
                    </div>
                </div>

                <div className="malfunction-container__buttons">
                    <Button 
                        label='Cancel'
                        icon="pi pi-times"
                        className="p-button-danger"
                        onClick={onHide}
                    />

                    <Button 
                        label='Finish report'
                        icon="pi pi-check"
                        className="p-button-primary"
                        onClick={onFinish}
                    />
                </div>
            </div>
        </Dialog>
    );
}