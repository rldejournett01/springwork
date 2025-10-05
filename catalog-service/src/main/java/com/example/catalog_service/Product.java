package com.example.catalog_service;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Product {
    @Id
    private String id; // UUID or SKU #
    private String name;
    private String description;
    private double price;
    private int stock; //inventory count

}
