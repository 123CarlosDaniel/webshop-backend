package com.backend.services.interfaces;

import com.backend.projections.CategoryClosedProjection;

import java.util.List;

public interface CategoryService {
    List<CategoryClosedProjection> findAllCategories();
}
