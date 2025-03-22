package com.pudding.config.redis;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.Duration;

@Configuration
public class RedisConfig {

    @Resource
    private RedisProperties redisProperties;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisProperties.getHost());
        configuration.setPort(redisProperties.getPort());
        // 可以不设置密码
        if (StrUtil.isNotEmpty(redisProperties.getPassword())) {
            configuration.setPassword(redisProperties.getPassword());
        }
        // 如果Redis启用了ACL需要用户名（Redis 6+）
        if(StrUtil.isNotEmpty(redisProperties.getUsername())){
            configuration.setUsername(redisProperties.getUsername());
        }

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(2)) // 命令超时时间
                .shutdownTimeout(Duration.ofMillis(200)) // 关闭超时时间
                .clientOptions(getClientOptions()) // 连接池配置
                .build();

        return new LettuceConnectionFactory(configuration, clientConfig);
    }

    /**
     * 连接池配置（自动读取yml中的pool配置）
     */
    private ClusterClientOptions getClientOptions() {
        return ClusterClientOptions.builder()
                .autoReconnect(true)
                .validateClusterNodeMembership(false)
                .socketOptions(SocketOptions.builder()
                        .connectTimeout(Duration.ofSeconds(5))
                        .build())
                .build();
    }

    /**
     * RedisTemplate 配置，使用 JSON 序列化
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        ObjectMapper objectMapper = new ObjectMapper();
        // 基础配置
        objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION) // 关闭默认的视图（View）包含机制
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // 禁止将日期序列化为数字时间戳
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) // 忽略未知JSON字段
                .enable(MapperFeature.USE_ANNOTATIONS) // 启用注解支持
                // 安全类型配置（生产必须！）
                .activateDefaultTyping(
                        LaissezFaireSubTypeValidator.instance, // 不限制类型
                        ObjectMapper.DefaultTyping.NON_FINAL, // 对非final类记录类型
                        JsonTypeInfo.As.PROPERTY // 类型信息作为属性存储
                )
                // 明确的日期格式配置
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                // 日期时间支持
                .registerModules(new JavaTimeModule(), new Jdk8Module());

        // 使用 GenericJackson2JsonRedisSerializer 处理 Value
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        redisTemplate.setValueSerializer(jsonSerializer);
        redisTemplate.setHashValueSerializer(jsonSerializer);

        // 初始化string的序列化方式
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        redisTemplate.setValueSerializer(stringRedisSerializer);
        // hash的value序列化方式采用jackson
        redisTemplate.setHashValueSerializer(jsonSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 配置缓存管理器
     */
    @Bean
    public CacheManager cacheManager() {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30)) // 默认缓存过期时间
                .disableCachingNullValues()         // 禁止缓存null值
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(redisConnectionFactory())
                .cacheDefaults(config)
                .transactionAware()
                .build();
    }

}
