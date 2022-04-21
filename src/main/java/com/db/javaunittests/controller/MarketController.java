package com.db.javaunittests.controller;

import com.db.javaunittests.model.Cart;
import com.db.javaunittests.model.User;
import com.db.javaunittests.service.CartService;
import com.db.javaunittests.service.UserService;
import com.db.javaunittests.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller used for extra endpoints.
 */
@RestController
public class MarketController {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private WishlistService wishlistService;


    /**
     * GET mapping that returns the list of all the carts in the database, sorted by total quantity.
     */
    @GetMapping("/carts/sorted_by_total_quantity")
    public List<Cart> getCartsSortedByQuantity() {
        return cartService.getCartsSortedByQuantity();
    }

    /**
     * GET mapping that returns the list of all the users in the database, sorted by number of orders.
     */
    @GetMapping("/users/sorted_by_number_of_orders")
    public List<User> getUsersSortedByNumberOfOrders() {
        return userService.getUsersSortedByNumberOfOrders();
    }
}
