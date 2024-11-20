package com.store.storemanagement.service;

import com.store.storemanagement.dto.OrderDTO;
import com.store.storemanagement.dto.OrderItemDTO;
import com.store.storemanagement.entity.*;
import com.store.storemanagement.exception.InvalidOrderStatusTransitionException;
import com.store.storemanagement.exception.OrderNotFoundException;
import com.store.storemanagement.exception.ProductNotFoundException;
import com.store.storemanagement.exception.UserNotFoundException;
import com.store.storemanagement.mapper.OrderMapper;
import com.store.storemanagement.repository.OrderItemRepository;
import com.store.storemanagement.repository.OrderRepository;
import com.store.storemanagement.repository.ProductRepository;
import com.store.storemanagement.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class OrderService {

    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final OrderMapper orderMapper;
    @Autowired
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO, String email) {
        User user = userRepository.findById(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        Order order = orderMapper.orderDTOToOrder(orderDTO);
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        for (OrderItemDTO itemDTO : orderDTO.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + itemDTO.getProductId()));

            if (product.getStockQuantity() < itemDTO.getQuantity()) {
                throw new ProductNotFoundException("Insufficient stock for product: " + product.getName());
            }
        }

        double total = orderDTO.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        order.setTotalAmount(total);
        Order savedOrder = orderRepository.save(order);
        List<OrderItem> savedItems = saveOrderItems(orderDTO.getItems(), savedOrder);
        savedOrder.setItems(savedItems);
        return orderMapper.orderToOrderDTO(savedOrder);
    }


    private List<OrderItem> saveOrderItems(List<OrderItemDTO> itemDTOs, Order savedOrder) {
        return itemDTOs.stream()
                .map(itemDTO -> {
                    OrderItem orderItem = orderMapper.orderItemDTOToOrderItem(itemDTO);
                    Product product = getProductById(itemDTO.getProductId());
                    orderItem.setProduct(product);
                    orderItem.setOrder(savedOrder);

                    product.setStockQuantity(product.getStockQuantity() - itemDTO.getQuantity());
                    productRepository.save(product);
                    orderItemRepository.save(orderItem);

                    return orderItem;
                })
                .collect(Collectors.toList());
    }


    public List<OrderDTO> getOrdersByUserEmail(String userEmail) {
        List<Order> orders = orderRepository.findByUserEmail(userEmail);

        return orders.stream()
                .map(orderMapper::orderToOrderDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO updateOrderStatus(Long orderId, OrderStatus newStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        validateStatusTransition(order.getStatus(), newStatus);

        order.setStatus(newStatus);

        if (newStatus == OrderStatus.CANCELLED) {
            restoreStock(order);
        }

        Order updatedOrder = orderRepository.save(order);
        return orderMapper.orderToOrderDTO(updatedOrder);

    }

    public OrderDTO getOrderById(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            return orderMapper.orderToOrderDTO(optionalOrder.get());
        } else {
            throw new OrderNotFoundException("Order with ID " + orderId + " not found");
        }
    }

    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(orderMapper::orderToOrderDTO)
                .collect(Collectors.toList());
    }

    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
    }

    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        switch (currentStatus) {
            case PENDING -> {
                if (newStatus != OrderStatus.PROCESSING && newStatus != OrderStatus.CANCELLED) {
                    throw new InvalidOrderStatusTransitionException(OrderStatus.PENDING.name(), newStatus.name());
                }
            }
            case PROCESSING -> {
                if (newStatus != OrderStatus.SHIPPED && newStatus != OrderStatus.CANCELLED) {
                    throw new InvalidOrderStatusTransitionException(OrderStatus.PROCESSING.name(), newStatus.name());
                }
            }
            case SHIPPED -> {
                if (newStatus != OrderStatus.COMPLETED) {
                    throw new InvalidOrderStatusTransitionException(OrderStatus.SHIPPED.name(), newStatus.name());
                }
            }
            case COMPLETED, CANCELLED -> {
                throw new InvalidOrderStatusTransitionException(OrderStatus.COMPLETED.name() + "/" + OrderStatus.CANCELLED.name(), newStatus.name());
            }
        }
    }

    private void restoreStock(Order order) {
        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + item.getProduct().getId()));

            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }
    }

}
