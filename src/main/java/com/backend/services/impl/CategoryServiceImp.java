package com.backend.services.impl;

import com.backend.projections.CategoryClosedProjection;
import com.backend.repository.CategoryRepository;
import com.backend.services.interfaces.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryClosedProjection> findAllCategories() {
        return categoryRepository.findBy();
    }
}
