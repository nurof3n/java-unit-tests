package com.db.javaunittests.repository;

import com.db.javaunittests.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
