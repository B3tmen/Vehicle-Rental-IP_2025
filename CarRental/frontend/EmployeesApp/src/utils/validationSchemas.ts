import * as yup from 'yup';

export const loginSchema = yup.object({
    username: yup.string().required('Username is required'),
    password: yup.string().required('Password is required').min(8, 'Password must be at least 8 characters')
}).required();

const phoneRegex = /^(?:\+387\s?|0)(\d{2})\/\d{3}-\d{3}$/;
export const userSchema = yup.object().shape({
  username: yup.string().required('Username is required'),
  passwordHash: yup.string().required('Password is required').min(8, 'Password must be at least 8 characters'),
  firstName: yup.string().required('First name is required'),
  lastName: yup.string().required('Last name is required'),
  type: yup.string().required('User type is required').oneOf(['Employee', 'Client']), // Add validation for possible values
  
  // For Employee type
  role: yup.string().when('type', {
    is: (value: string) => value === 'Employee', // Explicit check
    then: (schema) => schema.required('Role is required for employees'),
    otherwise: (schema) => schema.notRequired()
  }),
  
  // For Client type
  personalCardNumber: yup.string().when('type', {
    is: (value: string) => value === 'Client',
    then: (schema) => schema.required('Personal card number is required').length(13, 'Must be exactly 13 digits'),
    otherwise: (schema) => schema.notRequired()
  }),
  
  email: yup.string().when('type', {
    is: (value: string) => value === 'Client',
    then: (schema) => schema.email('Invalid email format').required('Email is required'),
    otherwise: (schema) => schema.notRequired()
  }),
  
  phoneNumber: yup.string().when('type', {
    is: (value: string) => value === 'Client',
    then: (schema) => schema.matches(phoneRegex, 'Invalid phone number. Format: 065/123-456 or +38765/123-456'),
    otherwise: (schema) => schema.notRequired()
  }),
  
  driversLicence: yup.string().when('type', {
    is: (value: string) => value === 'Client',
    then: (schema) => schema.required('Driver\'s license is required').length(9, 'Must be exactly 9 characters'),
    otherwise: (schema) => schema.notRequired()
  }),
  
  citizenType: yup.string().when('type', {
    is: (value: string) => value === 'Client',
    then: (schema) => schema.required('Citizen type is required'),
    otherwise: (schema) => schema.notRequired()
  })
});

export const manufacturerSchema = yup.object({
  name: yup.string().required('Name is required'),
  state: yup.string().required('State is required'),
  address: yup.string().required('Address is required'),
  phoneNumber: yup.string().matches(phoneRegex, 'Invalid phone number. Format: 065/123-456 or +38765/123-456'),
  fax: yup.string().required('Fax is required'),
  email: yup.string().email('Invalid email format').required('Email is required')
}).required();

export const vehicleSchema = yup.object().shape({
  model: yup.string().required('Model is required'),
  price: yup.number().positive('Price must be positive').required('Price is required'),
  rentalPrice: yup.number().positive('Rental price must be positive').required('Rental price is required'),
  manufacturer: yup.object().required('Manufacturer is required'),
  type_: yup.string().required('Type is required').oneOf(['Car', 'Bicycle', 'Scooter']),
  
  // Car specific fields
  carId: yup.string().when('type_', {
    is: (value: string) => value === 'Car',
    then: (schema) => schema.required('Car ID is required'),
    otherwise: (schema) => schema.notRequired()
  }),
  purchaseDate: yup.date().when('type_', {
    is: (value: string) => value === 'Car',
    then: (schema) => schema.required('Purchase date is required'),
    otherwise: (schema) => schema.notRequired()
  }),
  description: yup.string().when('type_', {
    is: (value: string) => value === 'Car',
    then: (schema) => schema.max(500, 'Description must be less than 500 characters')
  }),

  // Bicycle specific fields
  bicycleId: yup.string().when('type_', {
    is: (value: string) => value === 'Bicycle',
    then: (schema) => schema.required('Bicycle ID is required')
  }),
  ridingAutonomy: yup.number().when('type_', {
    is: (value: string) => value === 'Bicycle',
    then: (schema) => schema.positive('Autonomy must be positive').required('Autonomy is required')
  }),

  // Scooter specific fields
  scooterId: yup.string().when('type_', {
    is: (value: string) => value === 'Scooter',
    then: (schema) => schema.required('Scooter ID is required')
  }),
  maxSpeed: yup.number().when('type_', {
    is: (value: string) => value === 'Scooter',
    then: (schema) => schema.positive('Speed must be positive').required('Max speed is required')
  }),
});