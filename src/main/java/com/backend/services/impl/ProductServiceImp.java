package com.backend.services.impl;

import com.backend.exception.CustomExceptions.NotFoundException;
import com.backend.models.entities.Category;
import com.backend.projections.ProductProjection;
import com.backend.repository.CategoryRepository;
import com.backend.request.ProductRequest;
import com.backend.models.entities.File;
import com.backend.models.entities.Product;
import com.backend.repository.ProductRepository;
import com.backend.services.interfaces.FileService;
import com.backend.services.interfaces.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FileService fileService;

    @Override
    public List<ProductProjection> getAll(String category, String sort) {

        Sort sortParam = Sort.unsorted();
        if (sort.equalsIgnoreCase("asc")) {
            sortParam = Sort.by(Sort.Order.asc("price"));
        }
        else if (sort.equalsIgnoreCase("desc")){
            sortParam = Sort.by(Sort.Order.desc("price"));
        }

        if (category.equalsIgnoreCase("all")) {
            return productRepository.findBy(sortParam);
        }

        Optional<Category> category1 = categoryRepository.findByName(category.toLowerCase());
        if (category1.isEmpty()) throw new NotFoundException("Category not found", HttpStatus.NOT_FOUND);

        return productRepository.findByCategory(category1.get(), sortParam);
    }

    @Override
    public Product create(ProductRequest productRequest) throws IOException {
        File file = fileService.store(productRequest.getImage());
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/v1/file-manager/files/")
                .path(file.getId().toString())
                .toUriString();

        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .imageUrl(fileDownloadUri)
                .image(file)
                .build();

        Optional<Category> categoryInDb = categoryRepository.findByName(productRequest.getCategory().toLowerCase());
        if (categoryInDb.isEmpty()) {
            product.setCategory(Category.builder().name(productRequest.getCategory().toLowerCase()).build());
        }
        else {
            product.setCategory(categoryInDb.get());
        }

        return productRepository.save(product);
    }

    @Override
    public void delete(Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public ProductProjection get(Long productId) {
        return productRepository.findProductById(productId).orElseThrow(() -> new NotFoundException("Product not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public Product update(Long id, ProductRequest productRequest) throws IOException {

        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new NotFoundException("Product not found", HttpStatus.NOT_FOUND);
        }
        var productFound = product.get();
        if (Objects.nonNull(productRequest.getImage())) {
            File file = fileService.store(productRequest.getImage());
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("api/v1/file-manager/files/")
                    .path(file.getId().toString())
                    .toUriString();
            productFound.setImage(file);
            productFound.setImageUrl(fileDownloadUri);
        }
        productFound.setName(productRequest.getName());
        productFound.setDescription(productRequest.getDescription());
        productFound.setPrice(productRequest.getPrice());

        Optional<Category> categoryInDb = categoryRepository.findByName(productRequest.getCategory().toLowerCase());
        if (categoryInDb.isEmpty()) {
            productFound.setCategory(Category.builder().name(productRequest.getCategory().toLowerCase()).build());
        }
        else {
            productFound.setCategory(categoryInDb.get());
        }
        return productRepository.save(productFound);
    }

}
