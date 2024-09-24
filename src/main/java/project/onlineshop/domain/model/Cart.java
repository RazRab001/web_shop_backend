package project.onlineshop.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_id")
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @NotNull(message = "Owner is required")
    private User owner;

    public void addItem(CartItem item){
        if(item != null && item.getItem().getIsActive()){
            cartItems.add(item);
        }
    }

    public CartItem itemInCart(Long itemId){
        for(CartItem cartItem : cartItems){
            if(Objects.equals(cartItem.getItem().getId(), itemId)){
                return cartItem;
            }
        }
        return null;
    }

    public void deleteItem(Long itemId){
        Iterator<CartItem> iterator = cartItems.iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getItem().getId().equals(itemId)) {
                iterator.remove();
                break;
            }
        }
    }

    public void changeItemCount(Long itemId, Integer count){
        for(CartItem item : cartItems){
            if(Objects.equals(item.getItem().getId(), itemId)){
                item.setItemCount(count);
                break;
            }
        }
    }
}
