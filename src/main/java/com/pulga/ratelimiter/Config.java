package com.pulga.ratelimiter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class Config {
    @Bean
    public LettuceConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("localhost", 6379));
    }

    @Bean
    public RedisTemplate<String, Integer> redisTemplate(){
        RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Integer.class));
        return redisTemplate;
    }
}
