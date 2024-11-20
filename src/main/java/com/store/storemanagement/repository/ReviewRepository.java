package com.store.storemanagement.repository;

import com.store.storemanagement.entity.Product;
import com.store.storemanagement.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProduct(Product product);
}
