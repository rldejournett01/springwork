package com.example.catalog_service.controller;


import com.example.catalog_service.model.Product;
import com.example.catalog_service.repository.ProductRepository;
import com.example.catalog_service.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    //Get all products
    @GetMapping
    public List<Product> all() {
        return service.getAllProducts();
    }
    //Get one product by ID

    @GetMapping("/{id}")
    public ResponseEntity<Product> get(@PathVariable Long id) {
        Product product = service.getProductById(id);
        return ResponseEntity.ok(product);
    }
    //Create new product
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody @Valid Product product, BindingResult bindingResult) {
        Product created = service.createProduct(product);
        return new ResponseEntity<>(created,HttpStatus.CREATED);
    }

    //Update existing product by ID
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody @Valid Product product, BindingResult bindingResult) {
        Product updated = service.updateProduct(id, product);
        return  ResponseEntity.ok(updated);
    }
    //Delete product by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
