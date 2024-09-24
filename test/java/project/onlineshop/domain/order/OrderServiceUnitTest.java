package project.onlineshop.domain.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.onlineshop.domain.model.*;
import project.onlineshop.domain.repository.OrderRepository;
import project.onlineshop.domain.service.impl.*;
import project.onlineshop.utils.exceptions.ApiRequestException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceUnitTest {

    @Mock
    private OrderRepository repository;
    @Mock
    private CartService cartService;
    @Mock
    private ItemService itemService;
    @Mock
    private UserService userService;
    @Mock
    private OrderItemService orderItemService;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        orderService = new OrderService(repository, cartService, itemService, userService, orderItemService);
    }

    @Test
    void testCreateOrder() {
        // Mock data
        UUID userId = UUID.randomUUID();
        User owner = new User();
        Address address = new Address();
        Cart cart = new Cart();
        cart.setOwner(owner);
        List<CartItem> items = new ArrayList<>();
        items.add(new CartItem(cart, new Item())); // Example data, replace with appropriate test data
        String password = "password123";

        // Mock behavior
        when(repository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderItemService.create(any(Order.class), any(Item.class), anyInt())).thenReturn(new OrderItem());

        // Invoke method
        Order result = orderService.create(owner, address, items, password);

        // Verify interactions
        verify(repository, times(2)).save(any(Order.class));
        verify(orderItemService, times(items.size())).create(any(Order.class), any(Item.class), anyInt());

        // Assertions
        assertNotNull(result);
        assertEquals(address, result.getAddress());
        assertEquals(password, result.getPassword());
        // Add more assertions as needed
    }

    @Test
    void testMakeOrderWithAddressAddingError() {
        // Mock data
        UUID userId = UUID.randomUUID();
        User user = new User();
        Address address = new Address();
        Cart cart = new Cart();
        cart.setOwner(user);

        // Mock behavior
        when(cartService.getCartByOwner(userId)).thenReturn(cart);
        when(userService.addAddress(userId, address)).thenReturn(null);

        // Invoke method and verify exception
        assertThrows(ApiRequestException.class, () -> orderService.makeOrder(userId, address));
    }

    @Test
    void testMakeOrderWithChangedCart() {
        // Mock data
        UUID userId = UUID.randomUUID();
        User user = new User();
        Address address = new Address();
        Cart cart = new Cart();
        cart.setOwner(user);
        List<CartItem> items = new ArrayList<>();
        items.add(new CartItem(cart, new Item())); // Example data, replace with appropriate test data

        // Mock behavior
        when(cartService.getCartByOwner(userId)).thenReturn(cart);
        when(userService.addAddress(userId, address)).thenReturn(address);
        when(itemService.checkItemsInCart(anyList())).thenReturn(items);

        // Invoke method and verify exception
        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> orderService.makeOrder(userId, address));

        // Assertions
        assertEquals("Cart was changed", exception.getMessage());
    }

    @Test
    void testGetOrderIdByPassword() {
        // Mock data
        String password = "password123";
        Order order = new Order();
        order.setId(1L);

        // Mock behavior
        when(repository.getOrderByPassword(password)).thenReturn(order);

        // Invoke method
        Long result = orderService.getOrderIdByPassword(password);

        // Assertions
        assertNotNull(result);
        assertEquals(order.getId(), result);
    }

    @Test
    void testGetOrderIdByPasswordWithNonexistentOrder() {
        // Mock data
        String password = "password123";

        // Mock behavior
        when(repository.getOrderByPassword(password)).thenReturn(null);

        // Invoke method and verify exception
        assertThrows(ApiRequestException.class, () -> orderService.getOrderIdByPassword(password));
    }

    @Test
    void testGetOrderById() {
        // Mock data
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        // Mock behavior
        when(repository.getOrderById(orderId)).thenReturn(order);

        // Invoke method
        Order result = orderService.getOrderById(orderId);

        // Assertions
        assertNotNull(result);
        assertEquals(order, result);
    }

    @Test
    void testGetOrderByIdWithNonexistentOrder() {
        // Mock data
        Long orderId = 1L;

        // Mock behavior
        when(repository.getOrderById(orderId)).thenReturn(null);

        // Invoke method and verify exception
        assertThrows(ApiRequestException.class, () -> orderService.getOrderById(orderId));
    }

    @Test
    void testDeleteOrder() {
        // Mock data
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        // Mock behavior
        when(repository.getOrderById(orderId)).thenReturn(order);

        // Invoke method
        assertDoesNotThrow(() -> orderService.deleteOrder(orderId));

        // Verify interactions
        verify(orderItemService).deleteAllItemsWithOrder(orderId);
        verify(repository).delete(order);
    }

    @Test
    void testDeleteNonexistentOrder() {
        // Mock data
        Long orderId = 1L;

        // Mock behavior
        when(repository.getOrderById(orderId)).thenReturn(null);

        // Invoke method and verify exception
        assertThrows(ApiRequestException.class, () -> orderService.deleteOrder(orderId));
    }

    @Test
    void testGetOrderSum() {
        // Mock data
        List<CartItem> items = new ArrayList<>();
        Item item1 = new Item();
        item1.setPrice(10.0f);
        Item item2 = new Item();
        Cart cart = new Cart();
        item2.setPrice(20.0f);
        items.add(new CartItem(cart, item1));
        items.add(new CartItem(cart, item2));

        // Invoke method
        BigDecimal result = orderService.getOrderSum(items);

        // Assertions
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(30.0), result);
    }

    @Test
    void testGetOrderSumWithEmptyItems() {
        // Mock data
        List        <CartItem> items = new ArrayList<>();

        // Invoke method and verify exception
        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> orderService.getOrderSum(items));

        // Assertions
        assertEquals("Your cart is empty", exception.getMessage());
    }
}






