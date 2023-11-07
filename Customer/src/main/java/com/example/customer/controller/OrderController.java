package com.example.customer.controller;

import com.example.library.model.*;
import com.example.library.service.CityService;
import com.example.library.service.CountryService;
import com.example.library.service.CustomerService;
import com.example.library.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class OrderController {
    private final CustomerService customerService;
    private final OrderService orderService;
    private final CountryService countryService;
    private final CityService cityService;
    @GetMapping("/check-out")
    public String checkout(Model model, Principal principal){
        if (principal == null){
            return  "redirect:/login";
        }else {
            Customer customer = customerService.findByUsername(principal.getName());
            if (customer.getAddress() == null || customer.getCity() == null || customer.getPhoneNumber() == null){
                model.addAttribute("information", "You need update your information before check out!");
                List<Country> countryList = countryService.findAll();
                List<City> cities = cityService.findAll();
                model.addAttribute("customer", customer);
                model.addAttribute("cities", cities);
                model.addAttribute("countries", countryList);
                model.addAttribute("title", "Profile");
                model.addAttribute("page", "Profile");
                return "customer-information";
            }else {
                ShoppingCart cart = customerService.findByUsername(principal.getName()).getCart();
                model.addAttribute("customer",customer);
                model.addAttribute("title","Check-Out");
                model.addAttribute("page","Check-Out");
                model.addAttribute("shoppingCart", cart);
                model.addAttribute("grandTotal", cart.getTotalItems());
                return "checkout";
            }
        }

    }
    @GetMapping("/orders")
    public String getOrders(Principal principal, Model model)   {
        if (principal == null){
            return "redirect:/login";
        }else {
            Customer customer = customerService.findByUsername(principal.getName());
            List<Order> orderList = customer.getOrders();
            model.addAttribute("orders",orderList);
            model.addAttribute("title","Order");
            model.addAttribute("page","Order");
            return "order";
        }


    }
    @RequestMapping(value = "/cancel-order", method = {RequestMethod.GET, RequestMethod.PUT})
    public String cancelOrder(Long id, RedirectAttributes attributes){
        orderService.cancelOrder(id);
        attributes.addFlashAttribute("success","Cancel order successfully");
        return "redirect:/orders";
    }
    @RequestMapping(value = "/add-order", method = RequestMethod.POST)
    public String createOrder(Principal principal,
                              Model model,
                              HttpSession session){
        if (principal == null){
            return "redirect:/login";
        }else {
            Customer customer = customerService.findByUsername(principal.getName());
            ShoppingCart cart = customer.getCart();
            Order order = orderService.save(cart);
            session.removeAttribute("totalItems");
            model.addAttribute("order", order);
            model.addAttribute("title", "Order Detail");
            model.addAttribute("page", "Order Detail");
            model.addAttribute("success", "Add order successfully");
            return "order-detail";
        }
    }
    @RequestMapping(value = "/find-order", method = {RequestMethod.GET, RequestMethod.PUT})
    public String findOrder(Long id,Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }else {
            Order order = orderService.findItemOrder(id);
//            CartItem cartItem =
            model.addAttribute("order", order);
            model.addAttribute("title", "Order Detail");
            model.addAttribute("page", "Order Detail");
            model.addAttribute("success", "Add order successfully");
            return "information-order";
        }
    }
}
