package org.unibl.etf.carrentalbackend.service.interfaces;

import org.unibl.etf.carrentalbackend.interfaces.EntityDTOConverter;

import java.util.List;

public interface CrudBaseService<E, D> {
    List<D> getAll();
    D getById(int id);
    D insert(D dto);
    D update(D dto);
    boolean delete(Integer id);
}
