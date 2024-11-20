package com.store.storemanagement.mapper;

import com.store.storemanagement.dto.ReviewDTO;
import com.store.storemanagement.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "product.id", target = "productId")
    ReviewDTO reviewToReviewDTO(Review review);

    @Mapping(source = "productId", target = "product.id")
    Review reviewDTOToReview(ReviewDTO reviewDTO);
}
