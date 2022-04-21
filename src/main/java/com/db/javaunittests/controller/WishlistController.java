package com.db.javaunittests.controller;

import com.db.javaunittests.model.Wishlist;
import com.db.javaunittests.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller used for CRUD operations on the wishlists table.
 */
@RestController
@RequestMapping("/wishlists")
public class WishlistController {
    @Autowired
    private WishlistService wishlistService;

    // basic CRUD operations

    @PostMapping("/create")
    public void createWishlist(@RequestBody Wishlist wishlist) {
        wishlistService.createWishlist(wishlist);
    }

    @GetMapping("/{id}")
    public Optional<Wishlist> getWishlist(@PathVariable Long id) {
        return wishlistService.findWishlistById(id);
    }

    @GetMapping("/")
    public List<Wishlist> getAllWishlists() {
        return wishlistService.findAllWishlists();
    }

    @PostMapping("/update")
    public void updateWishlist(@RequestBody Wishlist wishlist) {
        wishlistService.updateWishlist(wishlist);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteWishlistById(@PathVariable Long id) {
        wishlistService.deleteWishlistById(id);
    }

    @DeleteMapping("/delete")
    public void deleteAllWishlists() {
        wishlistService.deleteAllWishlists();
    }
}
