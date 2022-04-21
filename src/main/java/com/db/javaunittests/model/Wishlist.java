package com.db.javaunittests.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a wishlist as a list of products that the user wants to buy.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Wishlist {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    private List<Product> products = new ArrayList<>();

    /**
     * Adds a product to the wishlist.
     *
     * @param product the product to add
     */
    public void addProduct(Product product) {
        products.add(product);
    }

    /**
     * Removes a product from the wishlist.
     *
     * @param product the product to remove
     */
    public void removeProduct(Product product) {
        products.remove(product);
    }

    /**
     * Removes all the products from the wishlist.
     */
    public void clear() {
        products.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Wishlist wishlist = (Wishlist) o;
        return id != null && Objects.equals(id, wishlist.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
