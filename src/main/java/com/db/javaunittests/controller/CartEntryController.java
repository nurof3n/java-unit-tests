package com.db.javaunittests.controller;

import com.db.javaunittests.model.CartEntry;
import com.db.javaunittests.model.Product;
import com.db.javaunittests.service.CartEntryService;
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
     * @return CartEntry
     */
    @PostMapping("/create")
    public CartEntry createCartEntry(@RequestParam Integer quantity, @RequestParam Long productId) {
        Optional<Product> product = productService.findProductById(productId);
        if (product.isPresent()) {
            return cartEntryService.createCartEntry(quantity, product.get());
        } else {
            throw new IllegalArgumentException("Product with id " + productId + " does not exist");
        }
    }

    @GetMapping("/{id}")
    public Optional<CartEntry> getCartEntryById(@PathVariable Long id) {
        return cartEntryService.findCartEntryById(id);
    }

    @GetMapping("/")
    public List<CartEntry> getAllCartEntries() {
        return cartEntryService.findAllCartEntries();
    }

    @PostMapping("/update")
    public CartEntry updateCartEntry(@RequestBody CartEntry cartEntry) {
        return cartEntryService.updateCartEntry(cartEntry);
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
