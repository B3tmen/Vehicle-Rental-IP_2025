package org.unibl.etf.clientapp.service;

import org.unibl.etf.clientapp.database.dao.impl.ElectricCarDAOImpl;
import org.unibl.etf.clientapp.database.dao.interfaces.ElectricCarDAO;
import org.unibl.etf.clientapp.model.dto.ElectricCar;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ElectricCarService {
    private ElectricCarDAO dao;

    public ElectricCarService(ElectricCarDAO dao){
        this.dao = dao;
    }

    public List<ElectricCar> getAll(){
        List<ElectricCar> cars = new ArrayList<>();
        try{
            cars = dao.getAll();
        } catch (SQLException e){
            e.printStackTrace();
        }

        return cars;
    }

    public ElectricCar getById(int id){
        ElectricCar car = null;
        try{
            car = dao.getById(id);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return car;
    }
}
