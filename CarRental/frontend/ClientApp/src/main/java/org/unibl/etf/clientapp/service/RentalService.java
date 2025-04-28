package org.unibl.etf.clientapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unibl.etf.clientapp.database.dao.interfaces.RentalDAO;
import org.unibl.etf.clientapp.factory.impl.MySqlServiceFactoryImpl;
import org.unibl.etf.clientapp.factory.interfaces.ServiceFactory;
import org.unibl.etf.clientapp.model.dto.Location;
import org.unibl.etf.clientapp.model.dto.Rental;
import org.unibl.etf.clientapp.util.CustomLogger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RentalService {
    private final CustomLogger logger = CustomLogger.getInstance(RentalService.class);
    private final RentalDAO dao;
    private final LocationService locationService;

    public RentalService(RentalDAO dao){
        this.dao = dao;

        ServiceFactory serviceFactory = new MySqlServiceFactoryImpl();
        this.locationService = serviceFactory.getLocationService();
    }

    public Rental insert(Rental rental) {
        Rental inserted = null;
        Location pickupLocation = rental.getPickupLocation();
        Location dropoffLocation = rental.getDropoffLocation();
        Location pickupExists = checkLocationExists(pickupLocation);
        Location dropoffExists = checkLocationExists(dropoffLocation);

        if(pickupExists != null) rental.setPickupLocation(pickupExists);
        if(dropoffExists != null) rental.setDropoffLocation(dropoffExists);

        if(pickupExists == null && dropoffExists == null) {
            Location insertedPickup = locationService.insert(pickupLocation);
            Location insertedDropoff = locationService.insert(dropoffLocation);
            if(insertedPickup != null && insertedDropoff != null) {
                rental.setPickupLocation(insertedPickup);       // For id field, initially it's null
                rental.setDropoffLocation(insertedDropoff);

                inserted = getInserted(rental);
            }
        }
        else{
            inserted = getInserted(rental);
        }
        
        return inserted;
    }

    private Rental getInserted(Rental rental) {
        Rental insertedRental = null;
        try{
            insertedRental = dao.insert(rental);
        } catch (SQLException e){
            logger.error(e.getMessage());
        }
        return insertedRental;
    }

    private Location checkLocationExists(Location location) {
        return locationService.getByLatitudeLongitude(location.getLatitude(), location.getLongitude());
    }
}
