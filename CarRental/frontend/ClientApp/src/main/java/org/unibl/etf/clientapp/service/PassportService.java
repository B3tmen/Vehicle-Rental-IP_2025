package org.unibl.etf.clientapp.service;

import org.unibl.etf.clientapp.database.dao.interfaces.PassportDAO;
import org.unibl.etf.clientapp.model.dto.Passport;

import java.sql.SQLException;

public class PassportService {
    private PassportDAO dao;

    public PassportService(PassportDAO dao) {
        this.dao = dao;
    }

    public int insert(Passport passport, int clientId) {
        int inserted = 0;
        try{
            inserted = dao.insert(passport, clientId);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return inserted;
    }
}
