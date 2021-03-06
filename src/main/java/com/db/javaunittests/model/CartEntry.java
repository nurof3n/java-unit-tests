package com.db.javaunittests.model;

import com.db.javaunittests.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

/**
 * Represents an entry in a cart as a product with a quantity of that product.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CartEntry {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Cart cart;

    /**
     * @return true if the quantity ordered is at most equal to the product's stock
     */
    public boolean validateCheckout() {
        return quantity <= product.getStock();
    }

    /**
     * Checks out the order, i.e., reduces the stock of the product by the quantity ordered.
     *
     * @param productService the product service used to update the product's stock
     */
    public void checkout(ProductService productService) {
        product.setStock(product.getStock() - quantity);
        product = productService.updateProduct(product);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        CartEntry order = (CartEntry) o;
        return id != null && Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
