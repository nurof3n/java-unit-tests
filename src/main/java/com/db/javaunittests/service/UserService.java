package com.db.javaunittests.service;

import com.db.javaunittests.model.*;
import com.db.javaunittests.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // basic CRUD operations

    public void createUser(User user) {
        userRepository.save(user);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    // custom operations

    /**
     * Assigns the given cart to the user.
     *
     * @param id   user id
     * @param cart cart to be assigned
     */
    public void assignCart(Long id, Cart cart) {
        Optional<User> user = findUserById(id);
        if (user.isPresent()) {
            user.get().setCurrentCart(cart);
            updateUser(user.get());
        }
    }

    /**
     * Gets the cart of the user
     *
     * @param id the id of the user
     * @return the cart of the user
     */
    public Cart getCart(Long id) {
        Optional<User> user = findUserById(id);
        return user.map(User::getCurrentCart).orElse(null);
    }

    /**
     * Adds an entry to the users' cart
     *
     * @param id        user id
     * @param cartEntry entry to be added
     */
    public void addToCart(Long id, CartEntry cartEntry) {
        Optional<User> user = findUserById(id);
        if (user.isPresent()) {
            user.get().addToCart(cartEntry);
            updateUser(user.get());
        }
    }

    /**
     * Removes an entry from the users' cart
     *
     * @param id        user id
     * @param cartEntry entry to be removed
     */
    public void removeFromCart(Long id, CartEntry cartEntry) {
        Optional<User> user = findUserById(id);
        if (user.isPresent()) {
            user.get().removeFromCart(cartEntry);
            updateUser(user.get());
        }
    }

    /**
     * Removes the cart of the user
     *
     * @param id user id
     */
    public void clearCart(Long id) {
        Optional<User> user = findUserById(id);
        if (user.isPresent()) {
            user.get().clearCart();
            updateUser(user.get());
        }
    }

    /**
     * Performs the checkout with the entries in the cart.
     * Adds the order to the history of the user.
     *
     * @param id user id
     */
    public void checkout(Long id) {
        Optional<User> user = findUserById(id);
        if (user.isPresent()) {
            user.get().checkout();
            updateUser(user.get());
        }
    }

    /**
     * Assigns the given wishlist to the user with given id.
     *
     * @param id       user id
     * @param wishlist wishlist to be assigned
     */
    public void assignWishlist(Long id, Wishlist wishlist) {
        Optional<User> user = findUserById(id);
        if (user.isPresent()) {
            user.get().setWishlist(wishlist);
            updateUser(user.get());
        }
    }

    /**
     * Gets the wishlist of the user
     *
     * @param id the id of the user
     * @return the wishlist of the user
     */
    public Wishlist getWishlist(Long id) {
        Optional<User> user = findUserById(id);
        return user.map(User::getWishlist).orElse(null);
    }

    /**
     * Adds a product to the users' wishlist
     *
     * @param id      user id
     * @param product product to be added
     */
    public void addToWishlist(Long id, Product product) {
        Optional<User> user = findUserById(id);
        if (user.isPresent()) {
            user.get().addToWishlist(product);
            updateUser(user.get());
        }
    }

    /**
     * Removes a product from the users' wishlist
     *
     * @param id      user id
     * @param product product to be removed
     */
    public void removeFromWishlist(Long id, Product product) {
        Optional<User> user = findUserById(id);
        if (user.isPresent()) {
            user.get().removeFromWishlist(product);
            updateUser(user.get());
        }
    }

    /**
     * Removes all the products from the users' wishlist
     *
     * @param id user id
     */
    public void clearWishlist(Long id) {
        Optional<User> user = findUserById(id);
        if (user.isPresent()) {
            user.get().clearWishlist();
            updateUser(user.get());
        }
    }

    /**
     * @return list of all the users in the database, sorted by total number of orders of the user.
     */
    public List<User> getUsersSortedByNumberOfOrders() {
        List<User> users = findAllUsers();
        users.sort(Comparator.comparingInt(u -> u.getOrderHistory().size()));
        return users;
    }
}
