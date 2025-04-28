import { Bicycle, Car, Client, Employee, Scooter, User, Vehicle } from "../types/types";

/**
 * Type guard to check if a user is a Client
 */
export function isClient(user: User): user is Client {
    return (
        user.type === 'Client' &&
        (user as Client).personalCardNumber !== undefined &&
        (user as Client).email !== undefined &&
        (user as Client).phoneNumber !== undefined &&
        (user as Client).citizenType !== undefined
    );
}

/**
 * Type guard to check if a user is an Employee
 */
export function isEmployee(user: User): user is Employee {
    return user.type === 'Employee' && (user as Employee).role !== undefined;
}

export function isCar(vehicle: Vehicle): vehicle is Car {
    return (
        vehicle.type_ === 'Car' &&
        (vehicle as Car).carId !== null &&
        (vehicle as Car).purchaseDate !== undefined &&
        (vehicle as Car).description !== undefined
    );
}

export function isBicycle(vehicle: Vehicle): vehicle is Bicycle {
    return (
        vehicle.type_ === 'Bicycle' &&
        (vehicle as Bicycle).bicycleId !== null &&
        (vehicle as Bicycle).ridingAutonomy !== undefined
    );
}

export function isScooter(vehicle: Vehicle): vehicle is Scooter {
    return (
        vehicle.type_ === 'Scooter' &&
        (vehicle as Scooter).scooterId !== null &&
        (vehicle as Scooter).maxSpeed !== undefined
    );
}