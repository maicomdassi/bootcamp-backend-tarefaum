package com.devsuperior.backend.tarefaum.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.backend.tarefaum.dto.ClientDTO;
import com.devsuperior.backend.tarefaum.entities.Client;
import com.devsuperior.backend.tarefaum.repositories.ClientRepository;
import com.devsuperior.backend.tarefaum.services.exceptions.DatabaseException;
import com.devsuperior.backend.tarefaum.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository repository;

	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {

		Page<Client> page = repository.findAll(pageRequest);

		return page.map(x -> new ClientDTO(x));
	}

	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {

		Optional<Client> op = repository.findById(id);
		Client entity = op.orElseThrow(() -> new ResourceNotFoundException("Nenhum cliente encontrado"));

		return new ClientDTO(entity);
	}

	@Transactional
	public ClientDTO insert(ClientDTO dto) {

		Client entity = new Client();
		copyDtoEntity(dto, entity);
		entity = repository.save(entity);

		return new ClientDTO(entity);
	}

	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
			Client entity = repository.getOne(id);
			copyDtoEntity(dto, entity);
			entity = repository.save(entity);

			return new ClientDTO(entity);

		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	public void delete(Long id) {
		try {

			repository.deleteById(id);

		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation ");
		}
	}

	private void copyDtoEntity(ClientDTO dto, Client entity) {

		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		entity.setBirthDate(dto.getBirthDate());
		entity.setChildren(dto.getChildren());

	}
}
