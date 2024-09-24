package project.onlineshop.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import project.onlineshop.domain.model.Like;

import java.util.List;
import java.util.UUID;

public interface LikeRepository extends CrudRepository<Like, Long> {
    @Query("SELECT COUNT(*) FROM Like l WHERE l.item.id = :id")
    Integer getLikesCountOfItem(Long id);
    Like getLikeByUser_IdAndItem_Id(UUID userId, Long itemId);
    List<Like> getLikesByUser_Id(UUID userId);
    List<Like> getLikesByItem_Id(Long itemId);
}
