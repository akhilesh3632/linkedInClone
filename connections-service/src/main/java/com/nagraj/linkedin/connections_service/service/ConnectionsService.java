package com.nagraj.linkedin.connections_service.service;

import com.nagraj.linkedin.connections_service.auth.UserContextHolder;
import com.nagraj.linkedin.connections_service.entity.Person;
import com.nagraj.linkedin.connections_service.event.AcceptConnectionRequestEvent;
import com.nagraj.linkedin.connections_service.event.SendConnectionRequestEvent;
import com.nagraj.linkedin.connections_service.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionsService {

    private final PersonRepository personRepository;
    private final KafkaTemplate<Long , SendConnectionRequestEvent> sendReuestKafkaTemplate;
    private final KafkaTemplate<Long , AcceptConnectionRequestEvent> acceptReuestKafkaTemplate;

    public List<Person> getFirstDegreeConnections(Long userId){
        log.info("Getting first degree connections for user with id: {}",userId);

        return personRepository.getFirstDegreeConnections(userId);
    }


    public  Boolean sendConnectionRequest(Long receiverId) {
        Long senderId = UserContextHolder.getCurrentUserId();
        log.info("Trying to send connection request, sender: {}, receiver: {}",senderId, receiverId);

        if(senderId.equals(receiverId)){
            throw new RuntimeException("Both sender and receiver are same ");
        }

        boolean alreadySentRequest = personRepository.connectionRequestExists(senderId,receiverId);
        log.info("Request exists for sender={}, receiver={}: {}",senderId,receiverId,alreadySentRequest);
        if (alreadySentRequest) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Connection request already exists"
            );
        }

        boolean alreadyConnected  = personRepository.alreadyConnected(senderId, receiverId);
        if(alreadyConnected){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Connection request already exists"
            );
        }
        personRepository.addConnectionRequest(senderId, receiverId);
        log.info("Successfully sent the connection request,sender:{}, receiver: {}",senderId,receiverId);

        SendConnectionRequestEvent sendConnectionRequestEvent = SendConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        sendReuestKafkaTemplate.send("send-connection-request-topic",sendConnectionRequestEvent);
        return true;

    }

    public  Boolean acceptConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();

        boolean connectionRequestExists = personRepository.connectionRequestExists(senderId,receiverId);
        if(!connectionRequestExists){
            throw new RuntimeException("No connection request exists to accept");
        }

        personRepository.acceptConnectionRequest(senderId, receiverId);
        log.info("Successfully accepted the connection request, sender: {}, receiver: {}",senderId,receiverId);

        AcceptConnectionRequestEvent acceptConnectionRequestEvent = AcceptConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        acceptReuestKafkaTemplate.send("accept-connection-request-topic",acceptConnectionRequestEvent);
        return true;
    }

    public Boolean rejectConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();

        boolean connectionRequestExists = personRepository.connectionRequestExists(senderId,receiverId);
        if(!connectionRequestExists){
            throw new RuntimeException("No connections request exists, cannot delete");
        }
        personRepository.rejectConnectionRequest(senderId,receiverId);
        return true;

    }
}
