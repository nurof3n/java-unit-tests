package com.db.javaunittests.service;

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

    public Wishlist createWishlist(Wishlist wishlist) {
        return wishlistRepository.save(wishlist);
    }

    public Optional<Wishlist> findWishlistById(Long id) {
        return wishlistRepository.findById(id);
    }

    public List<Wishlist> findAllWishlists() {
        return wishlistRepository.findAll();
    }

    public Wishlist updateWishlist(Wishlist wishlist) {
        return wishlistRepository.save(wishlist);
    }

    public void deleteWishlistById(Long id) {
        wishlistRepository.deleteById(id);
    }

    public void deleteAllWishlists() {
        wishlistRepository.deleteAll();
    }
}
