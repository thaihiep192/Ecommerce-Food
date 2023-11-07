package com.example.admin.controller;

import com.example.library.dto.AdminDto;
import com.example.library.model.Admin;
import com.example.library.service.impl.AdminServiceImpl;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AdminServiceImpl adminService;

    private final BCryptPasswordEncoder passwordEncoder;
    @GetMapping("/login")
    public String loginForm(Model model){
        model.addAttribute("title", "Login Page");
        return "login";
    }
    @RequestMapping("/index")
    public String home(Model model  ){
        model.addAttribute("title", "Home Page");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }
        return "index";
    }
    @GetMapping("/register")
    public String registerForm(Model model){
        model.addAttribute("adminDto", new AdminDto());
        return "register";
    }
    @GetMapping("/forgot-password")
    public String forgotPassword(Model model){
        model.addAttribute("title","Forgot Password");
        return "forgot-password";
    }
    @PostMapping("/register-new")
    public String addNewAdmin(@Valid @ModelAttribute("adminDto")AdminDto adminDto,
                              BindingResult result,
                              Model model
                              ){
        try {

            if (result.hasErrors()){
                model.addAttribute("adminDto", adminDto);
                return "register";
            }
            String username = adminDto.getUsername();
            Admin admin = adminService.findByUsername(username);
            if (admin != null){
                model.addAttribute("adminDto",adminDto);
                model.addAttribute("emailError", "Your email has been registered!");
                return "register";

            }
            if(adminDto.getPassword().equals(adminDto.getRepeatPassword())){
                adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));
                model.addAttribute("adminDto",adminDto);
                model.addAttribute("success", " Register successfully!");
                adminService.save(adminDto);
            }else {
                model.addAttribute("adminDto", adminDto);
                model.addAttribute("passwordError","Your password maybe wrong! Check again!" );
                return "register";
            }
            }
        catch (Exception e){
            e.printStackTrace();
            model.addAttribute("errors","The server has been wrong!");
        }
        return "register";
    }
}
