package project.onlineshop.domain.service;

import project.onlineshop.domain.model.CartItem;
import project.onlineshop.domain.model.Item;
import project.onlineshop.domain.model.User;
import project.onlineshop.utils.requests.ItemRequest;

import java.util.List;
import java.util.UUID;

public interface IItemService {
    Item create(Item item);
    Item update(Long itemId, ItemRequest itemRequest);
    //Item updateItemInDBandAddUpdateRedis(Item item);
    void delete(Long id);
    Item getItemById(Long id);
    void changeItemsCount(List<CartItem> cartItems);
    List<CartItem> checkItemsInCart(List<CartItem> cartItems);
    void changeItemCount(Item item, Integer count);
    List<Item> getAllItems();
    Item likeItem(User user, Long itemId);
    Item returnLikeFromItem(UUID userId, Long itemId);
    List<Item> getItemsWitchLikesUser(UUID userId);
    Integer getCountLikesForItem(Long itemId);
    void addOneViewingItem(Item item);
    void addOneAddingToCartItem(Item item);
    void addOneBuyingItem(Item item);
}
