package com.db.javaunittests.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Wishlist {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    private List<Product> productList;
}
