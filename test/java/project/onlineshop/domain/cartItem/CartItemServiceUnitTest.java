package project.onlineshop.domain.cartItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.onlineshop.domain.model.Cart;
import project.onlineshop.domain.model.CartItem;
import project.onlineshop.domain.model.Item;
import project.onlineshop.domain.repository.CartItemRepository;
import project.onlineshop.domain.service.impl.CartItemService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartItemServiceUnitTest {

    @Mock
    private CartItemRepository repository;

    private CartItemService cartItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        cartItemService = new CartItemService(repository);
    }

    @Test
    void testCreateCartItem() {
        // Mock data
        Cart cart = new Cart();
        Item item = new Item();

        // Mock behavior
        when(repository.save(any(CartItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Invoke method
        CartItem result = cartItemService.create(cart, item);

        // Verify interactions
        verify(repository).save(any(CartItem.class));

        // Assertions
        assertNotNull(result);
        assertEquals(cart, result.getCart());
        assertEquals(item, result.getItem());
    }

    @Test
    void testDeleteCartItem() {
        // Mock data
        Long cartId = 1L;
        Long itemId = 2L;
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);

        // Mock behavior
        when(repository.getCartItemByCart_IdAndItem_Id(cartId, itemId)).thenReturn(cartItem);

        // Invoke method
        cartItemService.delete(cartId, itemId);

        // Verify interactions
        verify(repository).getCartItemByCart_IdAndItem_Id(cartId, itemId);
        verify(repository).delete(cartItem);
    }

    @Test
    void testDeleteByItemId() {
        // Mock data
        Long itemId = 1L;

        // Invoke method
        cartItemService.deleteByItemId(itemId);

        // Verify interactions
        verify(repository).deleteAll(repository.getCartItemsByItem_Id(itemId));
    }
}

