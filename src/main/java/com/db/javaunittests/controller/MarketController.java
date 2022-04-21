package com.db.javaunittests.controller;

import com.db.javaunittests.service.CartService;
import com.db.javaunittests.service.UserService;
import com.db.javaunittests.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MarketController {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private WishlistService wishlistService;
}
