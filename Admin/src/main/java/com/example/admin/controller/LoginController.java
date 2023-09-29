package com.example.admin.controller;

import com.example.library.dto.AdminDto;
import com.example.library.model.Admin;
import com.example.library.service.impl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class LoginController {
    @Autowired
    private AdminServiceImpl adminService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @GetMapping("/login")
    public String loginForm(){
        return "authentication-login";
    }
    @GetMapping("/register")
    public String registerForm(Model model){
        model.addAttribute("adminDto", new AdminDto());
        return "authentication-register";
    }
    @GetMapping("/index")
    public String indexForm(){
        return "index";
    }
    @GetMapping("/forgot-password")
    public String forgotPassword(){
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
                System.out.println("nhap sai");
                result.toString();
                return "authentication-register";

            }
            String username = adminDto.getUsername();
            Admin admin = adminService.findByUsername(username);
            if (admin != null){
                model.addAttribute("adminDto",adminDto);
                model.addAttribute("emilError", "Your email has been registered!");
                System.out.println("admin not null");
                return "authentication-register";

            }
            if(adminDto.getPassword().equals(adminDto.getRepeatPassword())){
                adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));
                adminService.save(adminDto);
                System.out.println("success");
                model.addAttribute("adminDto",adminDto);
                model.addAttribute("success", " Register successfully!");
            }else {
                model.addAttribute("adminDto", adminDto);
                model.addAttribute("passwordError","Your password maybe wrong! Check again!" );
                System.out.println("password not same");
                return "authentication-register";
            }
            }catch (Exception e){
            model.addAttribute("errors"," Can not register because error serer");
        }
        return "authentication-register";
    }
}
