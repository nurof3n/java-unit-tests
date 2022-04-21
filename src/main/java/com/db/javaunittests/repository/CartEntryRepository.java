package com.db.javaunittests.repository;

import com.db.javaunittests.model.CartEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartEntryRepository extends JpaRepository<CartEntry, Long> {
}
