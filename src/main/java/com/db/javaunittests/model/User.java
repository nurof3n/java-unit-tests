package com.db.javaunittests.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne
    private Cart currentCart;

    @OneToOne
    private Wishlist wishlist;

    @OneToMany
    private List<Cart> orderHistory = new ArrayList<>();

    /**
     * Adds the given entry to the current cart.
     *
     * @param cartEntry the entry to add
     */
    public void addToCart(CartEntry cartEntry) {
        currentCart.addCartEntry(cartEntry);
    }

    /**
     * Removes the given entry from the current cart.
     *
     * @param cartEntry the entry to remove
     */
    public void removeFromCart(CartEntry cartEntry) {
        currentCart.removeCartEntry(cartEntry);
    }

    /**
     * Sets the current cart to null.
     */
    public void clearCart() {
        currentCart = null;
    }

    /**
     * Checks out the current cart and adds it to the order history.
     * This performs a validation that checks if the number of products ordered for each entry is at most equal to
     * the stock of the product.
     */
    public void checkout() {
        currentCart.checkout();
        orderHistory.add(currentCart);
        clearCart();
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
     * Sets the wishlist to null.
     */
    public void clearWishlist() {
        wishlist = null;
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
}
