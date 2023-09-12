package com.learning.cachingservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {

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

    public static final String KEY = "PRODUCT";

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
