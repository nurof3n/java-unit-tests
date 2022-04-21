package com.db.javaunittests.controller;

import com.db.javaunittests.model.Cart;
import com.db.javaunittests.model.CartEntry;
import com.db.javaunittests.model.Product;
import com.db.javaunittests.service.CartEntryService;
import com.db.javaunittests.service.CartService;
import com.db.javaunittests.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller user for CRUD operations on CartEntry
 */
@RestController
@RequestMapping("/cartentry")
public class CartEntryController {
    @Autowired
    private CartEntryService cartEntryService;
    @Autowired
    private ProductService productService;

    // basic CRUD operations

    /**
     * Creates a new CartEntry from given query parameters.
     * This is how I evade from needing to copy json object definitions for product and cart.
     *
     * @param quantity  quantity of product to add to cart
     * @param productId id of product
     */
    @PostMapping("/create")
    public void createCartEntry(@RequestParam Integer quantity, @RequestParam Long productId) {
        Optional<Product> product = productService.findProductById(productId);
        product.ifPresent(value -> cartEntryService.createCartEntry(quantity, value));
    }

    @GetMapping("/{id}")
    public Optional<CartEntry> getCartEntry(@PathVariable Long id) {
        return cartEntryService.findCartEntryById(id);
    }

    @GetMapping("/")
    public List<CartEntry> getAllCartEntries() {
        return cartEntryService.findAllCartEntries();
    }

    @PostMapping("/update")
    public void updateCartEntry(@RequestBody CartEntry cartEntry) {
        cartEntryService.updateCartEntry(cartEntry);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCartEntry(@PathVariable Long id) {
        cartEntryService.deleteCartEntryById(id);
    }

    @DeleteMapping("/delete")
    public void deleteAllCartEntries() {
        cartEntryService.deleteAllCartEntries();
    }
}
