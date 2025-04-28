package org.unibl.etf.carrentalbackend.controller;

import lombok.CustomLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.carrentalbackend.model.dto.ClientDTO;
import org.unibl.etf.carrentalbackend.service.interfaces.ClientService;
import org.unibl.etf.carrentalbackend.util.CustomLogger;

import java.util.List;

import static org.unibl.etf.carrentalbackend.util.Constants.EndpointUrls.API_CLIENTS_URL;

@RestController
@RequestMapping(API_CLIENTS_URL)
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAll() {
        List<ClientDTO> clients = clientService.getAll();
        CustomLogger.getInstance().info("[Server]: Sent all clients.");
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getById(@PathVariable int id) {
        ClientDTO client = clientService.getById(id);
        if(client != null){
            CustomLogger.getInstance().info("[Server]: Sent client with ID: " + client.getId());
            return ResponseEntity.ok(client);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ClientDTO> insert(@Validated @RequestPart("client") ClientDTO clientDTO,
                                            @RequestPart(value = "image", required = false) MultipartFile file) {

        ClientDTO insertedClient = clientService.insert(clientDTO, file);
        if(insertedClient != null){
            CustomLogger.getInstance().info("[Server]: Inserted client with ID: " + insertedClient.getId());
            return ResponseEntity.ok(insertedClient);
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(path="/{id}")
    public ResponseEntity<ClientDTO> update(@Validated @RequestBody ClientDTO clientDTO) {
        ClientDTO updatedClient = clientService.update(clientDTO);
        if(updatedClient != null){
            CustomLogger.getInstance().info("[Server]: Updated client with ID: " + updatedClient.getId());
            return ResponseEntity.ok(updatedClient);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(path="/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ClientDTO> update(@Validated @RequestPart("client") ClientDTO clientDTO,
                                            @RequestPart(value = "image", required = false) MultipartFile file, @PathVariable int id) {
        if(clientDTO.getId() == null)
            clientDTO.setId(id);      // I have no clue why this becomes null

        ClientDTO updatedClient = clientService.update(clientDTO, file);
        if(updatedClient != null){
            CustomLogger.getInstance().info("[Server]: Updated client with ID: " + updatedClient.getId());
            return ResponseEntity.ok(updatedClient);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        boolean deleted = clientService.delete(id);

        if(deleted){
            return ResponseEntity.noContent().build();
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
}
