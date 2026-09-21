package com.nagraj.linkdin.posts_service.controller;

import com.nagraj.linkdin.posts_service.dto.PostDto;
import com.nagraj.linkdin.posts_service.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikesController {
    private final PostLikeService postLikeService;

    @PostMapping("/{postId}")
    public ResponseEntity<Void> likePost(@PathVariable Long postId){
        postLikeService.likePost(postId, 1L);
        return ResponseEntity.noContent().build();

    }
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> disLikePost(@PathVariable Long postId){
        postLikeService.disLikePost(postId, 1L);
        return ResponseEntity.noContent().build();

    }

}
