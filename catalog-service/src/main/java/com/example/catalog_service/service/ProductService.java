package com.example.catalog_service.service;


import com.example.catalog_service.repository.ProductRepository;
import com.example.catalog_service.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public class ProductService {
    private final ProductRepository repo;

    public  ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    //Get all products
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    //Get a product by ID
    public Optional<Product> getProductById(Long id) {
        return repo.findById(id);
    }

    //Create a new product
    public Product createProduct(Product product) {
        return repo.save(product);
    }

    //Update an existing product
    public Optional<Product> updateProduct(Long id, Product updateProduct) {
        return repo.findById(id).map(existing -> {
            existing.setName(updateProduct.getName());
            existing.setDescription(updateProduct.getDescription());
            existing.setPrice(updateProduct.getPrice());
            existing.setStock(updateProduct.getStock());
            return repo.save(existing);
        });
    }

    //Delete a product
    public boolean deleteProduct(Long id){
        if (!repo.existsById(id)) {
            return false;
        }
        repo.deleteById(id);
        return true;
    }
}
