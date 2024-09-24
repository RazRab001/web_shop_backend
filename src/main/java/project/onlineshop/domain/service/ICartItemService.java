package project.onlineshop.domain.service;

import project.onlineshop.domain.model.Cart;
import project.onlineshop.domain.model.CartItem;
import project.onlineshop.domain.model.Item;

public interface ICartItemService {
    CartItem create(Cart cart, Item item);
    void delete(Long cartId, Long itemId);
    void deleteByItemId(Long itemId);
}
