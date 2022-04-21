package com.db.javaunittests.service;

import com.db.javaunittests.model.CartEntry;
import com.db.javaunittests.model.Product;
import com.db.javaunittests.repository.CartEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartEntryService {
    @Autowired
    private CartEntryRepository cartEntryRepository;

    // basic CRUD operations

    /**
     * Creates a new cart entry for the given product and quantity.
     *
     * @param quantity the quantity of the product
     * @param product  the product
     */
    public void createCartEntry(Integer quantity, Product product) {
        CartEntry cartEntry = new CartEntry();
        cartEntry.setQuantity(quantity);
        cartEntry.setProduct(product);
        cartEntryRepository.save(cartEntry);
    }

    public Optional<CartEntry> findCartEntryById(Long id) {
        return cartEntryRepository.findById(id);
    }

    public List<CartEntry> findAllCartEntries() {
        return cartEntryRepository.findAll();
    }

    public void updateCartEntry(CartEntry cartEntry) {
        cartEntryRepository.save(cartEntry);
    }

    public void deleteCartEntryById(Long id) {
        cartEntryRepository.deleteById(id);
    }

    public void deleteAllCartEntries() {
        cartEntryRepository.deleteAll();
    }
}
