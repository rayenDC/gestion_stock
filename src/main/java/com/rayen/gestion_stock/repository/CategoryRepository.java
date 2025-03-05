package com.rayen.gestion_stock.repository;

import com.rayen.gestion_stock.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
