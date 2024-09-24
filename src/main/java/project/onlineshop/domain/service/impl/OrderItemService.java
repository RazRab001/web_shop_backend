package project.onlineshop.domain.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.onlineshop.domain.model.Item;
import project.onlineshop.domain.model.Order;
import project.onlineshop.domain.model.OrderItem;
import project.onlineshop.domain.repository.OrderItemRepository;
import project.onlineshop.domain.service.IOrderItemService;

@Service
@AllArgsConstructor
public class OrderItemService implements IOrderItemService {
    private final OrderItemRepository repository;
    @Override
    public OrderItem create(Order order, Item item, Integer count) {
        return repository.save(new OrderItem(order, item, count));
    }

    @Override
    public void deleteAllItemsWithOrder(Long orderId) {
        repository.deleteAll(repository.getOrderItemsByOrder_Id(orderId));
    }

    @Override
    public void deleteAllItemsWithItem(Long itemId) {
        repository.deleteAll(repository.getOrderItemsByItem_Id(itemId));
    }
}
