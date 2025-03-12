package com.rayen.gestion_stock.repository;

import com.rayen.gestion_stock.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
