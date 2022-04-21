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
    @Autowired
    private CartService cartService;
    @Autowired
    private WishlistService wishlistService;

    // basic CRUD operations

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(User user) {
        return userRepository.save(user);
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
     * @return updated user
     */
    public User assignCart(Long id, Cart cart) {
        Optional<User> user = findUserById(id);
        if (user.isPresent()) {
            user.get().setCurrentCart(cart);
            return updateUser(user.get());
        } else {
            throw new IllegalArgumentException("User with id " + id + " does not exist");
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
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User with id " + id + " does not exist");
        }
        return user.get().getCurrentCart();
    }

    /**
     * Adds an entry to the users' cart
     *
     * @param id        user id
     * @param cartEntry entry to be added
     * @return the user with the cart entry added
     */
    public User addToCart(Long id, CartEntry cartEntry) {
        Optional<User> user = findUserById(id);
        if (user.isPresent()) {
            user.get().addToCart(cartEntry);
            user.get().setCurrentCart(cartService.updateCart(user.get().getCurrentCart()));
            return updateUser(user.get());
        } else {
            throw new IllegalArgumentException("User with id " + id + " does not exist");
        }
    }

    /**
     * Removes an entry from the users' cart
     *
     * @param id        user id
     * @param cartEntry entry to be removed
     * @return updated user
     */
    public User removeFromCart(Long id, CartEntry cartEntry) {
        Optional<User> user = findUserById(id);
        if (user.isPresent()) {
            user.get().removeFromCart(cartEntry);
            user.get().setCurrentCart(cartService.updateCart(user.get().getCurrentCart()));
            return updateUser(user.get());
        } else {
            throw new IllegalArgumentException("User with id " + id + " does not exist");
        }
    }

    /**
     * Removes the cart of the user
     *
     * @param id user id
     * @return updated user
     */
    public User clearCart(Long id) {
        Optional<User> user = findUserById(id);
        if (user.isPresent()) {
            user.get().clearCart();
            return updateUser(user.get());
        } else {
            throw new IllegalArgumentException("User with id " + id + " does not exist");
        }
    }

    /**
     * Performs the checkout with the entries in the cart.
     * Adds the order to the history of the user.
     *
     * @param id user id
     * @return updated user
     */
    public User checkout(Long id) {
        Optional<User> user = findUserById(id);
        if (user.isPresent()) {
            user.get().checkout();
            return updateUser(user.get());
        } else {
            throw new IllegalArgumentException("Checkout failed.");
        }
    }

    /**
     * Assigns the given wishlist to the user with given id.
     *
     * @param id       user id
     * @param wishlist wishlist to be assigned
     * @return the user with the wishlist assigned
     */
    public User assignWishlist(Long id, Wishlist wishlist) {
        Optional<User> user = findUserById(id);
        if (user.isPresent()) {
            user.get().setWishlist(wishlist);
            return updateUser(user.get());
        } else {
            throw new IllegalArgumentException("User with id " + id + " does not exist");
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
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User with id " + id + " does not exist");
        }
        return user.get().getWishlist();
    }

    /**
     * Adds a product to the users' wishlist
     *
     * @param id      user id
     * @param product product to be added
     * @return the user with the product added to the wishlist
     */
    public User addToWishlist(Long id, Product product) {
        Optional<User> user = findUserById(id);
        if (user.isPresent()) {
            user.get().addToWishlist(product);
            user.get().setWishlist(wishlistService.updateWishlist(user.get().getWishlist()));
            return updateUser(user.get());
        } else {
            throw new IllegalArgumentException("User with id " + id + " does not exist");
        }
    }

    /**
     * Removes a product from the users' wishlist
     *
     * @param id      user id
     * @param product product to be removed
     * @return updated user
     */
    public User removeFromWishlist(Long id, Product product) {
        Optional<User> user = findUserById(id);
        if (user.isPresent()) {
            user.get().removeFromWishlist(product);
            user.get().setWishlist(wishlistService.updateWishlist(user.get().getWishlist()));
            return updateUser(user.get());
        } else {
            throw new IllegalArgumentException("User with id " + id + " does not exist");
        }
    }

    /**
     * Removes all the products from the users' wishlist
     *
     * @param id user id
     * @return updated user
     */
    public User clearWishlist(Long id) {
        Optional<User> user = findUserById(id);
        if (user.isPresent()) {
            user.get().clearWishlist();
            return updateUser(user.get());
        } else {
            throw new IllegalArgumentException("User with id " + id + " does not exist");
        }
    }

    /**
     * @return list of all the users in the database, sorted by total number of orders of the user.
     */
    public List<User> getUsersSortedByNumberOfOrders() {
        List<User> users = findAllUsers();
        users.sort(Comparator.comparingInt((User u) -> u.getOrderHistory().size()).reversed());
        return users;
    }

    /**
     * Returns the order history of the user with given id.
     * If the user does not exist, returns null
     *
     * @param id user id
     * @return order history of the user
     */
    public List<Cart> getOrderHistory(Long id) {
        Optional<User> user = findUserById(id);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User with id " + id + " does not exist");
        }
        return user.get().getOrderHistory();
    }
}
