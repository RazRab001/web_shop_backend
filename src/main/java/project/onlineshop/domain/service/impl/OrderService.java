package project.onlineshop.domain.service.impl;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project.onlineshop.utils.PasswordGenerator;
import project.onlineshop.domain.model.*;
import project.onlineshop.domain.repository.OrderRepository;
import project.onlineshop.domain.service.IOrderService;
import project.onlineshop.utils.exceptions.ApiRequestException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository repository;
    private final CartService cartService;
    private final ItemService itemService;
    private final UserService userService;
    private final OrderItemService orderItemService;

    @Override
    public Order create(User owner, @NonNull Address address, @NotEmpty List<CartItem> items, String password) {
        Order order = repository.save(new Order(address, password, getOrderSum(items)));
        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem item : items){
            orderItems.add(orderItemService.create(order, item.getItem(), item.getItemCount()));
            itemService.addOneBuyingItem(item.getItem());
        }
        order.setItems(orderItems);
        cartService.makeCartEmpty(owner.getId());
        return repository.save(order);
    }

    @Override
    public Order makeOrder(UUID userId, Address address) {
        Cart cart = cartService.getCartByOwner(userId);
        Address newAddress = userService.addAddress(userId, address);
        if(newAddress == null){
            throw new ApiRequestException("Address adding error");
        }
        List<CartItem> controlList = itemService.checkItemsInCart(cart.getCartItems());
        if(controlList.isEmpty()){
            itemService.changeItemsCount(cart.getCartItems());
            return repository.save(create(cart.getOwner(), newAddress,
                    cart.getCartItems(), PasswordGenerator.generatePassword(6)));
        } else {
            controlList.forEach(cartItem -> cartService.changeItemCount(cart,
                    cartItem.getItem().getId(), cartItem.getItemCount()));
            throw new ApiRequestException("Cart was changed");
        }
    }

    @Override
    public Long getOrderIdByPassword(String password) {
        Order order = repository.getOrderByPassword(password);
        if(order == null){
            throw new ApiRequestException("Order with this ID doesn't exist");
        }
        return order.getId();
    }

    @Override
    public Order getOrderById(Long id) {
        Order order = repository.getOrderById(id);
        if(order == null){
            throw new ApiRequestException("Order with this ID doesn't exist");
        }
        return order;
    }

    @Override
    public void deleteOrder(Long id) {
        orderItemService.deleteAllItemsWithOrder(id);
        repository.delete(getOrderById(id));
    }

    @Override
    public BigDecimal getOrderSum(List<CartItem> items) {
        if (items.isEmpty()) {
            throw new ApiRequestException("Your cart is empty");
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (CartItem cartItem : items) {
            BigDecimal price = BigDecimal.valueOf(cartItem.getItem().getPrice());
            BigDecimal itemCount = BigDecimal.valueOf(cartItem.getItemCount());
            BigDecimal itemTotal = price.multiply(itemCount);
            sum = sum.add(itemTotal);
        }

        return sum;
    }
}
