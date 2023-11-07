package com.example.library.service.impl;

import com.example.library.dto.CategoryDto;
import com.example.library.model.Category;
import com.example.library.repository.CartItemRepository;
import com.example.library.repository.CategoryRepository;
import com.example.library.service.CategoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category save(Category category) {
        Category categorySave = new Category(category.getName());
        return categoryRepository.save(categorySave);

    }

    @Override
    public Category update(Category category) {
        Category category1 = null;
        try {
            category1 = categoryRepository.findById(category.getId()).get();
            category1.setName(category.getName());
            category1.setActivated(category.isActivated());
            category1.setDeleted(category.isDeleted());
        }catch (Exception e){
            e.printStackTrace();
        }
        return categoryRepository.save(category1);
    }

    @Override
    public List<Category> findAllByActivatedTrue() {
        return categoryRepository.findAllByaActivated();
    }

    @Override
    public List<Category> findALl() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        Category category = categoryRepository.getReferenceById(id);
        category.setActivated(false);
        category.setDeleted(true);
        categoryRepository.save(category);
    }

    @Override
    public void enableById(Long id) {
        Category category = categoryRepository.getReferenceById(id);
        category.setActivated(true);
        category.setDeleted(false);
        categoryRepository.save(category);
    }

    @Override
    public List<CategoryDto> getCategoriesAndSize() {
        List<CategoryDto> categories = categoryRepository.getCategoriesBySize();
        return categories;
    }



}
