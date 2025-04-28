package org.unibl.etf.carrentalbackend.service.interfaces;

import org.unibl.etf.carrentalbackend.model.dto.ClientDTO;
import org.unibl.etf.carrentalbackend.model.entities.Client;

public interface ClientService extends CrudBaseService<Client, ClientDTO>, DtoImageInserterUpdater<ClientDTO> {
    ClientDTO getByUsername(String username);
}
