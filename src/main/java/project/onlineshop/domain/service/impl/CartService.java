package project.onlineshop.domain.service.impl;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Service;
import project.onlineshop.domain.model.Cart;
import project.onlineshop.domain.model.CartItem;
import project.onlineshop.domain.model.Item;
import project.onlineshop.domain.model.User;
import project.onlineshop.domain.repository.CartRepository;
import project.onlineshop.domain.service.ICartService;
import project.onlineshop.utils.exceptions.ApiRequestException;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService implements ICartService {
    private final CartRepository repository;
    private final ItemService itemService;
    private final CartItemService cartItemService;
    @Override
    public Cart create(User user) {
        Cart cart = new Cart();
        cart.setOwner(user);
        return repository.save(cart);
    }

    @Override
    public Cart addItem(Long CartId, Item item) {
        Cart cart = repository.getCartById(CartId);
        CartItem cartItem = cart.itemInCart(item.getId());
        if(cartItem == null){
            cart.addItem(cartItemService.create(cart, item));
            itemService.addOneAddingToCartItem(item);
            return repository.save(cart);
        }
        cart.changeItemCount(item.getId(), cartItem.getItemCount()+1);
        return repository.save(cart);
    }

    @Override
    public Cart addItem(UUID ownerId, Long itemId) {
        Cart cart = getCartByOwner(ownerId);
        Item item = itemService.getItemById(itemId);
        CartItem cartItem = cart.itemInCart(itemId);
        if(cartItem == null){
            cart.addItem(cartItemService.create(cart, item));
            itemService.addOneAddingToCartItem(item);
            return repository.save(cart);
        }
        cart.changeItemCount(itemId, cartItem.getItemCount()+1);
        return repository.save(cart);
    }

    @Override
    public Cart deleteItem(UUID ownerId, Long itemId) {
        Cart cart = repository.getCartByOwner_Id(ownerId);
        if(cart.itemInCart(itemId) == null){
            return cart;
        }
        cartItemService.delete(cart.getId(), itemId);
        cart.deleteItem(itemId);
        return repository.save(cart);
    }

    @Override
    public Cart changeItemCount(Cart cart, Long itemId, Integer count) {
        if(itemService.getItemById(itemId).getCount() < count){
            return cart;
        }
        if(count <= 0){
            cart.deleteItem(itemId);
        }
        cart.changeItemCount(itemId, count);
        return repository.save(cart);
    }

    @Override
    public Cart changeItemCount(UUID ownerId, Long itemId, Integer count) {
        return changeItemCount(getCartByOwner(ownerId), itemId, count);
    }

    @Override
    public Cart getCartByOwner(UUID id) {
        Cart cart = repository.getCartByOwner_Id(id);
        if(cart == null){
            throw new ApiRequestException("User with this ID doesn't have cart or cart doesn't exist");
        }
        return cart;
    }

    @Override
    public void deleteCart(UUID owner) {
        Cart cart = getCartByOwner(owner);
        if (cart != null) {
            repository.delete(cart);
        }
    }

    @Override
    public Cart makeCartEmpty(UUID owner) {
        Cart cart = getCartByOwner(owner);
        for (CartItem item : cart.getCartItems()) {
            cartItemService.delete(item.getCart().getId(), item.getItem().getId());
        }
        cart.getCartItems().clear();
        return repository.save(cart);
    }


    @Override
    public List<CartItem> checkItemsInCart(UUID owner) {
        Cart cart = getCartByOwner(owner);
        List<CartItem> itemsInCart = itemService.checkItemsInCart(cart.getCartItems());
        for(CartItem item : itemsInCart){
            changeItemCount(cart, item.getItem().getId(), item.getItemCount());
        }
        return itemsInCart;
    }
}
