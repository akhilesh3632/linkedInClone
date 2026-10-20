package com.nagraj.linkedin.connections_service;

import lombok.Builder;
import lombok.Data;

@Data

public class SendConnectionRequestEvent {

    private Long senderId;
    private Long receiverId;
}
