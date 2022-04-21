package com.db.javaunittests.service;

import com.db.javaunittests.model.User;
import com.db.javaunittests.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void createUser(User user) {
        userRepository.save(user);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    /**
     * @return list of all the users in the database, sorted by total number of orders of the user.
     */
    public List<User> getUsersSortedByNumberOfOrders() {
        List<User> users = findAllUsers();
        users.sort(Comparator.comparingInt(u -> u.getOrderHistory().size()));
        return users;
    }
}
