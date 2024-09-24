package project.onlineshop.security.auth;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import project.onlineshop.domain.model.Address;
import project.onlineshop.domain.model.Like;
import project.onlineshop.security.user.Role;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  @Email(message = "Invalid email format")
  @NotBlank(message = "Password is required")
  private String email;

  @NotBlank(message = "Password is required")
  @Length(min = 6, message = "Password must be at least 6 characters long")
  private String password;

  @Length(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters long")
  private String phone = null;

  @Enumerated(EnumType.STRING)
  private Role role = Role.USER;

  public RegisterRequest(String email, String password) {
      this.email = email;
      this.password = password;
  }

  public RegisterRequest(String email, String password, Role role) {
    this.email = email;
    this.password = password;
    this.role = role;
  }
}
