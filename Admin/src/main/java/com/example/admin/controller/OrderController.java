package com.example.admin.controller;

import com.example.library.model.Order;
import com.example.library.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class OrderController {
    private final OrderService orderService;
    @GetMapping("/orders")
    public String getAll(Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }else {
            List<Order> orderList = orderService.findAllOrders();
            model.addAttribute("orders",orderList);
            return "orders";
        }
    }
    @RequestMapping(value = "/accept-order", method = {RequestMethod.PUT, RequestMethod.GET})
    public String acceptOrder(Long id, RedirectAttributes attributes, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }else {
            orderService.acceptOrder(id);
            attributes.addFlashAttribute("success", "Order Accepted");
            return "redirect:/orders";
        }
    }
    @RequestMapping(value = "/cancel-order", method = {RequestMethod.PUT, RequestMethod.GET})
    public String cancelOrder(Long id, Principal principal,RedirectAttributes attributes){
        if (principal == null){
            return "redirect:/login";
        }else {
            orderService.cancelOrder(id);
            attributes.addFlashAttribute("success", "Order Deleted");
            return "redirect:/orders";
        }
    }
    @RequestMapping(value = "/find-order", method = {RequestMethod.GET, RequestMethod.PUT})
    public String findOrder(Long id,Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }else {
            Order order = orderService.findItemOrder(id);
            model.addAttribute("order", order);
            model.addAttribute("title", "Information order");
            model.addAttribute("page", "Information order");
            return "information-order";
        }
    }
}
