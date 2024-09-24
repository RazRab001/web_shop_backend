package project.onlineshop.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Country is required and must not be blank")
    private String country;

    @NotBlank(message = "City is required and must not be blank")
    private String city;

    @NotBlank(message = "Street is required and must not be blank")
    private String street;

    @NotNull(message = "Home number is required")
    private Integer home;

    public Address(String country, String city, String street, Integer home){
        this.country = country;
        this.city = city;
        this.street = street;
        this.home = home;
    }
}
