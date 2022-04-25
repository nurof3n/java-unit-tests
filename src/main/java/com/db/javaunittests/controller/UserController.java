package com.db.javaunittests.controller;

import com.db.javaunittests.model.*;
import com.db.javaunittests.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller used for User CRUD operations on the users table.
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CartEntryService cartEntryService;
    @Autowired
    private ProductService productService;

    // basic CRUD operations

//    @PostMapping("/create")
//    public User createUser(@RequestBody User user) {
//        return userService.createUser(user);
//    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @GetMapping("/")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @PostMapping("/update")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @DeleteMapping("/delete")
    public void deleteAllUsers() {
        userService.deleteAllUsers();
    }

    // cart operations

    /**
     * Returns the cart of the user with the given id.
     *
     * @param id the id of the user
     * @return the cart of the user with the given id
     */
    @GetMapping("/{id}/cart")
    public Cart getCart(@PathVariable Long id) {
        return userService.getCart(id);
    }

    /**
     * Adds the given entry to the cart of the user with the given id.
     * If the user has no cart, it creates a new one and assigns it to the user.
     *
     * @param id          the id of the user
     * @param cartEntryId the id of the entry to add to the cart
     * @return user with the updated cart
     */
    @PostMapping("/{id}/cart/add")
    public User addToCart(@PathVariable Long id, @RequestParam Long cartEntryId) {
        if (getUserById(id).isEmpty()) {
            throw new IllegalArgumentException("User with id " + id + " does not exist.");
        }

        Optional<CartEntry> cartEntry = cartEntryService.findCartEntryById(cartEntryId);
        return cartEntry.map(entry -> userService.addToCart(id, entry)).orElseThrow(
                () -> new IllegalArgumentException("Cart entry with id " + cartEntryId + " does not exist."));
    }

    /**
     * Removes an entry from the cart of the user with the given id.
     *
     * @param id          the id of the user
     * @param cartEntryId the id of the entry to remove from the cart
     * @return user with the updated cart
     */
    @PostMapping("/{id}/cart/remove")
    public User removeFromCart(@PathVariable Long id, @RequestParam Long cartEntryId) {
        Optional<CartEntry> cartEntry = cartEntryService.findCartEntryById(cartEntryId);
        return cartEntry.map(entry -> userService.removeFromCart(id, entry)).orElseThrow(
                () -> new IllegalArgumentException("Cart entry with id " + cartEntryId + " does not exist."));
    }

    /**
     * Removes all entries from the cart of the user with the given id.
     *
     * @param id the id of the user
     * @return user with the updated cart
     */
    @PostMapping("/{id}/cart/clear")
    public User clearCart(@PathVariable Long id) {
        return userService.removeCart(id);
    }

    /**
     * Checks out the cart of the user with the given id.
     * Also adds it to his order history.
     *
     * @param id the id of the user
     * @return updated user
     */
    @PostMapping("/{id}/cart/checkout")
    public User checkout(@PathVariable Long id) {
        return userService.checkout(id);
    }

    // wishlist operations

    /**
     * Returns the wishlist of the user with the given id.
     *
     * @param id the id of the user
     * @return the wishlist of the user with the given id
     */
    @GetMapping("/{id}/wishlist")
    public Wishlist getWishlist(@PathVariable Long id) {
        return userService.getWishlist(id);
    }

    /**
     * Adds the given product to the wishlist of the user with the given id.
     * If the user has no wishlist, it creates a new one and assigns it to the user.
     *
     * @param id        the id of the user
     * @param productId the id of the product to add to the wishlist
     * @return user with the updated wishlist
     */
    @PostMapping("/{id}/wishlist/add")
    public User addToWishlist(@PathVariable Long id, @RequestParam Long productId) {
        if (getUserById(id).isEmpty()) {
            throw new IllegalArgumentException("User with id " + id + " does not exist.");
        }

        Optional<Product> product = productService.findProductById(productId);
        if (product.isEmpty()) {
            throw new IllegalArgumentException("Product with id " + productId + " does not exist.");
        } else {
            return userService.addToWishlist(id, product.get());
        }
    }

    /**
     * Removes a product from the wishlist of the user with the given id.
     *
     * @param id        the id of the user
     * @param productId the id of the product to remove from the wishlist
     * @return user with the updated wishlist
     */
    @PostMapping("/{id}/wishlist/remove")
    public User removeFromWishlist(@PathVariable Long id, @RequestParam Long productId) {
        Optional<Product> product = productService.findProductById(productId);
        return product.map(p -> userService.removeFromWishlist(id, p))
                .orElseThrow(() -> new IllegalArgumentException("Product with id " + productId + " does not exist."));
    }

    /**
     * Removes all products from the wishlist of the user with the given id.
     *
     * @param id the id of the user
     * @return user with the updated wishlist
     */
    @PostMapping("/{id}/wishlist/clear")
    public User clearWishlist(@PathVariable Long id) {
        return userService.removeWishlist(id);
    }

    /**
     * Returns the order history of the user with the given id.
     *
     * @param id the id of the user
     * @return the order history of the user with the given id
     */
    @GetMapping("/{id}/orderHistory")
    public List<Cart> getOrderHistory(@PathVariable Long id) {
        return userService.getOrderHistory(id);
    }
}
