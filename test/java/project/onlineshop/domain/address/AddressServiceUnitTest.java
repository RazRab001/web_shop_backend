package project.onlineshop.domain.address;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.onlineshop.domain.model.Address;
import project.onlineshop.domain.model.User;
import project.onlineshop.domain.repository.AddressRepository;
import project.onlineshop.domain.service.impl.AddressService;
import project.onlineshop.utils.exceptions.ApiRequestException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceUnitTest {

    @Mock
    private AddressRepository repository;

    @InjectMocks
    private AddressService service;

    @Test
    void testCreateAddress() {
        // Arrange
        Address address = new Address();
        User user = new User();

        // Mocking repository behavior
        when(repository.save(address)).thenReturn(address);

        // Act
        Address result = service.create(address, user);

        // Assert
        assertNotNull(result);
        assertEquals(address, result);
        verify(repository, times(1)).save(address);
    }

    @Test
    void testDeleteAddress() {
        // Arrange
        Long id = 1L;
        Address address = new Address();
        when(repository.getAddressById(id)).thenReturn(address);

        // Act
        service.delete(id);

        // Assert
        verify(repository, times(1)).delete(address);
    }

    @Test
    void testGetAddressById_ExistingAddress() {
        // Arrange
        Long id = 1L;
        Address address = new Address();
        when(repository.getAddressById(id)).thenReturn(address);

        // Act
        Address result = service.getAddressById(id);

        // Assert
        assertNotNull(result);
        assertEquals(address, result);
    }

    @Test
    void testGetAddressById_NonExistingAddress() {
        // Arrange
        Long id = 1L;
        when(repository.getAddressById(id)).thenReturn(null);

        // Act & Assert
        assertThrows(ApiRequestException.class, () -> service.getAddressById(id));
    }
}

