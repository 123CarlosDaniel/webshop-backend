package com.backend.services.interfaces;

import com.backend.projections.ProductProjection;
import com.backend.request.ProductRequest;
import com.backend.models.entities.Product;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    List<ProductProjection> getAll(String category, String sort);

    Product create(ProductRequest productRequest) throws IOException;

    void delete(Long productId);

    ProductProjection get(Long productId);

    Product update(Long id, ProductRequest productRequest) throws IOException;

}
