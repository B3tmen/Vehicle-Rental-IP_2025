package org.unibl.etf.clientapp.service;

import org.unibl.etf.clientapp.database.dao.interfaces.ElectricBicycleDAO;
import org.unibl.etf.clientapp.database.dao.interfaces.ElectricScooterDAO;
import org.unibl.etf.clientapp.model.dto.ElectricBicycle;
import org.unibl.etf.clientapp.model.dto.ElectricCar;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ElectricBicycleService {
    private ElectricBicycleDAO dao;

    public ElectricBicycleService(ElectricBicycleDAO dao) {
        this.dao = dao;
    }

    public List<ElectricBicycle> getAll(){
        List<ElectricBicycle> bicycles = new ArrayList<>();
        try{
            bicycles = dao.getAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        return bicycles;
    }

    public ElectricBicycle getById(int id){
        ElectricBicycle bicycle = null;
        try{
            bicycle = dao.getById(id);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return bicycle;
    }
}
