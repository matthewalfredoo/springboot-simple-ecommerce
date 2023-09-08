package com.learning.searchservice.mapper;

import com.learning.searchservice.dto.ProductDto;
import com.learning.searchservice.entity.Product;

public class ProductMapper {

    public static ProductDto toProductDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory(),
                product.getBrand(),
                product.getColor(),
                product.getSize(),
                product.getImage()
        );
    }

    public static Product toProduct(ProductDto productDto) {
        return new Product(
                productDto.getId(),
                productDto.getName(),
                productDto.getDescription(),
                productDto.getPrice(),
                productDto.getStock(),
                productDto.getCategory(),
                productDto.getBrand(),
                productDto.getColor(),
                productDto.getSize(),
                productDto.getImage()
        );
    }

}
