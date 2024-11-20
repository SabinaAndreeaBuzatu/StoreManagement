package com.store.storemanagement.mapper;

import com.store.storemanagement.dto.OrderDTO;
import com.store.storemanagement.dto.OrderItemDTO;
import com.store.storemanagement.entity.Order;
import com.store.storemanagement.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderMapper {

    OrderDTO orderToOrderDTO(Order order);

    Order orderDTOToOrder(OrderDTO productDTO);

    @Mapping(target = "product.id", source = "productId")
    OrderItem orderItemDTOToOrderItem(OrderItemDTO orderItemDTO);

    @Mapping(target = "productId", source = "product.id")
    OrderItemDTO orderItemToOrderItemDTO(OrderItem orderItem);
}