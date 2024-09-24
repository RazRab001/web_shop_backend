package project.onlineshop.domain.like;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.onlineshop.domain.model.Item;
import project.onlineshop.domain.model.Like;
import project.onlineshop.domain.model.User;
import project.onlineshop.domain.repository.LikeRepository;
import project.onlineshop.domain.service.impl.LikeService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceUnitTest {

    @Mock
    private LikeRepository repository;

    @InjectMocks
    private LikeService service;

    @Test
    void testLikeItem() {
        // Arrange
        User user = new User();
        Item item = new Item();
        Like like = new Like();
        when(repository.save(any(Like.class))).thenReturn(like);

        // Act
        Like result = service.likeItem(user, item);

        // Assert
        assertNotNull(result);
        assertEquals(like, result);
        verify(repository, times(2)).save(any(Like.class)); // This verifies the number of invocations
    }


    @Test
    void testReturnLike() {
        // Arrange
        UUID userId = UUID.randomUUID();
        Long itemId = 1L;
        Like like = new Like();
        when(repository.getLikeByUser_IdAndItem_Id(userId, itemId)).thenReturn(like);

        // Act
        service.returnLike(userId, itemId);

        // Assert
        verify(repository, times(1)).delete(like);
    }

    @Test
    void testUserLikeItem_existingLike() {
        // Arrange
        UUID userId = UUID.randomUUID();
        Long itemId = 1L;
        when(repository.getLikeByUser_IdAndItem_Id(userId, itemId)).thenReturn(new Like());

        // Act
        boolean result = service.userLikeItem(userId, itemId);

        // Assert
        assertTrue(result);
    }

    @Test
    void testUserLikeItem_nonExistingLike() {
        // Arrange
        UUID userId = UUID.randomUUID();
        Long itemId = 1L;
        when(repository.getLikeByUser_IdAndItem_Id(userId, itemId)).thenReturn(null);

        // Act
        boolean result = service.userLikeItem(userId, itemId);

        // Assert
        assertFalse(result);
    }
}

