package com.db.javaunittests.model;

import com.db.javaunittests.service.ProductService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Wishlist wishlist;

    private Boolean enabled;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private boolean credentialsNonExpired;
    @Transient
    private Collection<? extends GrantedAuthority> authorities = new ArrayList<>();

    /**
     * The order history contains all the carts that the users has purchased and, eventually, the last element is the
     * current cart (not yet checked out).
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart> orderHistory = new ArrayList<>();

    /**
     * Gets the last element of the order history, if it exists, and it is an unchecked out cart.
     *
     * @return the element, or null if it does not exist, or it is not an unchecked out cart
     */
    public Cart getUncheckedOutCart() {
        if (orderHistory.isEmpty() || orderHistory.get(orderHistory.size() - 1).isCheckedOut()) {
            return null;
        }

        return orderHistory.get(orderHistory.size() - 1);
    }

    /**
     * Assigns a current cart to the user as the last element in the order history.
     *
     * @param cart the cart to assign
     */
    public void assignCart(Cart cart) {
        Cart uncheckedOutCart = getUncheckedOutCart();
        if (uncheckedOutCart == null) {
            orderHistory.add(cart);
        } else {
            orderHistory.set(orderHistory.size() - 1, cart);
        }

        cart.setCheckedOut(false);  // just to make sure
        cart.setUser(this);
    }

    /**
     * Adds the given entry to the current cart.
     *
     * @param cartEntry the entry to add
     */
    public void addToCart(CartEntry cartEntry) {
        Cart currentCart = getUncheckedOutCart();
        if (currentCart == null) {
            throw new IllegalStateException("No unchecked out cart found");
        }

        currentCart.addCartEntry(cartEntry);
    }

    /**
     * Removes the given entry from the current cart.
     *
     * @param cartEntry the entry to remove
     */
    public void removeFromCart(CartEntry cartEntry) {
        Cart currentCart = getUncheckedOutCart();
        if (currentCart == null) {
            throw new IllegalStateException("No unchecked out cart found");
        }

        currentCart.removeCartEntry(cartEntry);
    }

    /**
     * Removes the user's cart.
     */
    public void removeCurrentCart() {
        Cart currentCart = getUncheckedOutCart();
        if (currentCart != null) {
            currentCart.setUser(null);
            orderHistory.remove(orderHistory.size() - 1);
        }
    }

    /**
     * Checks out the current cart and adds it to the order history.
     * This performs a validation that checks if the number of products ordered for each entry is at most equal to
     * the stock of the product.
     *
     * @param productService the product service used to update the products' stocks
     */
    public void checkout(ProductService productService) {
        Cart currentCart = getUncheckedOutCart();
        if (currentCart == null) {
            throw new IllegalStateException("No unchecked out cart found");
        }

        currentCart.checkout(productService);
        currentCart.setCheckedOut(true);
        removeCurrentCart();
    }

    /**
     * Assigns a wishlist to the user
     *
     * @param wishlist the wishlist to assign
     */
    public void assignWishlist(Wishlist wishlist) {
        this.wishlist = wishlist;
        this.wishlist.setUser(this);
    }

    /**
     * Adds the given product to the wishlist.
     *
     * @param product the product to add
     */
    public void addToWishlist(Product product) {
        wishlist.addProduct(product);
    }

    /**
     * Removes the given product from the wishlist.
     *
     * @param product the product to remove
     */
    public void removeFromWishlist(Product product) {
        wishlist.removeProduct(product);
    }

    /**
     * Removes the user's wishlist.
     */
    public void removeWishlist() {
        if (wishlist != null) {
            wishlist.setUser(null);
            wishlist = null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;   // the email is the username
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
