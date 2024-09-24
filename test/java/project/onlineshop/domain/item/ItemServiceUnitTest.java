package project.onlineshop.domain.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.onlineshop.domain.model.*;
import project.onlineshop.domain.repository.ItemRepository;
import project.onlineshop.domain.service.impl.*;
import project.onlineshop.utils.exceptions.ApiRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceUnitTest {

    @Mock
    private ItemRepository repository;

    @Mock
    private LikeService likeService;

    @Mock
    private CartItemService cartItemService;

    @Mock
    private OrderItemService orderItemService;

    @Mock
    private RedisPopularItemsService redisService;

    @InjectMocks
    private ItemService service;

    @Test
    void testCreateItem() {
        // Arrange
        Item item = new Item();

        // Mocking repository behavior
        when(repository.save(item)).thenReturn(item);

        // Act
        Item result = service.create(item);

        // Assert
        assertNotNull(result);
        assertEquals(item, result);
        verify(repository, times(1)).save(item);
    }

    @Test
    void testGetItemById_existingItem() {
        // Arrange
        Long itemId = 1L;
        Item item = new Item();
        when(redisService.getItemById(itemId)).thenReturn(item);
        when(repository.getItemById(itemId)).thenReturn(item);

        // Act
        Item result = service.getItemById(itemId);

        // Assert
        assertNotNull(result);
        assertEquals(item, result);
    }

    @Test
    void testGetItemById_nonExistingItem() {
        // Arrange
        Long itemId = 1L;
        when(redisService.getItemById(itemId)).thenReturn(null);
        when(repository.getItemById(itemId)).thenReturn(null);

        // Assert
        assertThrows(ApiRequestException.class, () -> {
            // Act
            service.getItemById(itemId);
        });
    }

    @Test
    void testDeleteItem() {
        // Arrange
        Long itemId = 1L;
        Item item = new Item();
        when(repository.getItemById(itemId)).thenReturn(item);

        // Act
        service.delete(itemId);

        // Assert
        verify(cartItemService, times(1)).deleteByItemId(itemId);
        verify(orderItemService, times(1)).deleteAllItemsWithItem(itemId);
        verify(likeService, times(1)).deleteAllLikesOfItem(itemId);
        verify(repository, times(1)).delete(item);
    }
}
