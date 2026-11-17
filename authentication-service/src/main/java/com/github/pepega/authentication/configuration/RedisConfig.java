package com.github.pepega.authentication.configuration;

import org.springframework.boot.security.autoconfigure.web.servlet.ConditionalOnDefaultWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, byte[]> byteArrays(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);

        RedisSerializer<byte[]> byteArraySerializer = RedisSerializer.byteArray();
        redisTemplate.setValueSerializer(byteArraySerializer);
        redisTemplate.setHashValueSerializer(byteArraySerializer);

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
