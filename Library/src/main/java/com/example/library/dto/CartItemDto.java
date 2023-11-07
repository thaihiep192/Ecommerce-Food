package com.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private Long id;
    private ShoppingCartDto cart;
    private ProductDto product;
    private int quantity;
    private double unitPrice;

}
