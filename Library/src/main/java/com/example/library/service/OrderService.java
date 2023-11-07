package com.example.library.service;

import com.example.library.model.Order;
import com.example.library.model.ShoppingCart;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order save(ShoppingCart shoppingCart);
    List<Order> findAll(String username);
    List<Order> findAllOrders();

    Order acceptOrder(Long id);
    void cancelOrder(Long id);
    Order findItemOrder(Long id);
}
