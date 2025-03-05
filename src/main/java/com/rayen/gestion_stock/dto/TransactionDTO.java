package com.rayen.gestion_stock.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rayen.gestion_stock.enums.TransactionStatus;
import com.rayen.gestion_stock.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private Integer totalProducts;

    private BigDecimal totalPrice;


    private TransactionType transactionType;

    private TransactionStatus status;

    private String description;

    private LocalDateTime UpdatedAt;

    private  LocalDateTime createdAT ;

    private UserDTO user;

    private ProductDTO product;

    private SupplierDTO supplier;


}
