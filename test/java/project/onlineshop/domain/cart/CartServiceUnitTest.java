package project.onlineshop.domain.cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import project.onlineshop.domain.model.*;
import project.onlineshop.domain.repository.CartRepository;
import project.onlineshop.domain.service.impl.CartItemService;
import project.onlineshop.domain.service.impl.CartService;
import project.onlineshop.domain.service.impl.ItemService;
import project.onlineshop.utils.exceptions.ApiRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceUnitTest {

    @Mock
    private CartRepository repository;

    @Mock
    private ItemService itemService;

    @Mock
    private CartItemService cartItemService;

    @InjectMocks
    private CartService service;

    @Test
    void testCreateCart() {
        // Arrange
        User user = new User();
        Cart savedCart = new Cart();
        savedCart.setOwner(user);

        // Mocking repository behavior
        when(repository.save(any())).thenReturn(savedCart);

        // Act
        Cart result = service.create(user);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getOwner());
        assertEquals(user, result.getOwner());
        verify(repository, times(1)).save(any());
    }

    @Test
    void testDeleteCart() {
        // Arrange
        UUID ownerId = UUID.randomUUID();
        Cart cart = new Cart();
        when(repository.getCartByOwner_Id(ownerId)).thenReturn(cart);

        // Act
        service.deleteCart(ownerId);

        // Assert
        verify(repository, times(1)).delete(cart);
    }

    @Test
    void testGetCartByOwner_existingCart() {
        // Arrange
        UUID ownerId = UUID.randomUUID();
        Cart cart = new Cart();
        when(repository.getCartByOwner_Id(ownerId)).thenReturn(cart);

        // Act
        Cart result = service.getCartByOwner(ownerId);

        // Assert
        assertNotNull(result);
        assertEquals(cart, result);
    }

    @Test
    void testGetCartByOwner_nonExistingCart() {
        // Arrange
        UUID ownerId = UUID.randomUUID();
        when(repository.getCartByOwner_Id(ownerId)).thenReturn(null);

        // Assert
        assertThrows(ApiRequestException.class, () -> {
            // Act
            service.getCartByOwner(ownerId);
        });
    }
}
