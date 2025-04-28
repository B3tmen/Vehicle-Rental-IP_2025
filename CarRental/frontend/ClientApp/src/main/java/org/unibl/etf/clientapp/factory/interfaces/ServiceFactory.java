package org.unibl.etf.clientapp.factory.interfaces;

import org.unibl.etf.clientapp.service.*;

public interface ServiceFactory {
    ClientService getClientService();
    RentalVehicleService getRentalVehicleService();
    ElectricCarService getElectricCarService();
    ElectricBicycleService getElectricBicycleService();
    ElectricScooterService getElectricScooterService();
    ImageService getImageService();
    LocationService getLocationService();
    PassportService getPassportService();
    PaymentService getPaymentService();
    RentalService getRentalService();
    InvoiceService getInvoiceService();
}
