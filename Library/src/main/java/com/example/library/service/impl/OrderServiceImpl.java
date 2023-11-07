package com.example.library.service.impl;

import com.example.library.model.*;
import com.example.library.repository.*;
import com.example.library.service.OrderService;
import com.example.library.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDetailRepository orderDetailRepository;

    private final OrderRepository orderRepository;

    private final ShoppingCartService cartService;

    private final CustomerRepository customerRepository;

    @Override
    public Order save(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setCustomer(shoppingCart.getCustomer());
        order.setTax(2);
        order.setTotalPrice(shoppingCart.getTotalPrices());
        order.setAccept(false);
        order.setPaymentMethod("Cash");
        order.setOrderStatus("Pending");
        order.setQuantity(shoppingCart.getTotalItems());
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (CartItem item : shoppingCart.getCartItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(item.getProduct());
            orderDetail.setQuantity(item.getQuantity());
            orderDetailRepository.save(orderDetail);
            orderDetailList.add(orderDetail);
        }
        order.setOrderDetailList(orderDetailList);
        cartService.deleteCartById(shoppingCart.getId());
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findAll(String username) {
        Customer customer = customerRepository.findByUsername(username);
        List<Order> orders = customer.getOrders();
        return orders;
    }

    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }



    @Override
    public Order acceptOrder(Long id) {
        Order order = orderRepository.getReferenceById(id);
        order.setAccept(true);
        order.setDeliveryDate(new Date());
        return orderRepository.save(order);
    }

    @Override
    public void cancelOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Order findItemOrder(Long id) {
        return orderRepository.findById(id).get();
    }
}
