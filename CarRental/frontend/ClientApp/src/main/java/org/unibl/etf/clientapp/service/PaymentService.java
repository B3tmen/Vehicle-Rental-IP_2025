package org.unibl.etf.clientapp.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.unibl.etf.clientapp.database.dao.interfaces.PaymentDAO;
import org.unibl.etf.clientapp.model.dto.Client;
import org.unibl.etf.clientapp.model.dto.Payment;

import java.security.MessageDigest;
import java.sql.Date;
import java.sql.SQLException;

public class PaymentService {
    private PaymentDAO dao;

    public PaymentService(PaymentDAO dao) {
        this.dao = dao;
    }

    public Payment insert(Payment payment) {
        Payment paymentInserted = null;
        Payment paymentByToken = getByToken(payment.getToken());
        if (paymentByToken == null) {
            try{
                paymentInserted = dao.insert(payment);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            paymentInserted = paymentByToken;
        }

        return paymentInserted;
    }

    public Payment getByClient(Client client){
        Payment payment = null;
        try{
            payment = dao.getByClient(client);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return payment;
    }

    public Payment getByToken(String token){
        Payment payment = null;
        try{
            payment = dao.getByToken(token);
        } catch (SQLException e){
            e.printStackTrace();
        }

        return payment;
    }

    public String generateCardToken(String cardNumber, String cvv){
        String uniqueNumber = cardNumber + cvv;
        return DigestUtils.sha256Hex(uniqueNumber);
    }

    public String extractLast4Digits(String cardNumber){
        int length = cardNumber.length();
        return cardNumber.substring(length-4);
    }

    public Date generateDate(String expiryDateMonth, String expiryDateYear) {
        // yyyy-MM-dd
        String dateString = expiryDateYear  + "-" + expiryDateMonth + "-01";
        return Date.valueOf(dateString);
    }
}
