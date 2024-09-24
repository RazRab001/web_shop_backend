package project.onlineshop.utils.responses;

import lombok.Getter;
import project.onlineshop.domain.model.*;
import project.onlineshop.utils.responses.ItemResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class OrderResponse {
    private final Long id;
    private final List<ItemResponse> cartItems;
    private final Address address;
    private final String password;
    private final BigDecimal sum;
    public OrderResponse(Order order){
        id = order.getId();
        address = order.getAddress();
        this.sum = order.getSum();
        cartItems = new ArrayList<>();
        order.getItems().forEach(cartItem -> {
            Item item = cartItem.getItem();
            item.setCount(cartItem.getItemCount());
            cartItems.add(new ItemResponse(item));

        });
        password = order.getPassword();
    }
}
