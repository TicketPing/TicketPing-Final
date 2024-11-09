package com.ticketPing.queue_manage.infrastructure.enums;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "spring.data.redis.cluster")
public class RedisClusterProperties {

    private List<String> nodes;

}