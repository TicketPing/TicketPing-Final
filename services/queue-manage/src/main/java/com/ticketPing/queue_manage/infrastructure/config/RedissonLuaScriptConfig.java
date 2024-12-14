package com.ticketPing.queue_manage.infrastructure.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

@Configuration
@RequiredArgsConstructor
public class RedissonLuaScriptConfig {

    private final RedissonReactiveClient redissonClient;

    @Bean
    public String saveTokenScript() throws IOException {
        return loadScript("scripts/saveTokenScript.lua");
    }

    @Bean
    public String getRankAndSizeScript() throws IOException {
        return loadScript("scripts/getRankAndSizeScript.lua");
    }

    @Bean
    public String deleteFirstTokenScript() throws IOException {
        return loadScript("scripts/deleteFirstTokenScript.lua");
    }

    private String loadScript(String scriptPath) throws IOException {
        String script = StreamUtils.copyToString(
                new ClassPathResource(scriptPath).getInputStream(),
                StandardCharsets.UTF_8
        );
        return redissonClient.getScript().scriptLoad(script).block();
    }

}
