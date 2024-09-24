package project.onlineshop.domain.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.onlineshop.domain.model.Address;
import project.onlineshop.domain.service.impl.OrderService;
import project.onlineshop.utils.responses.OrderResponse;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService service;
    @PostMapping("/{id}")
    OrderResponse createOrder(@PathVariable UUID id, @RequestBody @Valid Address address){
        return new OrderResponse(service.makeOrder(id, address));
    }
    @GetMapping("/pass/{password}")
    Long getOrderIdByPassword(@PathVariable @Size(min = 6, max = 6) String password){
        return service.getOrderIdByPassword(password);
    }
    @GetMapping("/{id}")
    OrderResponse getOrderById(@PathVariable Long id){
        return new OrderResponse(service.getOrderById(id));
    }

    @DeleteMapping("/{id}")
    void deleteOrder(@PathVariable Long id){
        service.deleteOrder(id);
    }
}
