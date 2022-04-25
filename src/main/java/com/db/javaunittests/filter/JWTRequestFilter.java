package com.db.javaunittests.filter;

import com.db.javaunittests.exception.JWTException;
import com.db.javaunittests.model.User;
import com.db.javaunittests.service.JWTService;
import com.db.javaunittests.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // extract the email and JWT from the authorization header
        final String authorizationHeader = request.getHeader("Authorization");
        String email = null;
        String jwt = null;

        // check if the header exists and contains the bearer
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            email = jwtService.extractEmail(jwt);
        }

        // try to authenticate only if there is no user authenticated in the context
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional<User> user = this.userService.findUserByEmail(email);

            try {
                // check if email is registered
                if (user.isEmpty()) {
                    throw new UsernameNotFoundException("User with email " + email + " not found");
                }

                // validate the token and create authentication context
                if (jwtService.validateToken(jwt, user.get())) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(user, null, user.get().getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            } catch (JWTException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }

        chain.doFilter(request, response);
    }

}
