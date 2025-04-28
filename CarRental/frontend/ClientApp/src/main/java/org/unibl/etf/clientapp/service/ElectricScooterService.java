package org.unibl.etf.clientapp.service;

import org.unibl.etf.clientapp.database.dao.interfaces.ElectricScooterDAO;
import org.unibl.etf.clientapp.model.dto.ElectricBicycle;
import org.unibl.etf.clientapp.model.dto.ElectricScooter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ElectricScooterService {
    private ElectricScooterDAO dao;

    public ElectricScooterService(ElectricScooterDAO dao) {
        this.dao = dao;
    }

    public List<ElectricScooter> getAll(){
        List<ElectricScooter> scooters = new ArrayList<>();
        try{
            scooters = dao.getAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        return scooters;
    }

    public ElectricScooter getById(int id){
        ElectricScooter scooter = null;
        try{
            scooter = dao.getById(id);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return scooter;
    }
}
