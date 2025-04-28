import { Employee, Image, Malfunction, Manufacturer, RentalStatus, UserSubType, Vehicle, VehicleSubType } from "types/types";

export const emptyImageObject: Image = {
    id: null, 
    name: '', 
    type: '', 
    url: ''
}

export const emptyRentalStatusObject: RentalStatus = {
    id: 1, status: 'Free'
}

export const emptyVehicleObject: VehicleSubType = {
    id: null,
    image: emptyImageObject,
    model: '',
    price: 0,
    rentalPrice: 0,
    manufacturer: undefined,
    rentalStatus: emptyRentalStatusObject,
    type_: '',
    promotion: undefined,
    announcement: undefined,
    isActive: true,

    carId: '',                  // Car specific
    purchaseDate: new Date(),
    description: '',

    bicycleId: '',              // Bicycle specific
    ridingAutonomy: 0,

    scooterId: '',              // Scooter specific
    maxSpeed: 0,
}

export const emptyUserObject: UserSubType = {
    id: null,
    username: '',
    passwordHash: '',
    firstName: '',
    lastName: '',
    type: '',
    isActive: true,

    role: '',   // Employee-specific

    personalCardNumber: '', // Client-specific
    email: '', // Client-specific
    phoneNumber: '', // Client-specific
    avatarImage: null, // Client-specific
    citizenType: '', // Client-specific
    driversLicence: 0, // Client-specific
};

export const emptyEmployeeObject: Employee = {
    id: null,
    username: '',
    passwordHash: '',
    firstName: '',
    lastName: '',
    type: '',
    isActive: true,
    role: '',   // Employee-specific
}

export const emptyMalfunctionObject: Malfunction = {
    id: null,
    reason: "",
    timeOfMalfunction: new Date()
}

export const emptyManufacturerObject: Manufacturer = {
    name: '',
    state: '',
    address: '',
    phoneNumber: '',
    fax: '',
    email: ''
};