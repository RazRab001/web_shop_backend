package project.onlineshop.domain.repository;

import org.springframework.data.repository.CrudRepository;
import project.onlineshop.domain.model.Address;

public interface AddressRepository extends CrudRepository<Address, Long> {
    Address getAddressById(Long id);
}
