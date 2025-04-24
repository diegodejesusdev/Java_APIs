package com.dfactory.weatherAPI.config;

import com.dfactory.weatherAPI.model.WeatherResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, WeatherResponse> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, WeatherResponse> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        ObjectMapper objectMapper = new ObjectMapper();
        Jackson2JsonRedisSerializer<WeatherResponse> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, WeatherResponse.class);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        return template;
    }
}
