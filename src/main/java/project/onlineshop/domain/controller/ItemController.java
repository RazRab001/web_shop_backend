package project.onlineshop.domain.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.onlineshop.domain.model.Item;
import project.onlineshop.domain.model.User;
import project.onlineshop.domain.service.impl.ItemService;
import project.onlineshop.domain.service.impl.UserService;
import project.onlineshop.utils.requests.ItemRequest;
import project.onlineshop.utils.responses.ItemResponse;
import project.onlineshop.utils.responses.ItemStatisticsResponse;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/item")
@Validated
public class ItemController {
    private final ItemService service;
    private final UserService userService;
    @PostMapping("")
    ItemResponse createItem(@RequestBody @Valid Item item){
        return new ItemResponse(service.create(item));
    }
    @GetMapping("/{id}")
    ItemResponse getItem(@PathVariable Long id){
        Item item = service.getItemById(id);
        service.addOneViewingItem(item);
        return new ItemResponse(item);
    }
    @GetMapping("")
    List<ItemResponse> getAllItems(){
        return service.getAllItems().stream()
                .map(ItemResponse::new)
                .collect(Collectors.toList());
    }
    @PutMapping("/{id}")
    ItemResponse updateItem(@PathVariable Long id, @RequestBody @Valid ItemRequest item){
        return new ItemResponse(service.update(id, item));
    }
    @DeleteMapping("/{id}")
    void deleteItem(@PathVariable Long id){
        service.delete(id);
    }
    @PutMapping("/{id}/{count}")
    ItemResponse changeItemCount(@PathVariable Long id, @PathVariable @Min(0) Integer count){
        Item item = service.getItemById(id);
        service.changeItemCount(item, count);
        return new ItemResponse(item);
    }
    @PutMapping("/like/{userId}/{itemId}")
    ItemResponse likeItem(@PathVariable UUID userId, @PathVariable Long itemId){
        User user = userService.getUserById(userId);
        return new ItemResponse(service.likeItem(user, itemId));
    }
    @DeleteMapping("/like/{userId}/{itemId}")
    ItemResponse deleteLikeFromItem(@PathVariable UUID userId, @PathVariable Long itemId){
        return new ItemResponse(service.returnLikeFromItem(userId, itemId));
    }
    @GetMapping("/like/{id}")
    List<ItemResponse> getLikesItem(@PathVariable UUID id){
        return service.getItemsWitchLikesUser(id).stream()
                .map(ItemResponse::new)
                .collect(Collectors.toList());
    }
    @GetMapping("/statistics")
    List<ItemStatisticsResponse> getAllItemsStatistics(){
        return service.getAllItems().stream()
                .map(ItemStatisticsResponse::new)
                .collect(Collectors.toList());
    }
    @GetMapping("/statistics/{id}")
    ItemStatisticsResponse getItemStatistic(@PathVariable Long id){
        return new ItemStatisticsResponse(service.getItemById(id));
    }
}
