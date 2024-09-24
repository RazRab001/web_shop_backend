package project.onlineshop.utils.responses;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import project.onlineshop.domain.model.Item;
import project.onlineshop.domain.model.Like;

import java.util.List;

@Getter
public class ItemResponse {
    private final Long id;
    private final Boolean isActive;
    private final String name;
    private final Float price;
    private final Integer count;
    private final Integer likesCount;
    private final String description;
    private final List<byte[]> images;

    public ItemResponse(Item item){
        id = item.getId();
        isActive = item.getIsActive();
        name = item.getName();
        price = item.getPrice();
        count = item.getCount();
        likesCount = item.getLikesCount();
        description = item.getDescription();
        images = item.getImages();
    }
}
