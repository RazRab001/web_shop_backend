package project.onlineshop.utils.responses;

import lombok.Getter;
import project.onlineshop.domain.model.Address;
import project.onlineshop.domain.model.User;

import java.util.List;
import java.util.UUID;

@Getter
public class UserResponse {
    private final UUID id;
    private final String email;
    private final String phone;
    private final String password;
    private final List<Address> addresses;

    public UserResponse(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.phone = user.getPhone();
        this.addresses = user.getAddresses();
    }
}
