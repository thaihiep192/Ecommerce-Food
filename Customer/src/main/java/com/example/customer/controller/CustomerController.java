package com.example.customer.controller;

import com.example.library.dto.CustomerDto;
import com.example.library.model.City;
import com.example.library.model.Country;
import com.example.library.model.Customer;
import com.example.library.service.CityService;
import com.example.library.service.CountryService;
import com.example.library.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class CustomerController {
    private final CountryService countryService;
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;
    private final CityService cityService;
    @GetMapping("/profile")
    public  String profile(Model model, Principal principal){
        if (principal == null){
            return "redirect:/login";
        }
        String username = principal.getName();
        CustomerDto customer = customerService.getCustomer(username);
        List<Country> countryList = countryService.findAll();
        List<City> cities = cityService.findAll();
        model.addAttribute("cities", cities);
        model.addAttribute("countries", countryList);
        model.addAttribute("title","Profile");
        model.addAttribute("page","Profile");
        model.addAttribute("customer", customer);
        return "customer-information";
    }
    @PostMapping("update-profile")
    public String updateProfile(@Valid @ModelAttribute("customer") CustomerDto customerDto,
                                BindingResult result,
                                RedirectAttributes attributes,
                                Model model,
                                Principal principal){
        if (principal == null){
            return "redirect:/login";
        }else {
            String username = principal.getName();
            List<Country> countryList = countryService.findAll();
            List<City> cities = cityService.findAll();
            model.addAttribute("countries", countryList);
            model.addAttribute("cities", cities);
            if (result.hasErrors()){
                return "customer-information";
            }
            customerService.update(customerDto);
            CustomerDto customerUpdate = customerService.getCustomer(principal.getName());
            attributes.addFlashAttribute("success","Update successfully");
            model.addAttribute("customer", customerUpdate);
            return "redirect:/profile";
        }
    }


}
