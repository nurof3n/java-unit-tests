package com.db.javaunittests.repository;

import com.db.javaunittests.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
