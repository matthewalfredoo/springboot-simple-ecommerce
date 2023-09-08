package com.learning.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

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
