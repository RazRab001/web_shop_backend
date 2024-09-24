package project.onlineshop.domain.service.impl;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.onlineshop.domain.model.CartItem;
import project.onlineshop.domain.model.Item;
import project.onlineshop.domain.model.User;
import project.onlineshop.domain.repository.ItemRepository;
import project.onlineshop.domain.service.IItemService;
import project.onlineshop.utils.exceptions.ApiRequestException;
import project.onlineshop.utils.requests.ItemRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ItemService implements IItemService {
    private final ItemRepository repository;
    private final LikeService likeService;
    private final CartItemService cartItemService;
    private final OrderItemService orderItemService;
    private final RedisPopularItemsService redisService;
    @Override
    public Item create(Item item) {
        return repository.save(item);
    }

    @Override
    public Item update(Long itemId, ItemRequest itemRequest) {
        Item item = getItemById(itemId);
        item.setName(itemRequest.getName());
        item.setCount(itemRequest.getCount());
        item.setPrice(itemRequest.getPrice());
        item.setDescription(itemRequest.getDescription());
        item.setLikesCount(itemRequest.getLikesCount());
        repository.save(item);
        getAllItems();
        return item;
    }

    @Override
    public void delete(Long id) {
        cartItemService.deleteByItemId(id);
        orderItemService.deleteAllItemsWithItem(id);
        likeService.deleteAllLikesOfItem(id);
        repository.delete(getItemById(id));
        getAllItems();
    }

    @Override
    public Item getItemById(Long id) {
        Item item = redisService.getItemById(id);
        System.out.print("Get item\n");
        if(item == null){
            System.out.print("Item not in cache\n");
            System.out.print(redisService.getAll());
            redisService.saveItem(item);
            System.out.print(item);
            System.out.print("Item in cache\n");
        }
        item = repository.getItemById(id);
        if(item == null){
            throw new ApiRequestException("Item with this ID doesn't exist");
        }
        return item;
    }

    @Override
    public void changeItemsCount(List<CartItem> cartItems) {
        Item gettingItem;
        Integer resultCount;
        for(CartItem item: cartItems){
            gettingItem = item.getItem();
            resultCount = item.getItem().getCount() - item.getItemCount();
            changeItemCount(gettingItem, resultCount);
        }
        getAllItems();
    }

    @Override
    public List<CartItem> checkItemsInCart(List<CartItem> cartItems) {
        List<CartItem> result = new ArrayList<>();
        for(CartItem item: cartItems){
            Item shopItem = getItemById(item.getItem().getId());
            if(!shopItem.getIsActive()){
                item.setItemCount(0);
                result.add(item);
            } else if(shopItem.getCount() < item.getItemCount()){
                item.setItemCount(shopItem.getCount());
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public void changeItemCount(Item item, @Min(0) Integer count) {
        item.setCount(count);
        repository.save(item);
        getAllItems();
    }

    @Override
    public List<Item> getAllItems() {
        List<Item> items = repository.getAll();
        if(items.size() >= 5){
            redisService.save(items.subList(0, 5));
        } else redisService.save(items);{

        }
        return items;
    }

    @Override
    public Item likeItem(User user, Long itemId) {
        Item item = getItemById(itemId);
        if(likeService.userLikeItem(user.getId(), itemId)){
            return item;
        }
        likeService.likeItem(user, item);
        item.setLikesCount(getCountLikesForItem(itemId));
        item = repository.save(item);
        getAllItems();
        return item;
    }

    @Override
    public Item returnLikeFromItem(UUID userId, Long itemId) {
        Item item = getItemById(itemId);
        if(!likeService.userLikeItem(userId, itemId)){
            return item;
        }
        likeService.returnLike(userId, itemId);
        item.setLikesCount(getCountLikesForItem(itemId));
        item = repository.save(item);
        getAllItems();
        return item;
    }

    @Override
    public List<Item> getItemsWitchLikesUser(UUID userId) {
        return repository.getItemsWhichUserLiked(userId);
    }

    @Override
    public Integer getCountLikesForItem(Long itemId) {
        return likeService.getLikesCountForItem(itemId);
    }

    @Override
    public void addOneViewingItem(Item item) {
        item.setViewingCount(item.getViewingCount() + 1);
        repository.save(item);
        getAllItems();
    }

    @Override
    public void addOneAddingToCartItem(Item item) {
        item.setAddingToCartCount(item.getAddingToCartCount() + 1);
        repository.save(item);
        getAllItems();
    }

    @Override
    public void addOneBuyingItem(Item item) {
        item.setBuyingCount(item.getBuyingCount() + 1);
        repository.save(item);
        getAllItems();
    }
}
