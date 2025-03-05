package com.rayen.gestion_stock.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO {

   private Long id ;

    private Long productId;
    private Long categoryId;
    private Long supplierId;

    private String name;

    private String sku;

    private BigDecimal price ;

    private Integer stockQuantity ;

    private String description;

    private String imageUrl ;

    private LocalDateTime expiryDate;

    private LocalDateTime UpdatedAt;

    private  LocalDateTime createdAT ;



}
