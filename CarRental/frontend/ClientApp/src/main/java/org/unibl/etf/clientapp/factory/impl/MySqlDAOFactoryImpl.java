package org.unibl.etf.clientapp.factory.impl;

import org.unibl.etf.clientapp.database.dao.impl.*;
import org.unibl.etf.clientapp.database.dao.interfaces.*;
import org.unibl.etf.clientapp.factory.interfaces.DAOFactory;
import org.unibl.etf.clientapp.model.dto.ElectricBicycle;
import org.unibl.etf.clientapp.model.dto.ElectricCar;
import org.unibl.etf.clientapp.model.dto.ElectricScooter;
import org.unibl.etf.clientapp.model.dto.RentalVehicle;

public class MySqlDAOFactoryImpl implements DAOFactory {
    @Override
    public ClientDAO getClientDAO() {
        return new ClientDAOImpl();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends RentalVehicle> RentalVehicleDAO<T> getRentalVehicleDAO(Class<T> vehicleType) {
        if (vehicleType == ElectricCar.class) {
            return (RentalVehicleDAO<T>) getElectricCarDAO();
        }
        else if (vehicleType == ElectricBicycle.class) {
            return (RentalVehicleDAO<T>) getElectricBicycleDAO();
        }
        else if (vehicleType == ElectricScooter.class) {
            return (RentalVehicleDAO<T>) getElectricScooterDAO();
        }

        throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType.getName());
    }

    @Override
    public ElectricCarDAO getElectricCarDAO() {
        return new ElectricCarDAOImpl();
    }

    @Override
    public ElectricBicycleDAO getElectricBicycleDAO() {
        return new ElectricBicycleDAOImpl();
    }

    @Override
    public ElectricScooterDAO getElectricScooterDAO() {
        return new ElectricScooterDAOImpl();
    }

    @Override
    public AvatarImageDAO getAvatarImageDAO() { return new AvatarImageDAOImpl(); }

    @Override
    public VehicleImageDAO<RentalVehicle> getVehicleImageDAO() {
        return new VehicleImageDAOImpl();
    }

    @Override
    public LocationDAO getLocationDAO() {
        return new LocationDAOImpl();
    }

    @Override
    public PassportDAO getPassportDAO() {
        return new PassportDAOImpl();
    }

    @Override
    public RentalDAO getRentalDAO() {
        return new RentalDAOImpl();
    }

    @Override
    public PaymentDAO getPaymentDAO() {
        return new PaymentDAOImpl();
    }

    @Override
    public InvoiceDAO getInvoiceDAO() {
        return new InvoiceDAOImpl();
    }

}
