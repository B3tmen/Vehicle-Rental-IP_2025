package org.unibl.etf.clientapp.database.dao.interfaces;

import org.unibl.etf.clientapp.model.dto.Image;

import java.sql.SQLException;

public interface ImageDAO<T> {
    boolean insertImage(T entity, Image image) throws SQLException;
    boolean updateImage(T entity, Image image) throws SQLException;
    Image getImageByEntity(T entity) throws SQLException;
}
