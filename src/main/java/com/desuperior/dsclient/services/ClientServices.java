package com.desuperior.dsclient.services;

import com.desuperior.dsclient.dto.ClientDTO;
import com.desuperior.dsclient.entities.Client;
import com.desuperior.dsclient.repository.ClientRepository;
import com.desuperior.dsclient.services.exceptions.ResourceEntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ClientServices {

    @Autowired
    private ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {
        Page<Client> list = clientRepository.findAll(pageRequest);
        return list.map(client -> new ClientDTO(client));
    }
    
    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) {
        Optional<Client> obj = clientRepository.findById(id);
        Client entity = obj.orElseThrow(() -> new ResourceEntityNotFoundException("Entity not found!"));
        return new ClientDTO(entity);
    }

    @Transactional
    public ClientDTO insert(ClientDTO clientDTO) {
        Client entity = new Client();
        entity.setName(clientDTO.getName());
        entity.setCpf(clientDTO.getCpf());
        entity.setIncome(clientDTO.getIncome());
        entity.setBirthDate(clientDTO.getBirthDate());
        entity.setChildren(clientDTO.getChildren());
        clientRepository.save(entity);
        return new ClientDTO(entity);
    }

    @Transactional
    public ClientDTO update(Long id, ClientDTO clientDTO) {
        try{
            Client entity = clientRepository.getOne(id);
            entity.setName(clientDTO.getName());
            entity.setCpf(clientDTO.getCpf());
            entity.setIncome(clientDTO.getIncome());
            entity.setBirthDate(clientDTO.getBirthDate());
            entity.setChildren(clientDTO.getChildren());
            entity = clientRepository.save(entity);
            return new ClientDTO(entity);
        }
        catch (EntityNotFoundException e){
            throw new ResourceEntityNotFoundException("Id not found " + id + "!");
        }
    }

    public void delete(Long id) {
        try{
            clientRepository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e){
            throw new ResourceEntityNotFoundException("Id not found " + id + "!");
        }
    }
}
