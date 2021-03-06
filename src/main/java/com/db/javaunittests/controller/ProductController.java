package com.db.javaunittests.controller;

import com.db.javaunittests.model.Product;
import com.db.javaunittests.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller used for Product CRUD operations on the products table.
 */
@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    // basic CRUD operations

    @PostMapping("/create")
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id) {
        return productService.findProductById(id);
    }

    @GetMapping("/")
    public List<Product> getAllProducts() {
        return productService.findAllProducts();
    }

    @PostMapping("/update")
    public Product updateProduct(@RequestBody Product product) {
        return productService.updateProduct(product);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
    }

    @DeleteMapping("/delete")
    public void deleteAllProducts() {
        productService.deleteAllProducts();
    }
}
