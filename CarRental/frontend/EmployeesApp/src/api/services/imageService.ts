import axiosInstance from "@api/axiosInstance";
import { API_IMAGES_URL } from "@utils/constants/ApiLinks";

export const downloadImage = async (id: number, relativePath: string) => {
    try {
        const response = await axiosInstance.get(API_IMAGES_URL + `${id}`, {
            params: { relativePath },
            responseType: 'blob'
        });
        
        return response.data;
    } 
    catch (error) {
        console.error('Error downloading image:', error);
        throw error;
    }
};

export const addImageToFormData = async (imageUrl: string, formData: FormData) => {
    try {
        // 1. Fetch the image
        const response = await fetch(imageUrl);
        const blob = await response.blob();

        // 2. Append the image
        formData.set('image', blob); 

        console.log('Image successfully added to FormData and submitted!');
    } catch (error) {
        console.error('Error processing image:', error);
    }
};