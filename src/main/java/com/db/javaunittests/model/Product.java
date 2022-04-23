package com.db.javaunittests.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "products")
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stock;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartEntry> cartEntries;

    /**
     * Adds a new cart entry to the list of cart entries where the product appears.
     *
     * @param cartEntry the cart where the product appears
     */
    public void addCartEntry(CartEntry cartEntry) {
        cartEntries.add(cartEntry);
        cartEntry.setProduct(this);
    }

    /**
     * Removes a cart entry from the list of cart entries where the product appears.
     *
     * @param cartEntry the cart where the product appears
     */
    public void removeCartEntry(CartEntry cartEntry) {
        cartEntries.remove(cartEntry);
        cartEntry.setProduct(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Product product = (Product) o;
        return id != null && Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
