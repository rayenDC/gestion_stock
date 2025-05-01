package com.rayen.gestion_stock.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String email;
    private String phoneNumber;
}
