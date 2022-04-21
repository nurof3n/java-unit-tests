package com.db.javaunittests.controller;

import com.db.javaunittests.model.Cart;
import com.db.javaunittests.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller used for Cart CRUD operations on the carts table.
 */
@RestController
@RequestMapping("/carts")
public class CartController {
    @Autowired
    private CartService cartService;

    // basic CRUD operations

    @PostMapping("/create")
    public Cart createCart() {
        return cartService.createCart(new Cart());
    }

    @GetMapping("/{id}")
    public Optional<Cart> getCartById(@PathVariable Long id) {
        return cartService.findCartById(id);
    }

    @GetMapping("/")
    public List<Cart> getAllCarts() {
        return cartService.findAllCarts();
    }

    @PostMapping("/update")
    public Cart updateCart(@RequestBody Cart cart) {
        return cartService.updateCart(cart);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCartById(@PathVariable Long id) {
        cartService.deleteCartById(id);
    }

    @DeleteMapping("/delete")
    public void deleteAllCarts() {
        cartService.deleteAllCarts();
    }
}
