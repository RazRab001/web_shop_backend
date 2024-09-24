package project.onlineshop.domain.orderItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.onlineshop.domain.model.Item;
import project.onlineshop.domain.model.Order;
import project.onlineshop.domain.model.OrderItem;
import project.onlineshop.domain.repository.OrderItemRepository;
import project.onlineshop.domain.service.impl.OrderItemService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderItemServiceUnitTest {

    @Mock
    private OrderItemRepository repository;

    private OrderItemService orderItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        orderItemService = new OrderItemService(repository);
    }

    @Test
    void testCreateOrderItem() {
        // Mock data
        Order order = new Order();
        Item item = new Item();
        Integer count = 2;

        // Mock behavior
        when(repository.save(any(OrderItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Invoke method
        OrderItem result = orderItemService.create(order, item, count);

        // Verify interactions
        verify(repository).save(any(OrderItem.class));

        // Assertions
        assertNotNull(result);
        assertEquals(order, result.getOrder());
        assertEquals(item, result.getItem());
        assertEquals(count, result.getItemCount());
    }

    @Test
    void testDeleteAllItemsWithOrder() {
        // Mock data
        Long orderId = 1L;

        // Invoke method
        orderItemService.deleteAllItemsWithOrder(orderId);

        // Verify interactions
        verify(repository).deleteAll(repository.getOrderItemsByOrder_Id(orderId));
    }

    @Test
    void testDeleteAllItemsWithItem() {
        // Mock data
        Long itemId = 1L;

        // Invoke method
        orderItemService.deleteAllItemsWithItem(itemId);

        // Verify interactions
        verify(repository).deleteAll(repository.getOrderItemsByItem_Id(itemId));
    }
}

