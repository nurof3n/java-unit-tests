package com.db.javaunittests;

import com.db.javaunittests.controller.*;
import com.db.javaunittests.model.Cart;
import com.db.javaunittests.model.CartEntry;
import com.db.javaunittests.model.Product;
import com.db.javaunittests.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JavaUnitTestsApplicationTests {
    @Autowired
    private UserController userController;
    @Autowired
    private CartEntryController cartEntryController;
    @Autowired
    private MarketController marketController;
    @Autowired
    private ProductController productController;
    @Autowired
    private CartController cartController;
    @Autowired
    private WishlistController wishlistController;

    private User user;
    private Product product;
    private CartEntry cartEntry;

    @BeforeAll
    void populateDatabase() {
        user = new User();
        user.setName("nurof3n");
        user.setEmail("andy.cioc@yahoo.ro");
        user.setPassword("123456");
        product = new Product();
        product.setName("mamaliga");
        product.setPrice(10.0);
        product.setStock(2);
        cartEntry = new CartEntry();
        cartEntry.setQuantity(2);
        cartEntry.setProduct(product);
        user = userController.createUser(user);
        product = productController.createProduct(product);
        cartEntry = cartEntryController.createCartEntry(2, product.getId());
    }

    @Test
    @Order(1)
    void givenUser_ThenSaveCorrect() {
        Optional<User> userFound = userController.getUserById(user.getId());
        assert userFound.isPresent();
        assert userFound.get().getName().equals(user.getName());
        assert userFound.get().getEmail().equals(user.getEmail());
        assert userFound.get().getPassword().equals(user.getPassword());
        assert Objects.equals(userFound.get().getId(), user.getId());
        assert userFound.get().getWishlist() == null;
        assert userFound.get().getUncheckedOutCart() == null;
    }

    @Test
    @Order(2)
    void givenProduct_ThenSaveCorrect() {
        Optional<Product> productFound = productController.getProductById(product.getId());
        assert productFound.isPresent();
        assert productFound.get().getName().equals(product.getName());
        assert Objects.equals(productFound.get().getPrice(), product.getPrice());
        assert Objects.equals(productFound.get().getStock(), product.getStock());
        assert Objects.equals(productFound.get().getId(), product.getId());
    }

    @Test
    @Order(3)
    void givenCartEntry_ThenSaveCorrect() {
        Optional<CartEntry> cartEntryFound = cartEntryController.getCartEntryById(cartEntry.getId());
        assert cartEntryFound.isPresent();
        assert Objects.equals(cartEntryFound.get().getQuantity(), cartEntry.getQuantity());
        assert Objects.equals(cartEntryFound.get().getProduct(), cartEntry.getProduct());
        assert Objects.equals(cartEntryFound.get().getId(), cartEntry.getId());
    }

    @Test
    @Order(4)
    void whenAddToWishlist_ThenSaveCorrect() {
        user = userController.addToWishlist(user.getId(), product.getId());
        assert user.getWishlist() != null;
        assert user.getWishlist().getProducts().contains(product);
        assert user.getWishlist().getProducts().size() == 1;
    }

    @Test
    @Order(5)
    void whenAddToCart_ThenSaveCorrect() {
        user = userController.addToCart(user.getId(), cartEntry.getId());
        assert user.getUncheckedOutCart() != null;
        assert user.getOrderHistory().size() == 1;
        assert user.getUncheckedOutCart().getCartEntries().contains(cartEntry);
        assert user.getUncheckedOutCart().getCartEntries().size() == 1;
    }

    @Test
    @Order(6)
    void whenRemoveFromWishlist_ThenSaveCorrect() {
        user = userController.removeFromWishlist(user.getId(), product.getId());
        assert !user.getWishlist().getProducts().contains(product);
        assert user.getWishlist().getProducts().size() == 0;
    }

    @Test
    @Order(7)
    void whenCheckout_ThenCartIsEmptyAndOrderHistoryIsNotEmptyAndProductStockUpdated() {
        user = userController.checkout(user.getId());
        assert user.getUncheckedOutCart() == null;
        assert user.getOrderHistory().size() == 1;
        assert productController.getProductById(product.getId()).get().getStock() == 0;
    }

    @Test
    @Order(8)
    void whenAddThenRemoveFromCart_ThenCartIsEmpty() {
        // we need to create a new cart entry
        cartEntry = cartEntryController.createCartEntry(1, product.getId());
        user = userController.addToCart(user.getId(), cartEntry.getId());
        user = userController.removeFromCart(user.getId(), cartEntry.getId());
        assert user.getUncheckedOutCart().getCartEntries().size() == 0;
    }

    @Test
    @Order(9)
    void givenTwoUsers_WhenSortedByNumberOfOrders_ThenOrderCorrect() {
        user = new User();
        user.setName("nurof3n2");
        user.setEmail("andy.cioc@yahoo.ro");
        user.setPassword("123456");
        user = userController.createUser(user);
        List<User> users = marketController.getUsersSortedByNumberOfOrders();
        assert users.size() == 2;
        assert users.get(0).getOrderHistory().size() >= users.get(1).getOrderHistory().size();
    }

    @Test
    @Order(10)
    void givenTwoCarts_WhenSortedByQuantity_ThenOrderCorrect() {
        List<Cart> carts = marketController.getCartsSortedByQuantity();
        assert carts.size() == 2;
        assert carts.get(0).getTotalQuantity() >= carts.get(1).getTotalQuantity();
    }

    @Test
    @Order(11)
    void givenDatabase_WhenDeleteTables_ThenTablesAreEmpty() {
        userController.deleteAllUsers();
        productController.deleteAllProducts();
        assert userController.getAllUsers().size() == 0;
        assert productController.getAllProducts().size() == 0;
        assert cartEntryController.getAllCartEntries().size() == 0;
        assert cartController.getAllCarts().size() == 0;
        assert wishlistController.getAllWishlists().size() == 0;
    }
}
