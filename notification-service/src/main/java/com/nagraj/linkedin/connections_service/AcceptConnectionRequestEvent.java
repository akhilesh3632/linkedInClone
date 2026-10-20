package com.nagraj.linkedin.connections_service;

import lombok.Builder;
import lombok.Data;

@Data

public class AcceptConnectionRequestEvent {
    private Long senderId;
    private Long receiverId;
}
