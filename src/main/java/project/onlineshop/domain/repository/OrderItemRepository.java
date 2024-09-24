package project.onlineshop.domain.repository;

import org.springframework.data.repository.CrudRepository;
import project.onlineshop.domain.model.OrderItem;

import java.util.List;

public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {
    List<OrderItem> getOrderItemsByOrder_Id(Long orderId);
    List<OrderItem> getOrderItemsByItem_Id(Long itemId);
}
