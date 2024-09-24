package project.onlineshop.domain.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project.onlineshop.domain.model.Address;
import project.onlineshop.domain.model.User;
import project.onlineshop.domain.repository.UserRepository;
import project.onlineshop.domain.service.IUserService;
import project.onlineshop.utils.exceptions.ApiRequestException;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements IUserService {
    private final UserRepository repository;
    private final AddressService addressService;
    private final CartService cartService;
    private final LikeService likeService;

    @Override
    public User create(User user) {
        User newUser = repository.save(user);
        cartService.create(newUser);
        return newUser;
    }

    @Override
    public User update(UUID userId, User user) {
        user.setId(userId);
        return repository.save(user);
    }

    @Override
    public User getUserById(UUID id) {
        User user = repository.getUserById(id);
        if(user == null){
            throw new ApiRequestException("User with this ID doesn't exist");
        }
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        User user = repository.getUserByEmail(email);
        if(user == null){
            throw new ApiRequestException("User with this ID doesn't exist");
        }
        return user;
    }

    @Override
    public void delete(UUID id) {
        User user = getUserById(id);
        likeService.deleteAllLikesOfUser(id);
        cartService.deleteCart(id);
        repository.delete(user);
    }

    @Override
    public Address addAddress(UUID id, Address address) {
        User user = getUserById(id);
        Address duplAddress = user.hasDuplicateAddress(address);
        if(duplAddress == null){
            Address newAddress = addressService.create(address, user);
            user.addAddress(newAddress);
            repository.save(user);
            return newAddress;
        }
        return duplAddress;
    }
}
