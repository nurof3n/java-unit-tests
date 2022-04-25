package com.db.javaunittests.controller;

import com.db.javaunittests.authentication.AuthenticationRequest;
import com.db.javaunittests.authentication.AuthenticationResponse;
import com.db.javaunittests.model.Cart;
import com.db.javaunittests.model.User;
import com.db.javaunittests.service.CartService;
import com.db.javaunittests.service.JWTService;
import com.db.javaunittests.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    // authentication with JWT operations

    /**
     * Authenticates the user with given credentials
     *
     * @param authenticationRequest contains the credentials (email and password)
     * @return an authentication response containing a JWT if successful
     */
    @PostMapping(value = "/auth")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        // authenticate the user with given credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()));
        // set authentication in the context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // find user by email
        Optional<User> user = userService.findUserByEmail(authenticationRequest.getUsername());
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User with email " + authenticationRequest.getUsername() + " not found");
        }

        // create a JWT and return it in the response
        String jwt = jwtService.createTokenFromUserDetails(user.get());
        return new AuthenticationResponse(jwt);
    }

    /**
     * Registers a new user. Checks if given email is available.
     *
     * @param authenticationRequest contains the credentials (email and password)
     * @return User created
     */
    @PostMapping(value = "/register")
    public User registerUser(@RequestBody AuthenticationRequest authenticationRequest) {
        // check if email is already registered
        if (userService.findUserByEmail(authenticationRequest.getUsername()).isPresent()) {
            throw new UsernameNotFoundException("User with email " + authenticationRequest.getUsername() + " not found");
        }

        // create and persist the user
        User user = new User();
        user.setName(authenticationRequest.getName());
        user.setEmail(authenticationRequest.getUsername());
        user.setEmail(authenticationRequest.getUsername());
        // encrypt the password
        user.setPassword(bCryptPasswordEncoder.encode(authenticationRequest.getPassword()));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        return userService.createUser(user);
    }

    // extra endpoints

    /**
     * GET mapping that returns the list of all the carts in the database, sorted by total quantity.
     * Uses authentication with JWT.
     */
    @GetMapping("/carts/sorted_by_total_quantity")
    public List<Cart> getCartsSortedByQuantity() {
        return cartService.getCartsSortedByQuantity();
    }

    /**
     * GET mapping that returns the list of all the users in the database, sorted by number of orders.
     * Uses authentication with JWT.
     */
    @GetMapping("/users/sorted_by_number_of_orders")
    public List<User> getUsersSortedByNumberOfOrders() {
        return userService.getUsersSortedByNumberOfOrders();
    }
}
