package project.onlineshop.domain.repository;

import org.springframework.data.repository.CrudRepository;
import project.onlineshop.domain.model.CartItem;

import java.util.List;

public interface CartItemRepository extends CrudRepository<CartItem, Long> {
    CartItem getCartItemByCart_IdAndItem_Id(Long cartId, Long itemId);
    List<CartItem> getCartItemsByItem_Id(Long itemId);
}
