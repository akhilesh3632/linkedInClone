package com.nagraj.linkedin.connections_service.event;

import lombok.AllArgsConstructor;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class SendConnectionRequestEvent {

    private Long senderId;
    private Long receiverId;
}
