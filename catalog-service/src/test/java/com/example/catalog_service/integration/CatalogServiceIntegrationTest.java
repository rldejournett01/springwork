package com.example.catalog_service.integration;

import com.example.catalog_service.model.Product;
import com.example.catalog_service.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
public class CatalogServiceIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository repo;

    @BeforeEach
    void setup() {
        repo.deleteAll();
        Product p1 = new Product();
        p1.setName("Prod1");
        p1.setDescription("Description 1");
        p1.setPrice(new BigDecimal("5.0"));
        p1.setStock(10);
        repo.save(p1);

        Product p2 = new Product();
        p2.setName("Prod2");
        p2.setDescription("Description 2");
        p2.setPrice(new BigDecimal("15.0"));
        p2.setStock(20);
        repo.save(p2);
    }

    @Test
    void getAllProducts_shouldReturnTwo() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getProductById_shouldReturnOne() throws Exception {
        // fetch one saved id
        Product product = repo.findAll().get(0);
        Long id = product.getId();

        mockMvc.perform(get("/api/v1/products/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(product.getName()));
    }

    @Test
    void createProduct_shouldAddOne() throws Exception {
        String json = """
            {
              "name": "NewProd",
              "description": "New Description",
              "price": 9.99,
              "stock": 5
            }
        """;

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("NewProd"));

        // Then all count = 3
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void updateProduct_shouldReflectChange() throws Exception {
        Product product = repo.findAll().get(0);
        Long id = product.getId();

        String json = """
            {
              "name": "UpdatedName",
              "description": "Updated Desc",
              "price": 20.0,
              "stock": 2
            }
        """;

        mockMvc.perform(put("/api/v1/products/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedName"));
    }

    @Test
    void deleteProduct_shouldRemoveOne() throws Exception {
        Product product = repo.findAll().get(0);
        Long id = product.getId();

        mockMvc.perform(delete("/api/v1/products/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/products/" + id))
                .andExpect(status().isNotFound());
    }
}
