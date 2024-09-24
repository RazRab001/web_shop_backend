package project.onlineshop.domain.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.onlineshop.domain.model.Item;
import project.onlineshop.domain.model.Like;
import project.onlineshop.domain.model.User;
import project.onlineshop.domain.repository.LikeRepository;
import project.onlineshop.domain.service.ILikeService;

import java.util.UUID;
@Service
@AllArgsConstructor
public class LikeService implements ILikeService {
    private final LikeRepository repository;
    @Override
    public Like likeItem(User user, Item item) {
        Like like = repository.save(new Like());
        like.setItem(item);
        like.setUser(user);
        return repository.save(like);
    }

    @Override
    public void returnLike(UUID userId, Long itemId) {
        Like like = repository.getLikeByUser_IdAndItem_Id(userId, itemId);
        repository.delete(like);
    }

    @Override
    public boolean userLikeItem(UUID userId, Long itemId) {
        return repository.getLikeByUser_IdAndItem_Id(userId, itemId) != null;
    }

    @Override
    public Integer getLikesCountForItem(Long itemId) {
        return repository.getLikesCountOfItem(itemId);
    }

    @Override
    public void deleteAllLikesOfUser(UUID userId) {
        repository.deleteAll(repository.getLikesByUser_Id(userId));
    }

    @Override
    public void deleteAllLikesOfItem(Long itemId) {
        repository.deleteAll(repository.getLikesByItem_Id(itemId));
    }
}
