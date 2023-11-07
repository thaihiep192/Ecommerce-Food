package com.example.library.service.impl;

import com.example.library.model.Country;
import com.example.library.repository.CountryRepository;
import com.example.library.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;
    @Override
    public List<Country> findAll() {
        return countryRepository.findAll();
    }
}
