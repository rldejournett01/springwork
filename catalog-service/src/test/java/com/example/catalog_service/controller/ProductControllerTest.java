package com.example.catalog_service.controller;

import com.example.catalog_service.model.Product;
import com.example.catalog_service.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Test
    void getAllProducts_shouldReturn200AndJson() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test");
        product.setDescription("Desc");
        product.setPrice(new BigDecimal("10.0"));
        product.setStock(5);

        when(service.getAllProducts()).thenReturn(List.of(product));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Test"));
    }

    @Test
    void getProductById_whenFound_shouldReturn200Json() throws Exception {
        Product product = new Product();
        product.setId(2L);
        product.setName("ABC");
        product.setDescription("Something");
        product.setPrice(new BigDecimal("15.0"));
        product.setStock(2);

        when(service.getProductById(2L)).thenReturn(product);

        mockMvc.perform(get("/api/v1/products/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ABC"));
    }

    @Test
    void getProductById_whenNotFound_shouldReturn404() throws Exception {
        when(service.getProductById(99L)).thenThrow(new com.example.catalog_service.exception.ProductNotFoundException(99L));

        mockMvc.perform(get("/api/v1/products/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProduct_whenValidInput_shouldReturn201() throws Exception {
        Product product = new Product();
        product.setId(3L);
        product.setName("New");
        product.setDescription("Nice product");
        product.setPrice(new BigDecimal("20.0"));
        product.setStock(10);

        when(service.createProduct(any(Product.class))).thenReturn(product);

        String json = """
            {
              "name": "New",
              "description": "Nice product",
              "price": 20.0,
              "stock": 10
            }
        """;

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("New"));
    }

    @Test
    void updateProduct_whenExists_shouldReturn200() throws Exception {
        Product product = new Product();
        product.setId(4L);
        product.setName("Update");
        product.setDescription("DescUpd");
        product.setPrice(new BigDecimal("11.0"));
        product.setStock(1);

        when(service.updateProduct(Mockito.eq(4L), any(Product.class))).thenReturn(product);

        String json = """
            {
              "name": "Update",
              "description": "DescUpd",
              "price": 11.0,
              "stock": 1
            }
        """;

        mockMvc.perform(put("/api/v1/products/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Update"));
    }

    @Test
    void deleteProduct_whenExists_shouldReturn204() throws Exception {
        // service.deleteProduct just returns void or throws exception
        Mockito.doNothing().when(service).deleteProduct(5L);

        mockMvc.perform(delete("/api/v1/products/5"))
                .andExpect(status().isNoContent());
    }


}
