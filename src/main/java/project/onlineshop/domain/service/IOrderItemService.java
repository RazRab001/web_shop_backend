package project.onlineshop.domain.service;

import project.onlineshop.domain.model.Item;
import project.onlineshop.domain.model.Order;
import project.onlineshop.domain.model.OrderItem;

public interface IOrderItemService {
    OrderItem create(Order order, Item item, Integer count);
    void deleteAllItemsWithOrder(Long orderId);
    void deleteAllItemsWithItem(Long itemId);
}
