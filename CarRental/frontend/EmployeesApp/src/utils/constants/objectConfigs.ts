import { Client, InfoDisplayConfig, Location, Manufacturer, RentalStatus, Vehicle } from "types/types";
 
export const manufacturerDisplayConfig: InfoDisplayConfig<Manufacturer>[] = [
    { label: "ID", field: "id" },
    { label: "Name", field: "name" },
    { label: "State", field: "state" },
    { label: "Address", field: "address" },
    { label: "Phone", field: "phoneNumber" },
    { label: "Fax", field: "fax" },
    { label: "Email", field: "email" },
];

export const clientDisplayConfig: InfoDisplayConfig<Client>[] = [
    { label: "ID", field: "id" },
    { label: "Username", field: "username" },
    { label: "First name", field: "firstName" },
    { label: "Last name", field: "lastName" },
    { label: "Type", field: "type" },
    { label: "Active", field: "isActive" },
    { label: "Phone number", field: "phoneNumber" },
    { label: "Email", field: "email" },
    { label: "Drivers licence", field: "driversLicence" }
];

export const vehicleDisplayConfig: InfoDisplayConfig<Vehicle>[] = [
    { label: "ID", field: "id" },
    { label: "Model", field: "model" },
    { label: "Price", field: "price" },
    { 
        label: "Manufacturer", 
        field: "manufacturer",
        render: (manufacturer: Manufacturer | undefined) => 
            manufacturer ? `${manufacturer.name} (${manufacturer.state})` : "N/A"
    },
    { label: "Rental price", field: "rentalPrice" },
    { 
        label: "Rental status", 
        field: "rentalStatus",
        render: (rentalStatus: RentalStatus | undefined) => 
            rentalStatus ? `${rentalStatus.status}` : "N/A"
    },
    { label: "Type", field: "type_" },
    { label: "Active", field: "isActive" },
];

export const locationDisplayConfig: InfoDisplayConfig<Location>[] = [
    { label: "Latitude", field: "latitude" },
    { label: "Longitude", field: "longitude" },
];
