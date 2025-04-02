package com.rafaeldsal.ws.minhaprata.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private Long id;

    @NotBlank(message = "não pode ser nulo ou vazio")
    @Size(min = 4, max = 15, message = "deve ter tamanho entre 4 e 15")
    private String name;

    @Size(min = 5, max = 255, message = "deve ter tamanho entre 5 e 255")
    private String description;
}