package com.nagraj.linkedin.posts_service.service;

import com.nagraj.linkedin.posts_service.auth.UserContextHolder;
import com.nagraj.linkedin.posts_service.clients.ConnectionsClient;
import com.nagraj.linkedin.posts_service.dto.PersonDto;
import com.nagraj.linkedin.posts_service.event.PostCreatedEvent;
import com.nagraj.linkedin.posts_service.exception.ResourceNotFoundException;
import com.nagraj.linkedin.posts_service.dto.PostCreateRequestDto;
import com.nagraj.linkedin.posts_service.dto.PostDto;
import com.nagraj.linkedin.posts_service.entity.Post;
import com.nagraj.linkedin.posts_service.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostsService {

    private final PostsRepository postsRepository;
    private final ModelMapper modelMapper;
    private final ConnectionsClient connectionsClient;

    public final KafkaTemplate<Long, PostCreatedEvent> kafkaTemplate;


    public PostDto createPost(PostCreateRequestDto postCreateRequestDto) {
        Long userId = UserContextHolder.getCurrentUserId();
        Post post = modelMapper.map(postCreateRequestDto,Post.class);
        post.setUserId(userId);

        Post savedPost = postsRepository.save(post);

        PostCreatedEvent postCreatedEvent = PostCreatedEvent.builder()
                .postId(savedPost.getId())
                .creatorId(userId)
                .content(savedPost.getContent())
                .build();
        kafkaTemplate.send("post-created-topic",postCreatedEvent)
                .whenComplete((result,ex)->{
                    if(ex != null) {
                        log.error("Failed to publish PostCreatedEvent ",ex);
                    } else {
                        log.info(
                                "PostCreatedEvent published! Topic: {},Partition: {},Offset: {}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset()
                        );
                    }
                });

        return modelMapper.map(savedPost,PostDto.class);

    }

    @Cacheable(value = "posts",key = "#postId")
    public PostDto getPostById(Long postId) {
        log.debug("Retrieving post with ID: {}",postId);


        Post post = postsRepository.findById(postId).orElseThrow(()->
                new ResourceNotFoundException("Post not found with id: "+postId));
        return modelMapper.map(post,PostDto.class);

    }

    public List<PostDto> getAllPostsOfUser(Long userId) {
        List<Post> posts = postsRepository.findByUserId(userId);
        return posts.stream().map((element)->modelMapper.map(element,PostDto.class))
                .collect(Collectors.toList());
    }
}
