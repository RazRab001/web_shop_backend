package project.onlineshop.domain.repository;

import org.springframework.data.repository.CrudRepository;
import project.onlineshop.domain.model.User;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
    User getUserById(UUID id);
    User getUserByEmail(String email);
}
