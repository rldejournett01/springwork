package com.example.catalog_service;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // UUID or SKU #
    private String name;
    private String description;
    private BigDecimal price;
    private int stock; //inventory count

}
