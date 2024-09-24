package project.onlineshop.domain.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project.onlineshop.domain.model.Address;
import project.onlineshop.domain.model.User;
import project.onlineshop.domain.repository.AddressRepository;
import project.onlineshop.domain.service.IAddressService;
import project.onlineshop.utils.exceptions.ApiRequestException;

@Service
@AllArgsConstructor
public class AddressService implements IAddressService {
    private final AddressRepository repository;
    @Override
    public Address create(Address address, User user) {
        return repository.save(address);
    }

    @Override
    public void delete(Long id) {
        Address address = getAddressById(id);
        repository.delete(address);
    }

    @Override
    public Address getAddressById(Long id) {
        Address address = repository.getAddressById(id);
        if(address == null){
            throw new ApiRequestException("Unknown address");
        }
        return address;
    }
}
