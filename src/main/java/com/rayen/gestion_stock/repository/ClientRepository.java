package com.rayen.gestion_stock.repository;

import com.rayen.gestion_stock.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
