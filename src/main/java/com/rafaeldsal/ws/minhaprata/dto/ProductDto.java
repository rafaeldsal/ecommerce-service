package com.rafaeldsal.ws.minhaprata.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

  private Long id;

  @NotBlank(message = "não pode ser nulo ou vazio")
  @Size(min = 3, message = "valor mínimo igual a 3")
  private String name;

  @NotBlank(message = "não pode ser nulo ou vazio")
  private String description;

  @NotEmpty
  private BigDecimal price;

  private String imgUrl;

  @NotNull
  private Long categoryId;
}
