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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderItemRepository orderItemRepository;

    private Order order;
    private OrderDTO orderDTO;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setEmail("email@email.com");

        Product product = new Product();
        product.setId(1L);
        product.setName("Product");
        product.setPrice(10.0);
        product.setStockQuantity(50);

        order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setProduct(product);
        orderItem.setQuantity(2);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        order.setItems(items);

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setProductId(1L);
        orderItemDTO.setQuantity(2);
        orderItemDTO.setPrice(10.0);

        orderDTO = new OrderDTO();
        orderDTO.setItems(List.of(orderItemDTO));
        orderDTO.setTotalAmount(20.0);

        when(userRepository.findById(user.getEmail())).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderMapper.orderDTOToOrder(orderDTO)).thenReturn(order);
        when(orderMapper.orderItemDTOToOrderItem(any())).thenReturn(orderItem);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.orderToOrderDTO(order)).thenReturn(orderDTO);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    }

    @Test
    void shouldCreateOrder() {
        OrderDTO result = orderService.createOrder(orderDTO, user.getEmail());

        assertNotNull(result);
        assertEquals(orderDTO.getTotalAmount(), result.getTotalAmount());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void shouldNotCreateOrderBecauseOfUserNotFound() {
        when(userRepository.findById(user.getEmail())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> orderService.createOrder(orderDTO, user.getEmail())
        );

        assertEquals("User not found with email: email@email.com", exception.getMessage());
    }

    @Test
    void shouldNotCreateOrderBecauseOfProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> orderService.createOrder(orderDTO, user.getEmail())
        );

        assertEquals("Product not found with ID: 1", exception.getMessage());
    }

    @Test
    void shouldGetOrdersByUserEmail() {
        when(orderRepository.findByUserEmail(user.getEmail())).thenReturn(List.of(order));

        List<OrderDTO> result = orderService.getOrdersByUserEmail(user.getEmail());

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findByUserEmail(user.getEmail());
    }

    @Test
    void shouldGetOrderById() {
        OrderDTO result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(orderDTO.getTotalAmount(), result.getTotalAmount());
    }

    @Test
    void shouldNotGetOrderByIdBecauseOfOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        OrderNotFoundException exception = assertThrows(
                OrderNotFoundException.class,
                () -> orderService.getOrderById(1L)
        );

        assertEquals("Order with ID 1 not found", exception.getMessage());
    }

    @Test
    void shouldUpdateOrderStatus() {
        orderService.updateOrderStatus(1L, OrderStatus.PROCESSING);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void shouldNotUpdateOrderStatus() {
        InvalidOrderStatusTransitionException exception = assertThrows(
                InvalidOrderStatusTransitionException.class,
                () -> orderService.updateOrderStatus(1L, OrderStatus.COMPLETED)
        );

        assertTrue(exception.getMessage().contains("Invalid transition"));
    }

    @Test
    void shouldGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<OrderDTO> result = orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
