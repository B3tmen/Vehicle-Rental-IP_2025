import '@styles/VehicleDataEntryForm.css'
import { useEffect, useState } from "react";
import { Button } from "primereact/button";
import { Checkbox } from "primereact/checkbox";
import { Dropdown } from "primereact/dropdown";
import { FileUpload, FileUploadHandlerEvent } from "primereact/fileupload";
import { InputText } from "primereact/inputtext";
import { RadioButton } from "primereact/radiobutton";
import { Calendar } from 'primereact/calendar';
import { InputTextarea } from 'primereact/inputtextarea';
import { Image } from 'primereact/image';
import { Controller, useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';

import { useFetchData } from "@hooks/useFetchData";
import { useToast } from "@hooks/useToast";
import { API_MANUFACTURERS_URL, API_RENTAL_STATUSES_URL } from "@utils/constants/ApiLinks";
import { isBicycle, isCar, isScooter } from "@utils/typeChecker";
import { Manufacturer, RentalStatus, Vehicle, VehicleSubType } from "types/types";
import { Image as ImageInterface } from 'types/types';
import { emptyImageObject, emptyVehicleObject } from '@utils/constants/emptyObjects';
import { getAll } from '@api/apiService';
import { vehicleSchema } from '@utils/validationSchemas';
import { format } from 'date-fns';

interface Props{
    vehicleProp: VehicleSubType | null;
    isUpdating?: boolean;
    onHide: () => void;
    onSave: (item: Vehicle, formData?: FormData) => void;
    formData?: FormData;
}

export const VehicleDataEntryForm: React.FC<Props> = ({ vehicleProp, isUpdating = false, onHide, onSave, formData }) => {
    const { showError } = useToast();
    const vehicleTypes = ["Car", "Bicycle", "Scooter"];
    const [vehicle, setVehicle] = useState<Vehicle | null>(vehicleProp);
    const [rentalStatuses, setRentalStatuses] = useState<RentalStatus[]>([]);
    const [avatarImage, setAvatarImage] = useState<ImageInterface>(emptyImageObject);
    const [vehicleImageUrl, setVehicleImageUrl] = useState<string | null>(vehicle != null && vehicle.image != null ? vehicle.image.url : '');
    const  { data: manufacturersData } = useFetchData<Manufacturer>(API_MANUFACTURERS_URL);
    const manufacturerOptions = manufacturersData.map((manuf: Manufacturer) => ({
            label: manuf.id + " " + manuf.name,
            value: manuf
        })
    );

    const { control, handleSubmit, formState: { errors, isSubmitting } } = useForm({ 
        resolver: yupResolver(vehicleSchema), 
        defaultValues: vehicleProp || emptyVehicleObject
    });

    useEffect(() => {
        setVehicle(vehicleProp);
    }, [vehicleProp]);

    useEffect(() => {
        const fetchRentalStatuses = async () => {
            const rentalStatusData = await getAll<RentalStatus>(API_RENTAL_STATUSES_URL);
            setRentalStatuses(rentalStatusData);
        }

        fetchRentalStatuses();
    }, []);

    const onSubmit = (data: Vehicle) => {
        console.log("On submit: " + JSON.stringify(data));

        if(data.image?.url === ''){
            data.image = avatarImage;
        }

        onSave(data, formData);
    };

    const formatDate = (date: Date): string => {
        const pad = (num: number) => num.toString().padStart(2, '0');
        
        const year = date.getFullYear();
        const month = pad(date.getMonth() + 1); // Months are 0-based
        const day = pad(date.getDate());
        const hours = pad(date.getHours());
        const minutes = pad(date.getMinutes());
        const seconds = pad(date.getSeconds());
        
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    };

    const updateVehicle = <K extends keyof VehicleSubType>(field: K, value: VehicleSubType[K]) => {
        if(value instanceof Date){
            const date: Date = new Date(format(value, 'yyyy-MM-dd HH:mm:ss'));
            console.log("Update date: " + JSON.stringify(date));
        }
        
        setVehicle((prevVehicle) => {
            if (!prevVehicle) return prevVehicle; // Prevent null access issues
            return {
                ...prevVehicle,
                [field]: field === "purchaseDate" && value instanceof Date 
                ? value
                : value,
            };
        });
    };

    const onFieldChange = <K extends keyof VehicleSubType>(fieldName: K, value: VehicleSubType[K], fieldOnChange: (value: Vehicle[K]) => void) => {
        updateVehicle(fieldName, value);
        fieldOnChange(value);
    };
    
    const renderVehicleTypesRadioBtns = () => {
        return vehicleTypes.map((type) => (
            <div key={type} className='vehicle-form__basic-details__type__radio-container'>
                <Controller
                    name="type_"
                    control={control}
                    render={({ field }) => (
                        <Checkbox
                            className='vehicle-form__basic-details__type__radio-container--radio'
                            inputId={type}
                            value={type}
                            checked={type === vehicle?.type_}
                            disabled={isUpdating}
                            onChange={() => onFieldChange('type_', type, field.onChange)}
                        />
                    )}
                />
                <label htmlFor="vehicleType">{type}</label>
                {errors.type_ && <small className="p-error">{errors.type_.message}</small>}
            </div>
        ));
    }

    const handleUploadImage = async (event: FileUploadHandlerEvent) => {
        if (event.files && event.files.length > 0) {
            const file = event.files[0];    // Get the first file (assuming single file upload)
            const fileName = file.name;     // Extract the file name
            const fileType = file.type;
            const image = emptyImageObject;
        
            if(vehicle !== null){
                if(vehicle.image === null) 
                    vehicle.image = emptyImageObject;
                else{
                    image.id = vehicle.image.id;
                }
                
                image.name = fileName;
                image.type = fileType;
                formData?.set("image", file);
                setAvatarImage(image);
            }

            const reader = new FileReader();
            reader.onloadend = () => {
                setVehicleImageUrl(reader.result);  // This will update the image with the selected image
            };
            if (file) {
                reader.readAsDataURL(file);
            }
        } else {
            showError('Error', 'No files selected!');
        }
    };

    const renderBasicDetails = () => {
        return (
            <>
            <div className='vehicle-form__additional-details__container__field'>
                <label htmlFor='model'>Model</label>
                <Controller
                    name="model"
                    control={control}
                    render={({ field }) => (
                        <InputText
                            id='model'
                            value={field.value}
                            placeholder='Model'
                            onChange={(e) => onFieldChange('model', e.target.value, field.onChange)}
                            
                            className={`vehicle-form__basic-details--input ${errors.model ? 'p-invalid' : ''}`}
                        />
                    )}
                />
                {errors.model && <small className="p-error">{errors.model.message}</small>}
            </div>

            <div className='vehicle-form__additional-details__container__field'>
                <label htmlFor='price'>Price</label>
                <Controller
                    name="price"
                    control={control}
                    render={({ field }) => (
                        <InputText
                            id='price'
                            maxLength={6}
                            placeholder='Price'
                            value={field.value.toString()}
                            onChange={(e) => onFieldChange('price', e.target.value, field.onChange)}
                            className={`vehicle-form__basic-details--input ${errors.price ? 'p-invalid' : ''}`}
                        />
                    )}
                />
                {errors.price && <small className="p-error">{errors.price.message}</small>}
            </div>

            <div className='vehicle-form__additional-details__container__field'>
                <label htmlFor='rentalPrice'>Rental price</label>
                <Controller
                    name="rentalPrice"
                    control={control}
                    render={({ field }) => (
                        <InputText
                            id='rentalPrice'
                            value={field.value}
                            placeholder='Rental price'
                            onChange={(e) => onFieldChange('rentalPrice', e.target.value, field.onChange)}
                            className={`vehicle-form__basic-details--input ${errors.rentalPrice ? 'p-invalid' : ''}`}
                            keyfilter="num"
                        />
                    )}
                />
                {errors.rentalPrice && <small className="p-error">{errors.rentalPrice.message}</small>}
            </div>
                    
            <div className='vehicle-form__additional-details__container__field'>
                <label htmlFor='manufacturer'>Manufacturer</label>
                <Controller
                    name="manufacturer"
                    control={control}
                    render={({ field }) => (
                    <Dropdown
                        id='manufacturer'
                        placeholder="Select Manufacturer"
                        value={field.value}
                        options={manufacturerOptions}
                        onChange={(e) => onFieldChange('manufacturer', e.target.value, field.onChange)}
                        className={`vehicle-form__basic-details--select ${errors.manufacturer ? 'p-invalid' : ''}`}
                        scrollHeight='20vh'
                    />
                    )}
                />
                {errors.manufacturer && <small className="p-error">{errors.manufacturer.message}</small>}
            </div>

            <div className='vehicle-form__basic-details__type'>
                <h3>Vehicle Type</h3>
                {renderVehicleTypesRadioBtns()}
            </div>
            </>
        );
    };

    const renderAdditionalDetails = () => {
        return (
            <>
                <h3>Additional Details</h3>
                {isCar(vehicle!) && (
                    <div className='vehicle-form__additional-details__container'>
                        <div className='vehicle-form__additional-details__container__field'>
                            <label htmlFor="carId">Car ID</label>
                            <Controller
                                name="carId"
                                control={control}
                                render={({ field }) => (
                                    <InputText
                                        id='carId'
                                        placeholder='carId'
                                        value={field.value}
                                        onChange={(e) => onFieldChange('carId', e.target.value, field.onChange)}
                                        className={errors.carId ? 'p-invalid' : ''}
                                    />
                                )}
                            />
                            {errors.carId && <small className="p-error">{errors.carId.message}</small>}
                        </div>


                        <div className='vehicle-form__additional-details__container__field'>
                            <label htmlFor="purchaseDate">Purchase date</label>
                            <Controller
                                name="purchaseDate"
                                control={control}
                                render={({ field }) => (
                                    <Calendar
                                        inputId='purchaseDate'
                                        placeholder='purchaseDate'
                                        value={field.value}
                                        showIcon
                                        dateFormat='yy-mm-dd'
                                        showTime
                                        onChange={(e) => onFieldChange('purchaseDate', e.target.value, field.onChange)}
                                        className={errors.purchaseDate ? 'p-invalid' : ''}
                                    />
                                )}
                            />
                            {errors.purchaseDate && <small className="p-error">{errors.purchaseDate.message}</small>}
                        </div>

                        <div className='vehicle-form__additional-details__container__field'>
                            <label htmlFor='description'>Description</label>
                            <Controller
                                name="description"
                                control={control}
                                render={({ field }) => (
                                    <InputTextarea
                                        id='description'
                                        placeholder='Description'
                                        value={field.value}
                                        rows={5}
                                        cols={15}
                                        onChange={(e) => onFieldChange('description', e.target.value, field.onChange)}
                                        className={errors.description ? 'p-invalid' : ''}
                                    />
                                )}
                            />
                            {errors.description && <small className="p-error">{errors.description.message}</small>}
                        </div>
                    </div>
                )}

                {isBicycle(vehicle!) && (
                    <div className='vehicle-form__additional-details__container'>
                        <div className='vehicle-form__additional-details__container__field'>
                            <label htmlFor="bicycleId">Bicycle ID</label>
                            <Controller
                                name="bicycleId"
                                control={control}
                                render={({ field }) => (
                                    <InputText
                                        id='bicycleId'
                                        placeholder='Bicycle ID'
                                        value={field.value}
                                        onChange={(e) => onFieldChange('bicycleId', e.target.value, field.onChange)}
                                        className={errors.bicycleId ? 'p-invalid' : ''}
                                    />
                                )}
                            />
                            {errors.bicycleId && <small className="p-error">{errors.bicycleId.message}</small>}
                        </div>

                        <div className='vehicle-form__additional-details__container__field'>
                            <label htmlFor="ridingAutonomy">Riding autonomy</label>
                            <Controller
                                name="ridingAutonomy"
                                control={control}
                                render={({ field }) => (
                                    <InputText
                                        id='ridingAutonomy'
                                        placeholder='Riding autonomy'
                                        keyfilter='num'
                                        maxLength={2}
                                        value={field.value}
                                        onChange={(e) => onFieldChange('ridingAutonomy', e.target.value, field.onChange)}
                                        className={errors.ridingAutonomy ? 'p-invalid' : ''}
                                    />
                                )}
                            />
                            {errors.ridingAutonomy && <small className="p-error">{errors.ridingAutonomy.message}</small>}
                        </div>
                    </div>
                )}

                {isScooter(vehicle!) && (
                    <div className='vehicle-form__additional-details__container'>
                        <div className='vehicle-form__additional-details__container__field'>
                            <label htmlFor="scooterId">Scooter ID</label>
                            <Controller
                                name="scooterId"
                                control={control}
                                render={({ field }) => (
                                    <InputText
                                        id='scooterId'
                                        placeholder='Scooter ID'
                                        value={field.value}
                                        onChange={(e) => onFieldChange('scooterId', e.target.value, field.onChange)}
                                        className={errors.scooterId ? 'p-invalid' : ''}
                                    />
                                )}
                            />
                            {errors.scooterId && <small className="p-error">{errors.scooterId.message}</small>}
                        </div>

                        <div className='vehicle-form__additional-details__container__field'>
                            <label htmlFor="maxSpeed">Max speed</label>
                            <Controller
                                name="maxSpeed"
                                control={control}
                                render={({ field }) => (
                                    <InputText
                                        id='maxSpeed'
                                        placeholder='Max speed'
                                        keyfilter='num'
                                        maxLength={3}
                                        value={field.value}
                                        onChange={(e) => onFieldChange('maxSpeed', e.target.value, field.onChange)}
                                        className={errors.maxSpeed ? 'p-invalid' : ''}
                                    />
                                )}
                            />
                            {errors.maxSpeed && <small className="p-error">{errors.maxSpeed.message}</small>}
                        </div>
                    </div>
                )}
            </>
        );
    };

    return (
        <form onSubmit={(e) => {
            console.log("Form submitted");
            handleSubmit(onSubmit)(e);
        }} className='vehicle-form'>
            {/* Image Upload */}
            <div className='vehicle-form__image-upload'>
                {vehicle != null && vehicleImageUrl && (
                    <Image src={vehicleImageUrl} alt='vehicle' width='300' height='300' preview />
                )}
                <FileUpload className='vehicle-form__image-upload--image'
                    name="vehicleImage"
                    accept="image/*"
                    customUpload
                    uploadHandler={handleUploadImage}
                    maxFileSize={1_000_000}
                    chooseLabel="Upload Image"
                />
            </div>

            {/* Basic Details */}
            <div className='vehicle-form__basic-details'>
                {renderBasicDetails()}
            </div>

            {/* Status */}
            {isUpdating && (
                <div style={{ marginBottom: "1rem" }}>
                    <h3>Status</h3>
                    {rentalStatuses.map((status) => (
                        <div key={status.id} className='vehicle-form__basic-details__type__radio-container'>
                            <Controller
                                name="rentalStatus"
                                control={control}
                                render={({ field }) => (
                                    <Checkbox
                                        inputId={status.id.toString()}
                                        value={status}
                                        name="status"
                                        onChange={(e) => onFieldChange('rentalStatus', e.target.value, field.onChange)}
                                        className='vehicle-form__basic-details__type__radio-container--radio'
                                        checked={vehicle?.rentalStatus.id === status.id}
                                    />
                                )}
                            />
                            {errors.rentalStatus && <small className="p-error">{errors.rentalStatus.message}</small>}
                            <label htmlFor={status.status}>{status.status}</label>
                        </div>
                    ))}
                </div>
            )}

            {/* Additional Details */}
            <div className='vehicle-form__additional-details'>
                {renderAdditionalDetails()}
            </div>

            <div className='vehicle-form__buttons'>
                <Button
                    label="Cancel"
                    icon="pi pi-times"
                    className="p-button-danger"
                    onClick={onHide}
                    type="button"
                />
                <Button
                    label="Save Vehicle"
                    icon="pi pi-check"
                    className="p-button-primary"
                    type="submit"
                    loading={isSubmitting}
                />
            </div>
        </form>
    );
}