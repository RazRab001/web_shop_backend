package project.onlineshop.domain.service;

import project.onlineshop.domain.model.Item;
import project.onlineshop.domain.model.Like;
import project.onlineshop.domain.model.User;

import java.util.UUID;

public interface ILikeService {
    Like likeItem(User user, Item item);
    void returnLike(UUID userId, Long itemId);
    boolean userLikeItem(UUID userId, Long itemId);
    Integer getLikesCountForItem(Long itemId);
    void deleteAllLikesOfUser(UUID userId);
    void deleteAllLikesOfItem(Long itemId);
}
