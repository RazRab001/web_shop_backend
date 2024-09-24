package project.onlineshop.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isActive = true;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Float price = 0f;

    @NotNull(message = "Count is required")
    @Min(value = 0, message = "Count must be greater than or equal to 0")
    private Integer count;

    private String description;

    //@NotNull(message = "Likes Count is required")
    private Integer likesCount = 0;

    //@NotEmpty(message = "Images cannot be empty")
    @ElementCollection
    private List<byte[]> images;

    @NotNull(message = "Viewing Count is required")
    private Long viewingCount = 0L;

    @NotNull(message = "Adding To Cart Count is required")
    private Long addingToCartCount = 0L;

    @NotNull(message = "Buying Count is required")
    private Long buyingCount = 0L;

    public Item(String name, Float price, Integer count){
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public void setCount(Integer count) {
        if(count == 0){
            setIsActive(false);
        }
        this.count = count;
    }

    public Float getPercentBuyItem(){
        if(viewingCount > 0){
            return (float) (buyingCount/viewingCount);
        }
        return 0f;
    }

    public Float getPercentAddingToCartItem(){
        if(viewingCount > 0){
            return (float) (addingToCartCount/viewingCount);
        }
        return 0f;
    }

    public Float getPercentOfItemInteresting(){
        if(getPercentAddingToCartItem() > 0){
            return getPercentBuyItem()/getPercentAddingToCartItem();
        }
        return 0f;
    }
}
