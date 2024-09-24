package project.onlineshop.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import project.onlineshop.domain.model.Item;

import java.util.List;
import java.util.UUID;

public interface ItemRepository extends CrudRepository<Item, Long> {
    Item getItemById(Long id);
    @Query("SELECT i FROM Item i ORDER BY i.likesCount DESC")
    List<Item> getAll();
    @Query("SELECT i FROM Item i WHERE i.id IN (SELECT l.item.id FROM Like l WHERE l.user.id = :userId)")
    List<Item> getItemsWhichUserLiked(UUID userId);
}
