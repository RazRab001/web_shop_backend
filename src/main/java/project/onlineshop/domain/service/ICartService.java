package project.onlineshop.domain.service;

import project.onlineshop.domain.model.Cart;
import project.onlineshop.domain.model.CartItem;
import project.onlineshop.domain.model.Item;
import project.onlineshop.domain.model.User;

import java.util.List;
import java.util.UUID;

public interface ICartService {
    Cart create(User user);
    Cart addItem(Long CartId, Item item);
    Cart addItem(UUID ownerId, Long itemId);
    Cart deleteItem(UUID ownerId, Long itemId);
    Cart changeItemCount(Cart cart, Long itemId, Integer count);
    Cart changeItemCount(UUID ownerId, Long itemId, Integer count);
    Cart getCartByOwner(UUID id);
    void deleteCart(UUID owner);
    Cart makeCartEmpty(UUID owner);
    List<CartItem> checkItemsInCart(UUID owner);
}
