package com.store.storemanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_email", nullable = false)
    private User user;

    private String comment;

    private int rating;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}

