package com.example.library.model;

import lombok.*;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@AllArgsConstructor

@Entity
@Table(name = "shopping_cart")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shopping_cart_id")
    private Long id;
    private int totalItems;
    private double totalPrices;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart")
    private Set<CartItem> cartItems;
    public ShoppingCart(){
        this.cartItems = new HashSet<>();
        this.totalItems = 0;
        this.totalPrices = 0.0;
    }
}
