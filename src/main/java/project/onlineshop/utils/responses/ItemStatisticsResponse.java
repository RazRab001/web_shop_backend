package project.onlineshop.utils.responses;

import lombok.Getter;
import project.onlineshop.domain.model.Item;

@Getter
public class ItemStatisticsResponse {
    private final Long itemId;
    private final String itemName;
    private final Integer likesCount;

    private final Long viewingCount;
    private final Long addingToCartCount;
    private final Long buyingCount;

    private final Float addingToCartPercent;
    private final Float buyingPercent;
    private final Float interestingPercent;

    public ItemStatisticsResponse(Item item){
        itemId = item.getId();
        itemName = item.getName();
        likesCount = item.getLikesCount();

        viewingCount = item.getViewingCount();
        addingToCartCount = item.getAddingToCartCount();
        buyingCount = item.getBuyingCount();

        addingToCartPercent = item.getPercentAddingToCartItem();
        buyingPercent = item.getPercentBuyItem();
        interestingPercent = item.getPercentOfItemInteresting();
    }
}
