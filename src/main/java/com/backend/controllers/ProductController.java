package com.backend.controllers;

import com.backend.models.entities.Product;
import com.backend.projections.ProductProjection;
import com.backend.request.ProductRequest;
import com.backend.services.interfaces.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductProjection>> getProductsList(@RequestParam(name = "category", defaultValue = "all") String category,
                                                                   @RequestParam(name = "sort", defaultValue= "none") String sort) {
        return ResponseEntity.ok(productService.getAll(category, sort));
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Product> createProduct(@RequestParam(name = "name") String name,
                                                 @RequestParam(name = "description") String description,
                                                 @RequestParam(name = "price") BigDecimal price,
                                                 @RequestParam(name = "category") String category,
                                                 @RequestPart(name = "image") MultipartFile image) throws IOException {
        ProductRequest productRequest = ProductRequest
                .builder()
                .name(name)
                .price(price)
                .category(category)
                .image(image)
                .description(description)
                .build();
        return new ResponseEntity<>(productService.create(productRequest), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Product> updateProduct(@PathVariable(name = "id") Long id, @RequestParam(name = "name") String name,
                                                 @RequestParam(name = "description") String description,
                                                 @RequestParam(name = "price") BigDecimal price,
                                                 @RequestParam(name = "category") String category,
                                                 @RequestPart(name = "image", required = false) MultipartFile image) throws IOException {

        ProductRequest productRequest = ProductRequest
                .builder()
                .name(name)
                .price(price)
                .category(category)
                .image(image)
                .description(description)
                .build();
        return new ResponseEntity<>(productService.update(id, productRequest), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") Long productId) {
        ProductProjection product = productService.get(productId);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long productId) {
        productService.delete(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
