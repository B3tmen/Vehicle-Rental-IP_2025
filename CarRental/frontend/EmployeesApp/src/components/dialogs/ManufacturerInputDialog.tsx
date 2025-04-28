import '@styles/ManufacturerInputDialog.css'
import { useEffect } from 'react';
import { Dialog } from "primereact/dialog";
import { Button } from "primereact/button";
import { FloatLabel } from 'primereact/floatlabel';
import { InputText } from 'primereact/inputtext';

import { Manufacturer } from 'types/types';
import { useToast } from '@hooks/useToast';
import { emptyManufacturerObject } from '@utils/constants/emptyObjects';
import { Controller, useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { manufacturerSchema } from '@utils/validationSchemas';


interface Props{
    visible: boolean;
    onHide: () => void;
    onFinish: (manufacturer: Manufacturer) => void;
    header: string;
    manufacturer?: Manufacturer | null;
}

const ManufacturerInputDialog: React.FC<Props> = ({ visible, onHide, onFinish, header, manufacturer: manufacturerProp = null}) => {
    const emptyManufacturer: Manufacturer = emptyManufacturerObject;
    const { showError } = useToast();

    const { 
        control,
        handleSubmit,
        reset,
        formState: { errors, isSubmitting }
      } = useForm<Manufacturer>({
        resolver: yupResolver(manufacturerSchema),
        defaultValues: manufacturerProp || emptyManufacturer
      });

    // Reset form when manufacturerProp changes
    useEffect(() => {
        reset(manufacturerProp || emptyManufacturer);
    }, [manufacturerProp, reset]);

    const onSubmit = async (data: Manufacturer) => {
        try {
            await onFinish(data);
            reset(emptyManufacturer);
        } 
        catch (error) {
            showError('Error', "" + error);
        }
    };

    return (
        <Dialog 
            header={header}
            visible={visible}
            style={{ width: '50vw' }}
            modal
            onHide={onHide}
            draggable={false}
            className='input-dialog'
        >
            <form onSubmit={handleSubmit(onSubmit)} className='dialog-container'>
                <div className='dialog-container__inputs'>
                    <div className='dialog-container__inputs__container'>
                        {/* Name Field */}
                        <Controller
                            name="name"
                            control={control}
                            render={({ field, fieldState }) => (
                                <FloatLabel>
                                    <label htmlFor="name">Name</label>
                                    <InputText 
                                        id="name" 
                                        value={field.value} 
                                        onChange={field.onChange} 
                                        className={fieldState.error ? 'p-invalid' : ''}
                                    />
                                    {fieldState.error && <small className="p-error">{fieldState.error.message}</small>}
                                </FloatLabel>
                            )}
                        />

                        {/* State Field */}
                        <Controller
                            name="state"
                            control={control}
                            render={({ field, fieldState }) => (
                                <FloatLabel>
                                    <label htmlFor="state">State</label>
                                    <InputText 
                                        id="state" 
                                        value={field.value} 
                                        onChange={field.onChange} 
                                        className={fieldState.error ? 'p-invalid' : ''}
                                    />
                                    {fieldState.error && <small className="p-error">{fieldState.error.message}</small>}
                                </FloatLabel>
                            )}
                        />

                        {/* Address Field */}
                        <Controller
                            name="address"
                            control={control}
                            render={({ field, fieldState }) => (
                                <FloatLabel>
                                    <label htmlFor="address">Address</label>
                                    <InputText 
                                        id="address" 
                                        value={field.value} 
                                        onChange={field.onChange} 
                                        className={fieldState.error ? 'p-invalid' : ''}
                                    />
                                    {fieldState.error && <small className="p-error">{fieldState.error.message}</small>}
                                </FloatLabel>
                            )}
                        />

                        {/* Phone Number Field */}
                        <Controller
                            name="phoneNumber"
                            control={control}
                            render={({ field, fieldState }) => (
                                <FloatLabel>
                                    <label htmlFor="phoneNumber">Phone number</label>
                                    <InputText 
                                        id="phoneNumber" 
                                        value={field.value} 
                                        onChange={field.onChange} 
                                        className={fieldState.error ? 'p-invalid' : ''}
                                    />
                                    {fieldState.error && <small className="p-error">{fieldState.error.message}</small>}
                                </FloatLabel>
                            )}
                        />

                        {/* Fax Field */}
                        <Controller
                            name="fax"
                            control={control}
                            render={({ field, fieldState }) => (
                                <FloatLabel>
                                    <label htmlFor="fax">Fax</label>
                                    <InputText 
                                        id="fax" 
                                        value={field.value} 
                                        onChange={field.onChange} 
                                        className={fieldState.error ? 'p-invalid' : ''}
                                        maxLength={8}
                                    />
                                    {fieldState.error && <small className="p-error">{fieldState.error.message}</small>}
                                </FloatLabel>
                            )}
                        />

                        {/* Email Field */}
                        <Controller
                            name="email"
                            control={control}
                            render={({ field, fieldState }) => (
                                <FloatLabel>
                                    <label htmlFor="email">Email</label>
                                    <InputText 
                                        id="email" 
                                        value={field.value} 
                                        onChange={field.onChange} 
                                        className={fieldState.error ? 'p-invalid' : ''}
                                    />
                                    {fieldState.error && <small className="p-error">{fieldState.error.message}</small>}
                                </FloatLabel>
                            )}
                        />
                    </div>
                </div>

                <div className='dialog-container__buttons'>
                    <Button label="Cancel" icon="pi pi-times" onClick={onHide} className='dialog-container__buttons__cancel-button' type="button" />
                    <Button label="Finish" icon="pi pi-check" type="submit" loading={isSubmitting} autoFocus className='dialog-container__buttons__finish-button' />
                </div>
            </form>
            
        </Dialog>
    );
}

export default ManufacturerInputDialog;