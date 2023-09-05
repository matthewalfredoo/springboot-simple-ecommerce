package com.learning.productservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private String category;

    private String brand;

    private String color;

    @Column(nullable = false)
    private String size;

    private String image;

}

// example of JSON for this class
// {
//     "id": 1,
//     "name": "Product 1",
//     "description": "Product 1 description",
//     "price": 100.0,
//     "category": "Category 1",
//     "brand": "Brand 1",
//     "color": "Color 1",
//     "size": "Size 1",
//     "image": "Image 1"
// }
