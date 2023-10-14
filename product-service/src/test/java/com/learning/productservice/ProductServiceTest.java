package com.learning.productservice;

import com.learning.productservice.entity.Product;
import com.learning.productservice.repository.ProductRepository;
import com.learning.productservice.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.mockito.Mockito.when;

public class ProductServiceTest {

    @InjectMocks
    ProductServiceImpl productService;

    @Mock
    ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveProduct() {
        Product product = new Product();
        product.setName("Product 1");
        product.setDescription("Product 1 description");
        product.setPrice(100.0);
        product.setStock(10);
        product.setCategory("Category 1");
        product.setBrand("Brand 1");
        product.setColor("Color 1");
        product.setSize("Size 1");
        product.setImage("Image 1");

        when(productRepository.save(product)).thenReturn(product);

        Product savedProduct = productService.saveProduct(product);

        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getDescription(), savedProduct.getDescription());
        assertEquals(product.getPrice(), savedProduct.getPrice());
        assertEquals(product.getStock(), savedProduct.getStock());
        assertEquals(product.getCategory(), savedProduct.getCategory());
        assertEquals(product.getBrand(), savedProduct.getBrand());
        assertEquals(product.getColor(), savedProduct.getColor());
        assertEquals(product.getSize(), savedProduct.getSize());
        assertEquals(product.getImage(), savedProduct.getImage());
    }

    @Test
    public void testGetAllProducts() {
        List<Product> products = List.of(
                new Product(1L, "Product 1", "Product 1 description", 100.0, 10, "Category 1", "Brand 1", "Color 1", "Size 1", "Image 1"),
                new Product(2L, "Product 2", "Product 2 description", 200.0, 20, "Category 2", "Brand 2", "Color 2", "Size 2", "Image 2"),
                new Product(3L, "Product 3", "Product 3 description", 300.0, 30, "Category 3", "Brand 3", "Color 3", "Size 3", "Image 3")
        );

        when(productRepository.findAll()).thenReturn(products);

        List<Product> allProducts = productService.getAllProducts();

        assertEquals(products.size(), allProducts.size());
    }

    @Test
    public void testGetProductById() {
        Product product = new Product(1L, "Product 1", "Product 1 description", 100.0, 10, "Category 1", "Brand 1", "Color 1", "Size 1", "Image 1");

        when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(product));

        Product foundProduct = productService.getProductById(1L);

        assertEquals(product.getId(), foundProduct.getId());
        assertEquals(product.getName(), foundProduct.getName());
        assertEquals(product.getDescription(), foundProduct.getDescription());
        assertEquals(product.getPrice(), foundProduct.getPrice());
        assertEquals(product.getStock(), foundProduct.getStock());
        assertEquals(product.getCategory(), foundProduct.getCategory());
        assertEquals(product.getBrand(), foundProduct.getBrand());
        assertEquals(product.getColor(), foundProduct.getColor());
        assertEquals(product.getSize(), foundProduct.getSize());
        assertEquals(product.getImage(), foundProduct.getImage());
    }

    @Test
    public void testUpdateProduct() {
        Product product = new Product(1L, "Product 1", "Product 1 description", 100.0, 10, "Category 1", "Brand 1", "Color 1", "Size 1", "Image 1");
        Product updatedProduct = new Product(1L, "Product 1 updated", "Product 1 description updated", 100.0, 10, "Category 1", "Brand 1", "Color 1", "Size 1", "Image 1");

        when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(product));
        when(productRepository.save(product)).thenReturn(updatedProduct);

        Product savedProduct = productService.updateProduct(product);

        assertEquals(product.getId(), savedProduct.getId());
        assertNotSame(product.getName(), savedProduct.getName());
        assertNotSame(product.getDescription(), savedProduct.getDescription());
    }

    @Test
    public void testDeleteProduct() {
        List<Product> products = List.of(
                new Product(1L, "Product 1", "Product 1 description", 100.0, 10, "Category 1", "Brand 1", "Color 1", "Size 1", "Image 1"),
                new Product(2L, "Product 2", "Product 2 description", 200.0, 20, "Category 2", "Brand 2", "Color 2", "Size 2", "Image 2"),
                new Product(3L, "Product 3", "Product 3 description", 300.0, 30, "Category 3", "Brand 3", "Color 3", "Size 3", "Image 3")
        );

        when(productRepository.findAll()).thenReturn(products);
        when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(products.get(0)));

        productService.deleteProduct(1L);
    }
}
