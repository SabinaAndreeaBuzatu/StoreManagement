package com.store.storemanagement.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private Long id;

    @NotEmpty(message = "Order must contain at least one item")
    private List<@Valid OrderItemDTO> items;

    private Double totalAmount;

    private String status;


}
