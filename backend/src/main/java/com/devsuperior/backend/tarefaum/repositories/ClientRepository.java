package com.devsuperior.backend.tarefaum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsuperior.backend.tarefaum.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

}
