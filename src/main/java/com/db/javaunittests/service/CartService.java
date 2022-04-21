package com.db.javaunittests.service;

import com.db.javaunittests.model.Cart;
import com.db.javaunittests.model.CartEntry;
import com.db.javaunittests.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    // basic CRUD operations

    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public Optional<Cart> findCartById(Long id) {
        return cartRepository.findById(id);
    }

    public List<Cart> findAllCarts() {
        return cartRepository.findAll();
    }

    public Cart updateCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public void deleteCartById(Long id) {
        cartRepository.deleteById(id);
    }

    public void deleteAllCarts() {
        cartRepository.deleteAll();
    }

    // custom operations

    /**
     * Adds an entry to the cart (product and quantity).
     *
     * @param id        the id of the cart
     * @param cartEntry the entry to be added
     * @return the updated cart
     */
    public Cart addCartEntry(Long id, CartEntry cartEntry) {
        Optional<Cart> cart = findCartById(id);
        if (cart.isPresent()) {
            cart.get().addCartEntry(cartEntry);
            return updateCart(cart.get());
        } else {
            throw new IllegalArgumentException("Cart with id " + id + " not found");
        }
    }

    /**
     * Removes an entry from the cart.
     *
     * @param id        the id of the cart
     * @param cartEntry the entry to be removed
     */
    public void removeCartEntry(Long id, CartEntry cartEntry) {
        Optional<Cart> cart = findCartById(id);
        if (cart.isPresent()) {
            cart.get().removeCartEntry(cartEntry);
            updateCart(cart.get());
        } else {
            throw new IllegalArgumentException("Cart with id " + id + " not found");
        }
    }

    /**
     * Removes all entries from the cart.
     *
     * @param id the id of the cart
     */
    public void clearCart(Long id) {
        Optional<Cart> cart = findCartById(id);
        if (cart.isPresent()) {
            cart.get().clear();
            updateCart(cart.get());
        } else {
            throw new IllegalArgumentException("Cart with id " + id + " not found");
        }
    }

    /**
     * @return a list of all the carts decreasingly ordered by the total quantity of products in the cart
     */
    public List<Cart> getCartsSortedByQuantity() {
        List<Cart> carts = findAllCarts();
        carts.sort(Comparator.comparingInt(Cart::getTotalQuantity).reversed());
        return carts;
    }
}
