package com.nagraj.linkedin.notification_service.consumer;

import com.nagraj.linkedin.connections_service.AcceptConnectionRequestEvent;
import com.nagraj.linkedin.connections_service.SendConnectionRequestEvent;
import com.nagraj.linkedin.notification_service.service.SendNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionsServiceConsumer {

    private final SendNotification sendNotification;

    @KafkaListener(topics = "send-connection-request-topic",
    properties = "spring.json.value.default.type=com.nagraj.linkedin.connections_service.SendConnectionRequestEvent")
    public void handleSendConnectionRequest(SendConnectionRequestEvent sendConnectionRequestEvent){
        log.info("handle connections: handleSendConnectionRequest: {} ",sendConnectionRequestEvent);
        String message = "You have received a connection request from user with id: %d "+sendConnectionRequestEvent.getSenderId();
        sendNotification.send(sendConnectionRequestEvent.getReceiverId(), message);

    }

    @KafkaListener(topics = "accept-connection-request-topic",
    properties = "spring.json.value.default.type=com.nagraj.linkedin.connections_service.AcceptConnectionRequestEvent")

    public void handleAcceptConnectionRequest(AcceptConnectionRequestEvent acceptConnectionRequestEvent){
        log.info("handle connections: handleAcceptConnectionRequest: {} ",acceptConnectionRequestEvent);
        String message = "Your connection request has been accepted by the user with id: %d "+acceptConnectionRequestEvent.getReceiverId();
        sendNotification.send(acceptConnectionRequestEvent.getSenderId(), message);

    }
}
