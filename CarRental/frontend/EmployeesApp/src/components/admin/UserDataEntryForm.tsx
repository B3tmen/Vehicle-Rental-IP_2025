import '../../styles/UserDataEntryForm.css'

import { useEffect, useState } from "react";
import { Button } from "primereact/button";
import { Checkbox } from "primereact/checkbox";
import { Dropdown, DropdownChangeEvent } from "primereact/dropdown";
import { FileUpload, FileUploadHandlerEvent } from "primereact/fileupload";
import { InputText } from "primereact/inputtext";
import { Password } from "primereact/password";
import { Tag } from 'primereact/tag';
import { Image } from 'primereact/image';

import { User, UserSubType } from "types/types";
import { Image as ImageInterface } from 'types/types';
import { useToast } from "@hooks/useToast";
import { isClient, isEmployee } from '@utils/typeChecker';
import { emptyImageObject, emptyUserObject } from '@utils/constants/emptyObjects';
import { Controller, useForm } from 'react-hook-form';
import { userSchema } from '@utils/validationSchemas';
import { yupResolver } from '@hookform/resolvers/yup';

interface Props{
    userProp: UserSubType | null;
    isUpdating?: boolean;
    onHide: () => void;
    onSave: (item: User, formData?: FormData) => void;
    formData?: FormData;
}

export const UserDataEntryForm: React.FC<Props> = ({ userProp, onHide, onSave, isUpdating = false, formData }) => {
    const { showError } = useToast();
    const userTypes = ["Client", "Employee"];   // TODO: get user roles/types from database
    const employeeRoles = ["Administrator", "Operator", "Manager"];
    const citizenTypes = ["Local", "Foreigner"];
    const statusOptions = [true, false];
    const [user, setUser] = useState<User | null>(userProp);
    const [avatarImage, setAvatarImage] = useState<ImageInterface>(emptyImageObject);
    const [avatarImageUrl, setAvatarImageUrl] = useState<string>(user !== null && isClient(user) ? user.avatarImage?.url : '');

    const {
            control,
            handleSubmit,
            formState: { errors, isSubmitting },
        } = useForm({ resolver: yupResolver(userSchema),
            defaultValues: userProp || emptyUserObject
         });

    // useEffect(() => {       // changes in the UserDataEntryModal for the client json data
    //     setFormDataReal(formData);
    // }, [formData]);

    useEffect(() => {
        setUser(userProp);
    }, [userProp]);

    const onSubmit = (data: User) => { 
        if(isClient(data) && data.avatarImage?.url === ''){
            data.avatarImage = avatarImage;
        }

        onSave(data, formData);
    };

    const updateUser = <K extends keyof User>(field: K, value: UserSubType[K]) => {
        setUser((prevUser) => {
            if (!prevUser) return prevUser; // Prevent null access issues
            return {
                ...prevUser,
                [field]: value,
            };
        });
    };
    
    const onFieldChange = <K extends keyof UserSubType>(fieldName: K, value: UserSubType[K], fieldOnChange: (value: User[K]) => void) => {
        updateUser(fieldName, value);
        fieldOnChange(value);
    };

    const renderUserTypesCheckBoxes = () => {
        return userTypes.map((type) => (
            <div key={type} className='user-form-body__basic-details__types__checkboxes-container__checkbox'>
                <Controller
                    name="type"
                    control={control}
                    render={({ field }) => (
                        <Checkbox
                            inputId={type}
                            value={type}
                            checked={type === user?.type}
                            disabled={isUpdating}
                            onChange={() => onFieldChange('type', type, field.onChange)}
                        />
                    )}
                />
              <label htmlFor={type}>{type}</label>
            </div>
          )
        );
    }

    const getSeverity = (value: string) => {
        if(value === 'Active'){
            return 'success';
        }
        return 'danger';
    };

    const statusItemTemplate = (isActive: boolean) => {
        let _color = 'var(--color-green)', option = 'Active';
        if(isActive === false){
            _color = 'var(--color-red)';
            option = 'Blocked';
        } 

        return <Tag value={option} severity={getSeverity(option)} style={{ background: _color }} />
    };
    
    const renderAdditionalDetails = () => {
        return (
            <>
              <h4>Additional details</h4>
                {isEmployee(user!) && (
                    <div className='user-form-body__additional-details__container'>
                        <label>Role</label>
                        <Controller
                            name="role"
                            control={control}
                            render={({ field }) => (
                                <Dropdown
                                    placeholder="Select role"
                                    value={user?.role}
                                    options={employeeRoles}
                                    onChange={(e) => onFieldChange('role', e.target.value, field.onChange)}
                                    className="add-vehicle__basic-details--select"
                                />
                            )}
                        />
                        {errors.role && <small className="p-error">{errors.role.message}</small>}
            
                        {isUpdating && (
                            <div className='user-form-body__additional-details__container__field'>
                                <label>Active status</label>
                                <Controller
                                name="isActive"
                                control={control}
                                render={({ field }) => (
                                    <Dropdown
                                        placeholder="Active status"
                                        value={field.value}
                                        options={statusOptions}
                                        valueTemplate={statusItemTemplate}
                                        itemTemplate={statusItemTemplate}
                                        onChange={(e) => onFieldChange('isActive', e.target.value, field.onChange)}
                                        className="add-vehicle__basic-details--select"
                                    />
                                )}
                                />
                            </div>
                        )}
                    </div>
                )}
              
              {isClient(user!) && (
                <div className='user-form-body__additional-details__container'>
                    <div className='user-form-body__additional-details__container__field'>
                        <label>Personal card number</label>
                        <Controller
                            name="personalCardNumber"
                            control={control}
                            render={({ field }) => (
                                <InputText
                                    id='personalCardNumber'
                                    placeholder='Personal card number'
                                    keyfilter='num'
                                    maxLength={13}
                                    value={field.value}
                                    onChange={(e) => onFieldChange('personalCardNumber', e.target.value, field.onChange)}
                                />
                            )}
                        />
                        {errors.personalCardNumber && <small className="p-error">{errors.personalCardNumber.message}</small>}
                    </div>
                  
                    <div className='user-form-body__additional-details__container__field'>
                        <label>Email</label>
                        <Controller
                            name="email"
                            control={control}
                            render={({ field }) => (
                                <InputText
                                    id='email'
                                    placeholder='Email'
                                    keyfilter='email'
                                    value={field.value}
                                    onChange={(e) => onFieldChange('email', e.target.value, field.onChange)}
                                />
                            )}
                        />
                        {errors.email && <small className="p-error">{errors.email.message}</small>}
                    </div>

                    <div className='user-form-body__additional-details__container__field'>
                        <label>Phone number</label>
                        <Controller
                            name="phoneNumber"
                            control={control}
                            render={({ field }) => (
                                <InputText
                                    id='phoneNumber' 
                                    placeholder='Phone number' 
                                    maxLength={13}
                                    value={field.value} 
                                    onChange={(e) => onFieldChange('phoneNumber', e.target.value, field.onChange) } 
                                />
                            )}
                        />
                        {errors.phoneNumber && <small className="p-error">{errors.phoneNumber.message}</small>}
                    </div>

                    <div className='user-form-body__additional-details__container__field'>
                        <label>Drivers licence</label>
                        
                        <Controller
                            name="driversLicence"
                            control={control}
                            render={({ field }) => (
                                <InputText 
                                    id='driversLicence' 
                                    placeholder='Drivers licence' 
                                    maxLength={9}
                                    value={field.value} 
                                    onChange={(e) => onFieldChange('driversLicence', e.target.value, field.onChange)} 
                                />
                            )}
                        />
                        {errors.driversLicence && <small className="p-error">{errors.driversLicence.message}</small>}
                    </div>

                    <div className='user-form-body__additional-details__container__field'>
                        <label>Citizenship</label>
                        
                        <Controller
                            name="citizenType"
                            control={control}
                            render={({ field }) => (
                                <Dropdown
                                    placeholder="Select citizen type"
                                    value={field.value} 
                                    options={citizenTypes}
                                    onChange={(e: DropdownChangeEvent) => onFieldChange('citizenType', e.target.value, field.onChange)}
                                    className="add-vehicle__basic-details--select"
                                />
                            )}
                        />
                        {errors.citizenType && <small className="p-error">{errors.citizenType.message}</small>}
                    </div>
                </div>
              )}
            </>
          );
    }

    const handleUploadImage = async (event: FileUploadHandlerEvent) => {
        if (event.files && event.files.length > 0) {
            const file = event.files[0];    // Get the first file (assuming single file upload)
            const fileName = file.name;     // Extract the file name
            const fileType = file.type;
            const image = emptyImageObject;

            if(user !== null && isClient(user)){ 
                if(user.avatarImage === null) 
                    user.avatarImage = emptyImageObject;
                else{
                    image.id = user.avatarImage.id;
                }
                
                image.name = fileName;
                image.type = fileType;
                formData?.set("image", file);
                setAvatarImage(image);
            }

            // In a real scenario, you'd send the file to the server and get the updated URL
            // For now, just update the avatar URL with the selected file's URL (local for demo)
            const reader = new FileReader();
            reader.onloadend = () => {
                setAvatarImageUrl(reader.result);  // This will update the avatar with the selected image
            };
            if (file) {
                reader.readAsDataURL(file);
            }
        } else {
            showError('Error', 'No files selected!');
        }
    };
    
    return(
        <form onSubmit={handleSubmit(onSubmit)} className='user-form-body'>
            <div className='user-form-body__basic-details'>
                <h4>Basic details</h4>
                <div className='user-form-body__basic-details__image'>
                    {/* Image Upload */}
                    {user != null && user.type === 'Client' && isClient(user) && (
                        <>
                            <Image src={avatarImageUrl} alt='avatar' width='200' height='200' preview /> 
                            <FileUpload
                                name="avatarImage"
                                accept="image/*"
                                customUpload
                                uploadHandler={handleUploadImage}
                                maxFileSize={1_000_000}
                                chooseLabel="Upload Image"
                            />
                        </>
                    )}
                </div>
                <div className='user-form-body__basic-details__input-fields'>
                    {/* Basic details fields */}
                    <div className='user-form-body__basic-details__input-fields__field'>
                        <label htmlFor='username'>Username</label>
                        <Controller
                            name="username"
                            control={control}
                            render={({ field }) => (
                                <InputText
                                    id='username'
                                    placeholder='Username'
                                    value={field.value}
                                    onChange={(e) => onFieldChange('username', e.target.value, field.onChange)}
                                />
                            )}
                        />
                        {errors.username && <small className="p-error">{errors.username.message}</small>}
                    </div>
                    <div className='user-form-body__basic-details__input-fields__field'>
                        <label htmlFor='password'>Password</label>
                            <Controller
                                name="passwordHash"
                                control={control}
                                render={({ field }) => (
                                    <Password
                                        id='password'
                                        value={field.value}
                                        inputStyle={{ width: '100%' }}
                                        toggleMask
                                        onChange={(e) => onFieldChange('passwordHash', e.target.value, field.onChange)}
                                        feedback={false}
                                    />
                                )}
                            />
                            {errors.passwordHash && <small className="p-error">{errors.passwordHash.message}</small>}
                        </div>
                    <div className='user-form-body__basic-details__input-fields__field'>
                        <label htmlFor='firstName'>First name</label>
                        <Controller
                            name="firstName"
                            control={control}
                            render={({ field }) => (
                                <InputText 
                                    id='firstName' 
                                    placeholder='First name' 
                                    value={field.value} 
                                    onChange={(e) => onFieldChange('firstName', e.target.value, field.onChange)} 
                                />
                            )}
                        />
                        {errors.firstName && <small className="p-error">{errors.firstName.message}</small>}
                    </div>
                    <div className='user-form-body__basic-details__input-fields__field'>
                        <label htmlFor='lastName'>Last name</label>
                        <Controller
                            name="lastName"
                            control={control}
                            render={({ field }) => (
                                <InputText 
                                    id='lastName' 
                                    placeholder='Last name' 
                                    value={field.value} 
                                    onChange={(e) => onFieldChange('lastName', e.target.value, field.onChange)} 
                                />
                            )}
                        />
                        {errors.lastName && <small className="p-error">{errors.lastName.message}</small>}
                    </div>
                </div>
                <div className='user-form-body__basic-details__types'>
                    <h4>Choose type</h4>
                    <div className='user-form-body__basic-details__types__checkboxes-container'>
                        {renderUserTypesCheckBoxes()}
                    </div>
                </div>
            </div>

            <div className='user-form-body__additional-details'>
                {renderAdditionalDetails()}
            </div>

            <div className='user-form-body__buttons'>
                {/* Cancel Button */}
                <Button
                    label="Cancel"
                    icon="pi pi-times"
                    className="p-button-danger"
                    onClick={onHide}
                />
                {/* Save Button */}
                <Button
                    label="Save User"
                    icon="pi pi-check"
                    type='submit'
                    loading={isSubmitting}
                    className="p-button-primary"
                />
            </div>
        </form>
    );
}