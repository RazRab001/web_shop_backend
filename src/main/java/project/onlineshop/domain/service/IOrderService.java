package project.onlineshop.domain.service;

import lombok.NonNull;
import project.onlineshop.domain.model.Address;
import project.onlineshop.domain.model.CartItem;
import project.onlineshop.domain.model.Order;
import project.onlineshop.domain.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface IOrderService {
    Order create(User owner, @NonNull Address address, List<CartItem> items, String password);
    Order makeOrder(UUID userId, Address address);
    Long getOrderIdByPassword(String password);
    Order getOrderById(Long id);
    void deleteOrder(Long id);
    BigDecimal getOrderSum(List<CartItem> items);
}
