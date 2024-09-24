package project.onlineshop.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Password is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Length(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @Length(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters long")
    private String phone = null;

    @OneToMany()
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Like> likes;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String phone) {
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public void addAddress(Address address){
        addresses.add(address);
    }

    public Address hasDuplicateAddress(Address newAddress) {
        for (Address address : addresses) {
            if (address != null && isSameExceptId(address, newAddress)) {
                return address;
            }
        }
        return null;
    }

    private boolean isSameExceptId(Address address1, Address address2) {
        for (Field field : Address.class.getDeclaredFields()) {
            if (!field.getName().equals("id")) {
                try {
                    field.setAccessible(true);
                    Object value1 = field.get(address1);
                    Object value2 = field.get(address2);
                    if (value1 != null ? !value1.equals(value2) : value2 != null) {
                        return false;
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Access denied to field: " + field.getName(), e);
                }
            }
        }
        return true;
    }
}
