package project.onlineshop.utils.responses;

import lombok.Getter;
import project.onlineshop.domain.model.Cart;
import project.onlineshop.domain.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class CartResponse {
    private final Long id;
    private final UUID owner;
    private final List<ItemResponse> cartItems;

    public CartResponse(Cart cart){
        id = cart.getId();
        owner = cart.getOwner().getId();
        cartItems = new ArrayList<>();
        cart.getCartItems().forEach(cartItem -> {
            Item item = cartItem.getItem();
            item.setCount(cartItem.getItemCount());
            cartItems.add(new ItemResponse(item));

        });
    }
}
