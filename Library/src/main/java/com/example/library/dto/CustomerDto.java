package com.example.library.dto;

import com.example.library.model.City;
import com.example.library.model.Country;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    @Size(min = 3, max = 50, message = "First name should have 3-50 characters")
    private String firstName;
    @Size(min = 3, max = 50, message = "Last name should have 3-50 characters")
    private String lastName;
    private String username;
    @Size(min = 8, max = 50, message = "Password should hava 8-50 characters")
    private String password;
    @Size(min = 10, max = 15, message = "Phone number contains 10-15 characters")
    private String phoneNumber;
    private String address;
    private String confirmPassword;
    private City city;
    private String image;
    private Country country;

}
