package project.onlineshop.domain.repository;

import org.springframework.data.repository.CrudRepository;
import project.onlineshop.domain.model.Cart;

import java.util.UUID;

public interface CartRepository extends CrudRepository<Cart, Long> {
    Cart getCartById(Long id);
    Cart getCartByOwner_Id(UUID id);
}
