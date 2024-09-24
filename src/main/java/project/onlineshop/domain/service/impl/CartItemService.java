package project.onlineshop.domain.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.onlineshop.domain.model.Cart;
import project.onlineshop.domain.model.CartItem;
import project.onlineshop.domain.model.Item;
import project.onlineshop.domain.repository.CartItemRepository;
import project.onlineshop.domain.service.ICartItemService;

@Service
@AllArgsConstructor
public class CartItemService implements ICartItemService {
    private final CartItemRepository repository;
    @Override
    public CartItem create(Cart cart, Item item) {
        return repository.save(new CartItem(cart, item));
    }

    @Override
    public void delete(Long cartId, Long itemId) {
        CartItem cartItem = repository.getCartItemByCart_IdAndItem_Id(cartId, itemId);
        repository.delete(cartItem);
    }

    @Override
    public void deleteByItemId(Long itemId) {
        repository.deleteAll(repository.getCartItemsByItem_Id(itemId));
    }


}
