package com.nagraj.linkedin.posts_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@Builder
public class PostCreatedEvent {
    Long creatorId;
    String content;
    Long postId;
}
