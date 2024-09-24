package project.onlineshop.domain.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.onlineshop.domain.model.Address;
import project.onlineshop.domain.model.User;
import project.onlineshop.domain.repository.UserRepository;
import project.onlineshop.domain.service.impl.AddressService;
import project.onlineshop.domain.service.impl.CartService;
import project.onlineshop.domain.service.impl.UserService;
import project.onlineshop.utils.exceptions.ApiRequestException;
import project.onlineshop.domain.service.impl.LikeService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceUnitTest {

    @Mock
    private UserRepository repository;
    @Mock
    private AddressService addressService;
    @Mock
    private CartService cartService;
    @Mock
    private LikeService likeService;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(repository, addressService, cartService, likeService);
    }

    @Test
    void testCreateUser() {
        // Mock data
        User user = new User();

        // Mock behavior
        when(repository.save(user)).thenReturn(user);

        // Invoke method
        User result = userService.create(user);

        // Verify interactions
        verify(repository).save(user);
        verify(cartService).create(user);

        // Assertions
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testUpdateUser() {
        // Mock data
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        // Mock behavior
        when(repository.save(user)).thenReturn(user);

        // Invoke method
        User result = userService.update(userId, user);

        // Assertions
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testGetUserById() {
        // Mock data
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        // Mock behavior
        when(repository.getUserById(userId)).thenReturn(user);

        // Invoke method
        User result = userService.getUserById(userId);

        // Assertions
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testGetUserByIdWithNonexistentUser() {
        // Mock data
        UUID userId = UUID.randomUUID();

        // Mock behavior
        when(repository.getUserById(userId)).thenReturn(null);

        // Invoke method and verify exception
        assertThrows(ApiRequestException.class, () -> userService.getUserById(userId));
    }

    @Test
    void testGetUserByEmail() {
        // Mock data
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        // Mock behavior
        when(repository.getUserByEmail(email)).thenReturn(user);

        // Invoke method
        User result = userService.getUserByEmail(email);

        // Assertions
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testGetUserByEmailWithNonexistentUser() {
        // Mock data
        String email = "test@example.com";

        // Mock behavior
        when(repository.getUserByEmail(email)).thenReturn(null);

        // Invoke method and verify exception
        assertThrows(ApiRequestException.class, () -> userService.getUserByEmail(email));
    }

    @Test
    void testDeleteUser() {
        // Mock data
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        // Mock behavior
        when(repository.getUserById(userId)).thenReturn(user);

        // Invoke method
        assertDoesNotThrow(() -> userService.delete(userId));

        // Verify interactions
        verify(likeService).deleteAllLikesOfUser(userId);
        verify(cartService).deleteCart(userId);
        verify(repository).delete(user);
    }

    @Test
    void testDeleteNonexistentUser() {
        // Mock data
        UUID userId = UUID.randomUUID();

        // Mock behavior
        when(repository.getUserById(userId)).thenReturn(null);

        // Invoke method and verify exception
        assertThrows(ApiRequestException.class, () -> userService.delete(userId));
    }

    @Test
    void testAddAddress() {
        // Mock data
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        Address address = new Address();

        // Mock behavior
        when(repository.getUserById(userId)).thenReturn(user);
        when(addressService.create(address, user)).thenReturn(address);

        // Invoke method
        Address result = userService.addAddress(userId, address);

        // Verify interactions
        verify(repository).getUserById(userId);
        verify(addressService).create(address, user);
        verify(repository).save(user);

        // Assertions
        assertNotNull(result);
        assertEquals(address, result);
    }
}
