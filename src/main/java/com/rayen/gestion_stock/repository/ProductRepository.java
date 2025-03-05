package com.rayen.gestion_stock.repository;

import com.rayen.gestion_stock.entity.Category;
import com.rayen.gestion_stock.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
