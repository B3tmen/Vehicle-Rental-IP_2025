package org.unibl.etf.carrentalbackend.interfaces;

public interface EntityDTOConverter<E, D>  {
    D convertToDTO(E entity);
    E convertToEntity(D dto);
}
