package com.nagraj.linkedin.connections_service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendConnectionRequestEvent {

    private Long senderId;
    private Long receiverId;
}
