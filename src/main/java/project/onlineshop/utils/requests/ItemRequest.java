package project.onlineshop.utils.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Float price;

    @NotNull(message = "Count is required")
    @Min(value = 0, message = "Count must be greater than or equal to 0")
    private Integer count;

    private String description;

    private Integer likesCount;
}
