package project.onlineshop.utils.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import project.onlineshop.domain.model.Address;

@Data
@AllArgsConstructor
public class AddressRequest {
    @NotBlank(message = "Country is required and must not be blank")
    private String country;

    @NotBlank(message = "City is required and must not be blank")
    private String city;

    @NotBlank(message = "Street is required and must not be blank")
    private String street;

    @NotNull(message = "Home number is required")
    private Integer home;

    public Address toAddress(AddressRequest request){
        return new Address(
                request.getCountry(),
                request.getCity(),
                request.getStreet(),
                request.getHome()
        );
    }
}
