package project.onlineshop.domain.service.impl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import project.onlineshop.domain.model.Item;
import project.onlineshop.domain.service.IRedisPopularItemsService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class RedisPopularItemsService implements IRedisPopularItemsService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public RedisPopularItemsService(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void save(List<Item> items) {
        try {
            String itemsJson = objectMapper.writeValueAsString(items);
            redisTemplate.opsForValue().set("popularItems", itemsJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Item> getAll() {
        String itemsJson = redisTemplate.opsForValue().get("popularItems");
        if (itemsJson != null) {
            try {
                return Arrays.asList(objectMapper.readValue(itemsJson, Item[].class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Item getItemById(Long id) {
        List<Item> popularItems = getAll();
        if (popularItems != null) {
            for (Item item : popularItems) {
                if (item != null && Objects.equals(item.getId(), id)) {
                    return item;
                }
            }
        }
        return null;
    }

    @Override
    public void saveItem(Item item) {
        List<Item> redisItems = getAll();
        redisItems.set(redisItems.size()-1, item);
        save(redisItems);
    }

    @Override
    public void updateItem(Item item) {
        List<Item> redisItems = getAll();
        if (redisItems != null) {
            for (int i = 0; i < redisItems.size(); i++) {
                if (redisItems.get(i).getId().equals(item.getId())) {
                    redisItems.set(i, item);
                    break;
                }
            }
            save(redisItems);
        }
    }
}
