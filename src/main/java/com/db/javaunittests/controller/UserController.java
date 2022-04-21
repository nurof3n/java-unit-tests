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
    private CartService cartService;
    @Autowired
    private WishlistService wishlistService;
    @Autowired
    private CartEntryService cartEntryService;
    @Autowired
    private ProductService productService;

    // basic CRUD operations

    @PostMapping("/create")
    public void createUser(@RequestBody User user) {
        userService.createUser(user);
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @GetMapping("/")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @PostMapping("/update")
    public void updateUser(@RequestBody User user) {
        userService.updateUser(user);
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
     */
    @PostMapping("/{id}/cart/add")
    public void addToCart(@PathVariable Long id, @RequestParam Long cartEntryId) {
        if (getUserById(id).isEmpty()) {
            return;
        }

        if (getCart(id) == null) {
            Cart cart = new Cart();
            cartService.createCart(cart);
            userService.assignCart(id, cart);
        }

        Optional<CartEntry> cartEntry = cartEntryService.findCartEntryById(cartEntryId);
        cartEntry.ifPresent(entry -> userService.addToCart(id, entry));
    }

    /**
     * Removes an entry from the cart of the user with the given id.
     *
     * @param id          the id of the user
     * @param cartEntryId the id of the entry to remove from the cart
     */
    @PostMapping("/{id}/cart/remove")
    public void removeFromCart(@PathVariable Long id, @RequestParam Long cartEntryId) {
        Optional<CartEntry> cartEntry = cartEntryService.findCartEntryById(cartEntryId);
        cartEntry.ifPresent(entry -> userService.removeFromCart(id, entry));
    }

    /**
     * Removes all entries from the cart of the user with the given id.
     *
     * @param id the id of the user
     */
    @PostMapping("/{id}/cart/clear")
    public void clearCart(@PathVariable Long id) {
        userService.clearCart(id);
    }

    /**
     * Checks out the cart of the user with the given id.
     * Also adds it to his order history.
     *
     * @param id the id of the user
     */
    @PostMapping("/{id}/cart/checkout")
    public void checkout(@PathVariable Long id) {
        userService.checkout(id);
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
     */
    @PostMapping("/{id}/wishlist/add")
    public void addToWishlist(@PathVariable Long id, @RequestParam Long productId) {
        if (getWishlist(id) == null) {
            Wishlist wishlist = new Wishlist();
            wishlistService.createWishlist(wishlist);
            userService.assignWishlist(id, wishlist);
        }

        Optional<Product> product = productService.findProductById(productId);
        product.ifPresent(p -> userService.addToWishlist(id, p));
    }

    /**
     * Removes a product from the wishlist of the user with the given id.
     *
     * @param id        the id of the user
     * @param productId the id of the product to remove from the wishlist
     */
    @PostMapping("/{id}/wishlist/remove")
    public void removeFromWishlist(@PathVariable Long id, @RequestParam Long productId) {
        Optional<Product> product = productService.findProductById(productId);
        product.ifPresent(p -> userService.removeFromWishlist(id, p));
    }

    /**
     * Removes all products from the wishlist of the user with the given id.
     *
     * @param id the id of the user
     */
    @PostMapping("/{id}/wishlist/clear")
    public void clearWishlist(@PathVariable Long id) {
        userService.clearWishlist(id);
    }
}