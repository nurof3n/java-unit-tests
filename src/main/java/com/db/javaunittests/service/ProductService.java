package com.db.javaunittests.service;

import com.db.javaunittests.model.Product;
import com.db.javaunittests.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    // basic CRUD operations

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    public void deleteAllProducts() {
        productRepository.deleteAll();
    }
}
