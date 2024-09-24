package project.onlineshop.domain.repository;

import org.springframework.data.repository.CrudRepository;
import project.onlineshop.domain.model.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {
    Order getOrderById(Long id);
    Order getOrderByPassword(String password);
}
