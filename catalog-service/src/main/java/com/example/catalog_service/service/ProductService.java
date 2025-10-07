package com.example.catalog_service.service;


import com.example.catalog_service.exception.ProductNotFoundException;
import com.example.catalog_service.repository.ProductRepository;
import com.example.catalog_service.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository repo;

    public  ProductService(ProductRepository repo) {
        this.repo = repo;
    }
//Service methods
    //Get all products
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    //Get a product by ID
    public Product getProductById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    //Create a new product
    public Product createProduct(Product product) {
        return repo.save(product);
    }

    //Update an existing product
    public Product updateProduct(Long id, Product updateProduct) {
        Product existing = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        existing.setName(updateProduct.getName());
        existing.setDescription(updateProduct.getDescription());
        existing.setPrice(updateProduct.getPrice());
        existing.setStock(updateProduct.getStock());
        return repo.save(existing);
    }

    //Delete a product
    public void deleteProduct(Long id){
        if (!repo.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        repo.deleteById(id);
    }
}
