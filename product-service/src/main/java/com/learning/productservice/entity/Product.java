package com.learning.productservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {

    @Transient
    public static final String SEQUENCE_NAME = "products_sequence";

    @Id
    private Long id;

    private String name;

    private String description;

    private Double price;

    private int stock;

    private String category;

    private String brand;

    private String color;

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
