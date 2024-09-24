package project.onlineshop.domain.service;

import project.onlineshop.domain.model.Item;

import java.util.List;

public interface IRedisPopularItemsService {
    void save(List<Item> items);
    List<Item> getAll();
    Item getItemById(Long id);
    void saveItem(Item item);
    void updateItem(Item item);
}
