package com.ticketPing.order.infrastructure.config;

import com.ticketPing.order.infrastructure.listener.RedisKeyExpiredListener;
import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class RedisMessageListenerConfig {

    @Bean(name = "redisMessageTaskExecutor")
    public Executor redisMessageTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(2);
        threadPoolTaskExecutor.setMaxPoolSize(4);
        return threadPoolTaskExecutor;
    }

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                                   RedisKeyExpiredListener redisKeyExpiredListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(redisKeyExpiredListener, new ChannelTopic("__keyevent@0__:expired"));
        container.setTaskExecutor(redisMessageTaskExecutor());
        return container;
    }

}