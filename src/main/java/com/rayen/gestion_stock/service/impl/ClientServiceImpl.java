package com.rayen.gestion_stock.service.impl;

import com.rayen.gestion_stock.dto.ClientDTO;
import com.rayen.gestion_stock.dto.Response;
import com.rayen.gestion_stock.entity.Client;
import com.rayen.gestion_stock.exceptions.NotFoundException;
import com.rayen.gestion_stock.repository.ClientRepository;
import com.rayen.gestion_stock.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response addClient(ClientDTO clientDTO) {
        Client client = modelMapper.map(clientDTO, Client.class);
        clientRepository.save(client);

        return Response.builder().status(200).message("Client added successfully").build();
    }

    @Override
    public Response updateClient(Long id, ClientDTO clientDTO) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found"));

        if (clientDTO.getName() != null) client.setName(clientDTO.getName());
        if (clientDTO.getEmail() != null) client.setEmail(clientDTO.getEmail());
        if (clientDTO.getPhoneNumber() != null) client.setPhoneNumber(clientDTO.getPhoneNumber());

        clientRepository.save(client);

        return Response.builder().status(200).message("Client updated").build();
    }

    @Override
    public Response getAllClients() {
        List<Client> clients = clientRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<ClientDTO> dtos = modelMapper.map(clients, new TypeToken<List<ClientDTO>>() {}.getType());
        return Response.builder().status(200).message("success").clients(dtos).build();
    }

    @Override
    public Response getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found"));
        ClientDTO dto = modelMapper.map(client, ClientDTO.class);
        return Response.builder().status(200).message("success").client(dto).build();
    }

    @Override
    public Response deleteClient(Long id) {
        clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found"));
        clientRepository.deleteById(id);
        return Response.builder().status(200).message("Client deleted").build();
    }
}
