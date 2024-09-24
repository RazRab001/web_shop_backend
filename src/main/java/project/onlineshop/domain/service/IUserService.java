package project.onlineshop.domain.service;

import project.onlineshop.domain.model.Address;
import project.onlineshop.domain.model.User;

import java.util.UUID;

public interface IUserService {
    User create(User user);
    User update(UUID userId, User user);
    User getUserById(UUID id);
    User getUserByEmail(String email);
    void delete(UUID id);
    Address addAddress(UUID id, Address address);
}
