package com.koala.utils.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.*;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        //集群用这个
//        JedisConnectionFactory connectionFactory1 = new JedisConnectionFactory(redisSentinelConfiguration(propertyResolver.getProperty("cache.masterName"),
//                propertyResolver.getProperty("cache.host")),jedisPoolConfig());
        //单台用这个
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(jedisPoolConfig());
        connectionFactory.setHostName(propertyResolver.getProperty("cache.masterName"));
        connectionFactory.setPort(Integer.valueOf(propertyResolver.getProperty("cache.port")));
        connectionFactory.setPassword(propertyResolver.getProperty("cache.password"));
        connectionFactory.afterPropertiesSet();

        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(connectionFactory);

        return redisTemplate;

    }

    @Bean
    public ShardedJedisPool getShardedJedisPool(){
        String[] serverArray = propertyResolver.getProperty("cache.host").split(",");//获取服务器数组(这里要相信自己的输入，所以没有考虑空指针问题)
        int expireSeconds = Integer.valueOf(propertyResolver.getProperty("cache.expireSeconds"));
        String password = propertyResolver.getProperty("cache.password");
        List<JedisShardInfo> shardInfos = new ArrayList<JedisShardInfo>();
        for (String ipPort : serverArray) {
            String[] ipPortPair = ipPort.split(":");
            JedisShardInfo info = new JedisShardInfo(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim()), expireSeconds);
            info.setPassword(password);
            shardInfos.add(info);
        }
        return new ShardedJedisPool(jedisPoolConfig(), shardInfos);
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
