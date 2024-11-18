package com.store.storemanagement.mapper;

import com.store.storemanagement.dto.ProductDTO;
import com.store.storemanagement.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO productToProductDTO(Product product);

    Product productDTOToProduct(ProductDTO productDTO);
}

