package org.unibl.etf.clientapp.factory.impl;

import org.unibl.etf.clientapp.database.dao.impl.RentalVehicleDAOImpl;
import org.unibl.etf.clientapp.factory.interfaces.DAOFactory;
import org.unibl.etf.clientapp.factory.interfaces.ServiceFactory;
import org.unibl.etf.clientapp.model.dto.RentalVehicle;
import org.unibl.etf.clientapp.service.*;

public class MySqlServiceFactoryImpl implements ServiceFactory {
    private final DAOFactory daoFactory;

    public MySqlServiceFactoryImpl(){
        daoFactory = new MySqlDAOFactoryImpl();
    }


    @Override
    public ClientService getClientService() {
        return new ClientService(daoFactory.getClientDAO());
    }

    @Override
    public RentalVehicleService getRentalVehicleService() {
        return new RentalVehicleService(new RentalVehicleDAOImpl());
    }

    @Override
    public ElectricCarService getElectricCarService() {
        return new ElectricCarService(daoFactory.getElectricCarDAO());
    }

    @Override
    public ElectricBicycleService getElectricBicycleService() {
        return new ElectricBicycleService(daoFactory.getElectricBicycleDAO());
    }

    @Override
    public ElectricScooterService getElectricScooterService() {
        return new ElectricScooterService(daoFactory.getElectricScooterDAO());
    }

    @Override
    public ImageService getImageService() {
        return new ImageService(daoFactory.getAvatarImageDAO());
    }

    @Override
    public LocationService getLocationService() {
        return new LocationService(daoFactory.getLocationDAO());
    }

    @Override
    public PassportService getPassportService() {
        return new PassportService(daoFactory.getPassportDAO());
    }

    @Override
    public PaymentService getPaymentService() {
        return new PaymentService(daoFactory.getPaymentDAO());
    }

    @Override
    public RentalService getRentalService() {
        return new RentalService(daoFactory.getRentalDAO());
    }

    @Override
    public InvoiceService getInvoiceService() {
        return new InvoiceService(daoFactory.getInvoiceDAO());
    }


}
