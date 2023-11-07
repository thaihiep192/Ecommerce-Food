package com.example.library.service;

import com.example.library.dto.CustomerDto;
import com.example.library.model.Customer;

public interface CustomerService {
    Customer save(CustomerDto customerDto);
    Customer findByUsername(String username);
    Customer update(CustomerDto customerDto);
    Customer changePass(CustomerDto customerDto);
    CustomerDto getCustomer(String username);
}
