package com.nvs.config.cache;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

@Configuration
@EnableCaching
public class RedisConfiguration {

  @Value("${spring.redis.host}")
  private String redisHost;

  @Value("${spring.redis.port}")
  private int redisPort;

  @Bean
  public LettuceConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost,
        redisPort);
    return new LettuceConnectionFactory(configuration);
  }

  @Bean
  public CacheManager cacheManager() {
    RedisCacheConfiguration defaultCacheConfig = createDefaultCacheConfig(Duration.ofMinutes(1))
        .disableCachingNullValues();

    RedisCacheManager cacheManager = RedisCacheManager.builder(redisConnectionFactory())
        .cacheDefaults(defaultCacheConfig)
        .build();

    // Optional: Wrap RedisCacheManager with LoggingCacheManager if needed
    return new LoggingCacheManager(cacheManager);
  }

  private RedisCacheConfiguration createDefaultCacheConfig(Duration duration) {
    return RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(duration)
        .serializeValuesWith(
            SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
  }
}
