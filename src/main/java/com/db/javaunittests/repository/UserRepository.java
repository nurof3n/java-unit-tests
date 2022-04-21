package com.db.javaunittests.repository;

import com.db.javaunittests.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
