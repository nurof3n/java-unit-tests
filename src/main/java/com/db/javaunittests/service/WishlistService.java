package com.db.javaunittests.service;

import com.db.javaunittests.model.Product;
import com.db.javaunittests.model.Wishlist;
import com.db.javaunittests.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistService {
    @Autowired
    private WishlistRepository wishlistRepository;

    // basic CRUD operations

    public void createWishlist(Wishlist wishlist) {
        wishlistRepository.save(wishlist);
    }

    public Optional<Wishlist> findWishlistById(Long id) {
        return wishlistRepository.findById(id);
    }

    public List<Wishlist> findAllWishlists() {
        return wishlistRepository.findAll();
    }

    public void updateWishlist(Wishlist wishlist) {
        wishlistRepository.save(wishlist);
    }

    public void deleteWishlistById(Long id) {
        wishlistRepository.deleteById(id);
    }

    public void deleteAllWishlists() {
        wishlistRepository.deleteAll();
    }

    // custom operations

    /**
     * Adds a product to a wishlist.
     *
     * @param id      the id of the wishlist
     * @param product the product to add
     */
    public void addToWishlist(Long id, Product product) {
        Optional<Wishlist> wishlist = findWishlistById(id);
        if (wishlist.isPresent()) {
            wishlist.get().addProduct(product);
            updateWishlist(wishlist.get());
        }
    }

    /**
     * Removes a product from a wishlist.
     *
     * @param id      the id of the wishlist
     * @param product the product to remove
     */
    public void removeFromWishlist(Long id, Product product) {
        Optional<Wishlist> wishlist = findWishlistById(id);
        if (wishlist.isPresent()) {
            wishlist.get().removeProduct(product);
            updateWishlist(wishlist.get());
        }
    }

    /**
     * Removes all the products from the wishlist.
     *
     * @param id the id of the wishlist
     */
    public void clearWishlist(Long id) {
        Optional<Wishlist> wishlist = findWishlistById(id);
        if (wishlist.isPresent()) {
            wishlist.get().clear();
            updateWishlist(wishlist.get());
        }
    }
}
