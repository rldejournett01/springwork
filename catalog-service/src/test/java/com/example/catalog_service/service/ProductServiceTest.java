package com.example.catalog_service.service;

import com.example.catalog_service.exception.ProductNotFoundException;
import com.example.catalog_service.model.Product;
import com.example.catalog_service.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ProductServiceTest {
    private ProductRepository repo;
    private ProductService service;

    @BeforeEach
    void setup() {
        repo = Mockito.mock(ProductRepository.class);
        service = new ProductService(repo);
    }

    @Test
    void getAllProducts_shouldReturnList() {
        Product product = new Product();
        product.setName("Test");
        product.setDescription("Description");
        product.setPrice(new BigDecimal("25.0"));
        product.setStock(50);

        when(repo.findAll()).thenReturn(List.of(product));

        List<Product> result = service.getAllProducts();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test");

        verify(repo, times(1)).findAll();
    }

    @Test
    void getProductById_whenExists_shouldReturnProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Foo");
        product.setDescription("Desc");
        product.setPrice(new BigDecimal("5.0"));
        product.setStock(2);

        when(repo.findById(1L)).thenReturn(Optional.of(product));

        Product result = service.getProductById(1L);
        assertThat(result.getName()).isEqualTo("Foo");
    }

    @Test
    void getProductById_whenNotExist_shouldThrow() {
        when(repo.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getProductById(2L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("2");
    }

    @Test
    void createProduct_shouldCallSave() {
        Product product = new Product();
        product.setName("New");
        product.setDescription("Desc");
        product.setPrice(new BigDecimal("3.0"));
        product.setStock(10);

        when(repo.save(any(Product.class))).thenReturn(product);

        Product created = service.createProduct(product);
        assertThat(created).isNotNull();
        verify(repo, times(1)).save(product);
    }

    @Test
    void updateProduct_whenExists_shouldUpdate() {
        Product existing = new Product();
        existing.setId(1L);
        existing.setName("Old");
        existing.setDescription("OldDesc");
        existing.setPrice(new BigDecimal("1.0"));
        existing.setStock(1);

        Product update = new Product();
        update.setName("NewName");
        update.setDescription("NewDesc");
        update.setPrice(new BigDecimal("2.0"));
        update.setStock(5);

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any(Product.class))).thenReturn(existing);

        Product updated = service.updateProduct(1L, update);
        assertThat(updated.getName()).isEqualTo("NewName");
        assertThat(updated.getPrice()).isEqualTo(new BigDecimal("2.0"));
    }

    @Test
    void updateProduct_whenNotExist_shouldThrow() {
        when(repo.findById(9L)).thenReturn(Optional.empty());
        Product dummy = new Product();
        dummy.setName("X");
        dummy.setDescription("Y");
        dummy.setPrice(new BigDecimal("0.5"));
        dummy.setStock(0);

        assertThatThrownBy(() -> service.updateProduct(9L, dummy))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void deleteProduct_whenExists_shouldDelete() {
        when(repo.existsById(1L)).thenReturn(true);
        // no exception
        service.deleteProduct(1L);
        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    void deleteProduct_whenNotExist_shouldThrow() {
        when(repo.existsById(5L)).thenReturn(false);
        assertThatThrownBy(() -> service.deleteProduct(5L))
                .isInstanceOf(ProductNotFoundException.class);
    }
}
