
export interface LoginRequest {
    username: string;
    password: string;
}

export interface AuthenticationResponse {
    user: Employee;
    jwtToken: string;
    roles: string[];
}

export interface JwtPayload {
    sub: string;
    role: string;
    iat: number;
    exp: number;
}

export interface Manufacturer {
    id: number | null,
    name: string,
    state: string,
    address: string,
    phoneNumber: string,
    fax: string,
    email: string
}

export interface Promotion {
    id: number;
    title: string;
    description: string;
    duration: Date;
}

export interface Announcement {
    id: number;
    title: string;
    content: string;
}

export interface RentalStatus {
    id: number;
    status: string;
}

export interface VehicleType {
    name: string;
}

export interface Vehicle{
    id: number | null;
    image: Image | null;
    model: string;
    price: number;
    rentalPrice: number;
    manufacturer?: Manufacturer;
    rentalStatus: RentalStatus;
    type_: string;
    promotion?: Promotion;
    announcement?: Announcement;
    isActive: boolean;
}

export interface Car extends Vehicle {
    carId: string;
    purchaseDate: Date;
    description: string;
}

export interface Bicycle extends Vehicle {
    bicycleId: string;
    ridingAutonomy: number;
}

export interface Scooter extends Vehicle {
    scooterId: string;
    maxSpeed: number;
}

export type VehicleSubType = Car | Bicycle | Scooter;

export interface VehicleType{
    name: string;
    imgUrl: string;
}

export interface Image{
    id: number | null;
    name: string;
    type: string;
    url: string;
}

export interface User{
    id: number | null;
    username: string;
    passwordHash: string;
    firstName: string;
    lastName: string;
    type: string;
    isActive: boolean;
}

export interface Client extends User{
    personalCardNumber: string;
    email: string;
    phoneNumber: string;
    avatarImage: Image | null;
    citizenType: string;
    driversLicence: number;
}

export interface Employee extends User {
    role: string;
}

export type UserSubType = Client | Employee;

export interface Location{
    latitude: number;
    longitude: number;
}

export interface Rental{
    id: number;
    vehicle: Vehicle;
    client: Client;
    rentalDateTime: string;
    duration: number;
    pickupLocation: Location;
    dropoffLocation: Location;
}

export interface MapVehicle {
    vehicle: Vehicle;
    location: Location;
}

export interface Malfunction {
    id: number | null;
    reason: string;
    timeOfMalfunction: Date;
}

export interface VehicleMalfunction {
    vehicle: Vehicle;
    malfunction: Malfunction;
}

export interface StatisticsMapData {
    [key: string]: number;  // Map<String, Integer> will be received from backend
}

export interface MonthlyIncome {
    date: string;
    income: number;
}

export interface VehicleTypeIncome {
    vehicleType: string;
    income: number;
}

export interface InfoDisplayConfig<T> {
    label: string;
    field: keyof T;
    render?: (value: any, item: T) => React.ReactNode;
}

export interface CustomError {
    status: number;
    message: string;
}