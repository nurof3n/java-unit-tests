package com.db.javaunittests.model;

import com.db.javaunittests.service.ProductService;
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

    private boolean checkedOut;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartEntry> cartEntries = new ArrayList<>();

    @ManyToOne
    private User user;

    /**
     * Adds an entry to the cart.
     *
     * @param cartEntry the entry to add
     */
    public void addCartEntry(CartEntry cartEntry) {
        cartEntries.add(cartEntry);
        cartEntry.setCart(this);
    }

    /**
     * Removes an entry from the cart.
     *
     * @param cartEntry the entry to remove
     */
    public void removeCartEntry(CartEntry cartEntry) {
        cartEntries.remove(cartEntry);
        cartEntry.setCart(null);
    }

    /**
     * Removes all the entries from the cart.
     */
    public void clear() {
        cartEntries.forEach(cartEntry -> cartEntry.setCart(null));
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
     *
     * @param productService the product service used to update the products' stocks
     */
    public void checkout(ProductService productService) {
        if (validateCheckout()) {
            cartEntries.forEach(cartEntry -> cartEntry.checkout(productService));
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
