package com.learning.searchservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "products")
public class Product {

    @Id
    private Long id;

    @Field(type = FieldType.Text, name = "name")
    private String name;

    @Field(type = FieldType.Text, name = "description")
    private String description;

    @Field(type = FieldType.Double, name = "price")
    private Double price;

    @Field(type = FieldType.Integer, name = "stock")
    private int stock;

    @Field(type = FieldType.Text, name = "category")
    private String category;

    @Field(type = FieldType.Text, name = "brand")
    private String brand;

    @Field(type = FieldType.Text, name = "color")
    private String color;

    @Field(type = FieldType.Text, name = "size")
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
