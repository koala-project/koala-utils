package com.koala.utils.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.lang.reflect.Method;

//@Configuration
//@EnableCaching
public class CacheConfiguration implements EnvironmentAware {

    Logger logger = LoggerFactory.getLogger(CacheConfiguration.class);

    private RelaxedPropertyResolver propertyResolver = null;

    @Override
    public void setEnvironment(Environment environment) {
        propertyResolver = new RelaxedPropertyResolver(environment,"redis.");
    }


    @Bean
    JedisPoolConfig jedisPoolConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(Integer.valueOf(propertyResolver.getProperty("maxIdle")));
        config.setMaxTotal(Integer.valueOf(propertyResolver.getProperty("maxTotal")));
        config.setMinIdle(Integer.valueOf(propertyResolver.getProperty("minIdle")));
        config.setMaxWaitMillis(Long.valueOf(propertyResolver.getProperty("maxWaitMillis")));
        config.setMinEvictableIdleTimeMillis(3000);
        config.setNumTestsPerEvictionRun(3);
        config.setTimeBetweenEvictionRunsMillis(6000);
        return config;
    }

    RedisSentinelConfiguration redisSentinelConfiguration(String masterName, String...hosts){
        return new RedisSentinelConfiguration(masterName, Sets.newHashSet(hosts));
    }

    @Bean
    public RedisTemplate redisTemplate(){

        RedisTemplate redisTemplate = new RedisTemplate();

        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(redisSentinelConfiguration(propertyResolver.getProperty("cache.masterName"),
                propertyResolver.getProperty("cache.host1"),
                propertyResolver.getProperty("cache.host2"),
                propertyResolver.getProperty("cache.host3")),jedisPoolConfig());

        connectionFactory.afterPropertiesSet();

        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES,true);
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(connectionFactory);

        return redisTemplate;

    }



    @Configuration
    @EnableCaching
    static class RedisCacheConfiguration extends CachingConfigurerSupport{

        @Resource RedisTemplate redisTemplate;

        @Bean
        @ConditionalOnMissingBean
        public KeyGenerator keyGenerator(){
            return (Object target, Method method, Object... params) -> {
                StringBuilder sb = new StringBuilder();
                sb.append("[service]");
                sb.append(target.getClass().getName());
                sb.append(".");
                sb.append(method.getName());
                sb.append("(");
                for (Object obj : params) {
                    sb.append(obj.toString());
                    sb.append(",");
                }
                if (sb.lastIndexOf(",") == (sb.length() - 1)){
                    sb.delete(sb.length()-1,sb.length());
                }
                sb.append(")");
                return sb.toString();
            };
        }
    }
}
