import { Dialog } from "primereact/dialog";

import { User, UserSubType } from "types/types";
import { UserDataEntryForm } from './UserDataEntryForm';
import { useToast } from '@hooks/useToast';
import { isClient, isEmployee } from "@utils/typeChecker";
import { emptyUserObject } from '@utils/constants/emptyObjects';
import { addImageToFormData } from '@api/services/imageService';

interface Props {
    visible: boolean;
    onHide: () => void;
    onAdd?: (item: User, formData?: FormData) => void;
    onUpdate?: (item: User, formData?: FormData) => void;
    isUpdating?: boolean;
    userToUpdate?: UserSubType | null;
    formData?: FormData;
}

export const UserDataEntryModal: React.FC<Props> = ({ visible, onHide, onUpdate, onAdd, isUpdating = false, userToUpdate = null, formData }) => {
    //const phoneRegex = /^(?:\+387\s?|0)(\d{2})\/\d{3}-\d{3}$/;
    const emptyUser = emptyUserObject;
    const { showError } = useToast();
    const user = isUpdating === false ? emptyUser : userToUpdate;

    const onSave = async (item: User, formData?: FormData) => {
        
        const isValid = item?.username && item.passwordHash && item.firstName && item.lastName && 
                        (item.type !== 'Employee' || 'role' in item); 

        console.log(JSON.stringify(item));
        try{
            if(!item.type)
                throw new Error("No user type was specified.");
            if(item.type === 'Employee' && ('role' in item && !item.role) )
                throw new Error("Please specify the role of the employee");

            if(isValid){
                if(isUpdating){
                    //setUser(item);
                    console.log("From update: " + JSON.stringify(item));
                    if(isClient(item)){
                        const blob: Blob = new Blob([JSON.stringify(item)], { type: "application/json" });
                        formData?.set('client', blob);
                        let updatedWithImageUrl = false;
                        
                        if(item.avatarImage !== null && item.avatarImage.url !== '') {
                            onUpdate?.(item, formData);
                            updatedWithImageUrl = true;
                        }
                        
                        if(!updatedWithImageUrl){
                            onUpdate?.(item, formData);
                        }
                    }
                    else if(isEmployee(item)){
                        onUpdate?.(item);
                    }
                }
                else{
                    item.isActive = true;

                    if(isClient(item)){
                        const blob: Blob = new Blob([JSON.stringify(item)], { type: "application/json" }); // Make sure clientDTO is serialized, formData is in UserDataEntryForm
                        formData?.set('client', blob);
                        
                        if(item.avatarImage != null) {
                            await addImageToFormData(item.avatarImage.url, formData!);
    
                            if(formData?.has('image')){
                                onAdd?.(item, formData);
                            }
                            else{
                                throw new Error("No image was specified");
                            }
                        }
                        
                        onAdd?.(item, formData);
                    }
                    else if(isEmployee(item)){
                        onAdd?.(item);
                    }
                }
            }
            else{
                throw new Error("Failed to add a user. Fields missing.");
            }
        } catch(error) {
            showError("Error", `${error}`);
        }
    }

    return (
        <Dialog visible={visible} onHide={onHide} header={ isUpdating ? "Update user" : "Create a User" } draggable={false} style={{ minWidth: '60%' }} >
            <UserDataEntryForm userProp={user} onHide={onHide} onSave={onSave} isUpdating={isUpdating} formData={formData} />
        </Dialog>
    );
}