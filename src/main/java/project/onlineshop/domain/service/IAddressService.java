package project.onlineshop.domain.service;

import project.onlineshop.domain.model.Address;
import project.onlineshop.domain.model.User;

public interface IAddressService {
    Address create(Address address, User user);
    void delete(Long id);
    Address getAddressById(Long id);
}
