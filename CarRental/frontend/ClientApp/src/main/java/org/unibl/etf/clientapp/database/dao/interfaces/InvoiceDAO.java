package org.unibl.etf.clientapp.database.dao.interfaces;

import org.unibl.etf.clientapp.model.dto.Invoice;

import java.sql.SQLException;
import java.util.List;

public interface InvoiceDAO {
    Invoice insert(Invoice invoice) throws SQLException;
    List<Invoice> getAllByClientId(int clientId) throws SQLException;
}
