package org.unibl.etf.carrentalbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.carrentalbackend.model.dto.ClientDTO;
import org.unibl.etf.carrentalbackend.model.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    Client findByUsername(String username);
    Client findByPhoneNumber(String phoneNumber);
    Client findByPersonalCardNumber(String personalCardNumber);
}
