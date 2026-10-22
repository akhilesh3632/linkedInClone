package com.nagraj.linkedin.notification_service.consumer;

import com.nagraj.linkedin.notification_service.clients.ConnectionsClient;
import com.nagraj.linkedin.notification_service.dto.PersonDto;
import com.nagraj.linkedin.notification_service.entity.Notification;
import com.nagraj.linkedin.notification_service.repository.NotificationRepository;
import com.nagraj.linkedin.notification_service.service.SendNotification;
import com.nagraj.linkedin.posts_service.event.PostCreatedEvent;
import com.nagraj.linkedin.posts_service.event.PostLikedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostsServiceConsumer {

    private final ConnectionsClient connectionsClient;
    private final SendNotification sendNotification;

    @KafkaListener(topics = "post-created-topic",
    properties = "spring.json.value.default.type=com.nagraj.linkedin.posts_service.event.PostCreatedEvent")
    public void handlePostCreated(PostCreatedEvent postCreatedEvent){
        log.info("Sending notifications: handlePostCreated {}",postCreatedEvent);
        List<PersonDto> connections = connectionsClient.getFirstConnections(postCreatedEvent.getCreatorId());

        for(PersonDto connection: connections){
            sendNotification.send(connection.getUserId(),"your connection "+postCreatedEvent.getCreatorId()+" has created " +
                    "a post--->");
        }


    }
    @KafkaListener(topics = "post-liked-topic",
            properties = "spring.json.value.default.type=com.nagraj.linkedin.posts_service.event.PostLikedEvent")
    public void handlePostLiked(PostLikedEvent postLikedEvent){
        log.info("Sending notification: handlePostLiked {}",postLikedEvent);
        String message = String.format("Your post, %d has been liked by %d",postLikedEvent.getPostId(),
                postLikedEvent.getLikedByUserId());
        sendNotification.send(postLikedEvent.getCreatorId(),message);
    }


}
