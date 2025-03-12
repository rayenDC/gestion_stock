package com.rayen.gestion_stock.service;


import com.rayen.gestion_stock.dto.CategoryDTO;
import com.rayen.gestion_stock.dto.Response;

public interface CategoryService {
    Response createCategory(CategoryDTO categoryDTO);
    Response getAllCategories();
    Response getCategoryById(Long id);
    Response updateCategory(Long id, CategoryDTO categoryDTO);
    Response deleteCategory(Long id);
}
