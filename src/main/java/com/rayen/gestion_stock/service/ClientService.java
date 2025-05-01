package com.rayen.gestion_stock.service;

import com.rayen.gestion_stock.dto.ClientDTO;
import com.rayen.gestion_stock.dto.Response;

public interface ClientService {
    Response addClient(ClientDTO clientDTO);
    Response updateClient(Long id, ClientDTO clientDTO);
    Response getAllClients();
    Response getClientById(Long id);
    Response deleteClient(Long id);
}
