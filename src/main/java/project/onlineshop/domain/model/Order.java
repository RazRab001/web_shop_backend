package project.onlineshop.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Address address;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> items;

    @NotEmpty
    private String password;

    @NotNull
    private BigDecimal sum = BigDecimal.valueOf(0);

    public Order(@NonNull Address address, String password, BigDecimal sum) {
        this.address = address;
        this.password = password;
        this.sum = sum;
    }
}
