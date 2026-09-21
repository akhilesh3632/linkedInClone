package com.nagraj.linkdin.posts_service.repository;

import com.nagraj.linkdin.posts_service.entity.PostLike;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByUserIdAndPostId(Long userId, Long postId);

//    @Transactional
    void deleteByUserIdAndPostId(Long userId, Long postId);
}
