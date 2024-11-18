package com.store.storemanagement.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long id;
    @NotBlank(message = "Product name is mandatory")
    private String name;

    @NotNull(message = "Price is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price must be have max 10 digits and 2 decimals")
    private Double price;

    @Size(max = 500, message = "Description can be at most 500 characters")
    private String description;

    @NotNull(message = "Stock quantity is mandatory")
    @Min(value = 0, message = "Stock quantity must be 0 or more")
    private Integer stockQuantity;

}

