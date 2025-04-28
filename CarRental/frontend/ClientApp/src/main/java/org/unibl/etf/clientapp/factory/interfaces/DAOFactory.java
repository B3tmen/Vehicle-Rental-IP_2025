package org.unibl.etf.clientapp.factory.interfaces;

import org.unibl.etf.clientapp.database.dao.interfaces.*;
import org.unibl.etf.clientapp.model.dto.RentalVehicle;

public interface DAOFactory {
    ClientDAO getClientDAO();
    <T extends RentalVehicle> RentalVehicleDAO<T> getRentalVehicleDAO(Class<T> vehicleType);
    ElectricCarDAO getElectricCarDAO();
    ElectricBicycleDAO getElectricBicycleDAO();
    ElectricScooterDAO getElectricScooterDAO();
    AvatarImageDAO getAvatarImageDAO();
    VehicleImageDAO<RentalVehicle> getVehicleImageDAO();
    LocationDAO getLocationDAO();
    PassportDAO getPassportDAO();
    RentalDAO getRentalDAO();
    PaymentDAO getPaymentDAO();
    InvoiceDAO getInvoiceDAO();
}
