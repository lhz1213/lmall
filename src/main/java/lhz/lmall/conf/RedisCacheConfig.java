package lhz.lmall.conf;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@SuppressWarnings("rawtypes")
public class RedisCacheConfig {

	@SuppressWarnings("deprecation")
	@Bean
	public Jackson2JsonRedisSerializer jackson2JsonRedisSerializer() {
		@SuppressWarnings("unchecked")
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		// om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		return jackson2JsonRedisSerializer;
	}

	@Bean
	public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory,
			Jackson2JsonRedisSerializer jackson2JsonRedisSerializer) {

		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		RedisSerializer stringSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(stringSerializer);
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.setHashKeySerializer(stringSerializer);
		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
		return redisTemplate;

	}

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory,
			Jackson2JsonRedisSerializer jackson2JsonRedisSerializer) {
		@SuppressWarnings("unchecked")
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
				.serializeKeysWith(
						RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(
						RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
				.entryTtl(Duration.ofMinutes(5)).disableCachingNullValues();
		RedisCacheManager redisCacheManager = RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(config)
				.transactionAware().build();

		return redisCacheManager;
	}
}
