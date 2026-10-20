package com.nagraj.linkedin.posts_service.clients;

import com.nagraj.linkedin.posts_service.dto.PersonDto;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "connections-service", path = "/connections")
public interface ConnectionsClient {
    @GetMapping("/core/first-connections")
   List<PersonDto> getFirstConnections();


}
