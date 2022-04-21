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
@Table(name = "carts")
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    private List<CartEntry> cartEntries = new ArrayList<>();

    /**
     * Adds an entry to the cart.
     *
     * @param cartEntry the entry to add
     */
    public void addCartEntry(CartEntry cartEntry) {
        cartEntries.add(cartEntry);
    }

    /**
     * Removes an entry from the cart.
     *
     * @param cartEntry the entry to remove
     */
    public void removeCartEntry(CartEntry cartEntry) {
        cartEntries.remove(cartEntry);
    }

    /**
     * Removes all the entries from the cart.
     */
    public void clear() {
        cartEntries.clear();
    }

    /**
     * @return true if the checkout is valid, i.e., the number of products ordered is at most equal to the product stock
     * for each cart entry
     */
    public boolean validateCheckout() {
        return cartEntries.stream().allMatch(CartEntry::validateCheckout);
    }

    /**
     * Checks out each cart entry. This also validates that the checkout is valid, i.e., the number of products ordered
     * is at most equal to the product stock.
     */
    public void checkout() {
        if (validateCheckout()) {
            cartEntries.forEach(CartEntry::checkout);
        }
    }

    /**
     * @return the total quantity of all products in the cart
     */
    public Integer getTotalQuantity() {
        return cartEntries.stream().mapToInt(CartEntry::getQuantity).sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Cart cart = (Cart) o;
        return id != null && Objects.equals(id, cart.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
