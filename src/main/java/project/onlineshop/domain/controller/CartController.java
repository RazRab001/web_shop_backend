package project.onlineshop.domain.controller;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.onlineshop.domain.model.CartItem;
import project.onlineshop.domain.service.impl.CartService;
import project.onlineshop.utils.responses.CartResponse;
import project.onlineshop.utils.responses.ItemResponse;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService service;
    @GetMapping("/{ownerId}")
    CartResponse getCart(@PathVariable UUID ownerId){
        return new CartResponse(service.getCartByOwner(ownerId));
    }
    @PutMapping("/{ownerId}/{itemId}/{count}")
    CartResponse changeItemCountInCart(@PathVariable UUID ownerId,
                               @PathVariable Long itemId,
                               @PathVariable @Min(0) Integer count){
        return new CartResponse(service.changeItemCount(ownerId, itemId, count));
    }
    @PutMapping("/{ownerId}/{itemId}")
    CartResponse addItemInCart(@PathVariable UUID ownerId, @PathVariable Long itemId){
        return new CartResponse(service.addItem(ownerId, itemId));
    }
    @DeleteMapping("/{ownerId}/{itemId}")
    CartResponse deleteItemInCart(@PathVariable UUID ownerId, @PathVariable Long itemId){
        return new CartResponse(service.deleteItem(ownerId, itemId));
    }
    @PutMapping("/{ownerId}")
    CartResponse devastateCart(@PathVariable UUID ownerId){
        return new CartResponse(service.makeCartEmpty(ownerId));
    }
    @PostMapping("/{ownerId}")
    List<ItemResponse> checkItemsInCartBeforeBuy(@PathVariable UUID ownerId){
        return service.checkItemsInCart(ownerId).stream()
                .map(CartItem::getItem)
                .map(ItemResponse::new)
                .collect(Collectors.toList());
    }
}
