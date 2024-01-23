package com.backend.repository;

import com.backend.models.entities.Category;
import com.backend.models.entities.Product;
import com.backend.projections.ProductProjection;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<ProductProjection> findByCategory(Category category, Sort sort);
    List<ProductProjection> findBy(Sort sort);

    Optional<ProductProjection> findProductById(Long id);
}
