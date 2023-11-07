package com.example.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
@Entity
@Table(name = "order_detail")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long id;
    private int quantity;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id",referencedColumnName = "order_id")
    private Order order;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",referencedColumnName = "product_id")
    private Product product;

}
