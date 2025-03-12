package com.rayen.gestion_stock.service;


import com.rayen.gestion_stock.dto.ProductDTO;
import com.rayen.gestion_stock.dto.Response;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    Response saveProduct(ProductDTO productDTO, MultipartFile imageFile);
    Response updateProduct(ProductDTO productDTO, MultipartFile imageFile);
    Response getAllProducts();
    Response getProductById(Long id);
    Response deleteProduct(Long id);
}
