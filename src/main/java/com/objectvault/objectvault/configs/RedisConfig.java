/* (C) 2024 */
package com.objectvault.objectvault.configs;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;
import io.lettuce.core.resource.DefaultClientResources;
import io.lettuce.core.resource.Delay;
import io.lettuce.core.resource.DirContextDnsResolver;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
@Slf4j
public class RedisConfig implements CachingConfigurer {
  private String connectTimeout = "3000";
  private String commandTimeout = "3500";

  @Value("${redis.host}")
  private List<String> host;

  @Bean
  public DefaultClientResources defaultClientResources() {
    return DefaultClientResources.builder()
        .reconnectDelay(
            Delay.fullJitter(
                Duration.ofMillis(100), Duration.ofSeconds(10), 100, TimeUnit.MILLISECONDS))
        .dnsResolver(new DirContextDnsResolver())
        .build();
  }

  @Bean
  LettuceClientConfigurationBuilderCustomizer lettuceClientConfigurationBuilderCustomizer() {
    ClusterTopologyRefreshOptions clusterTopologyRefreshOptions =
        ClusterTopologyRefreshOptions.builder()
            .dynamicRefreshSources(true)
            .enablePeriodicRefresh(true)
            .closeStaleConnections(true)
            .enablePeriodicRefresh()
            .enableAdaptiveRefreshTrigger(
                ClusterTopologyRefreshOptions.RefreshTrigger.PERSISTENT_RECONNECTS)
            .build();

    SocketOptions socketOptions =
        SocketOptions.builder()
            .keepAlive(true)
            .connectTimeout(Duration.ofMillis(Long.parseLong(connectTimeout)))
            .build();

    ClientOptions clientOptions =
        ClusterClientOptions.builder()
            .socketOptions(socketOptions)
            .autoReconnect(true)
            .timeoutOptions(TimeoutOptions.create())
            .nodeFilter(
                it ->
                    !(it.is(RedisClusterNode.NodeFlag.FAIL)
                        || it.is(RedisClusterNode.NodeFlag.EVENTUAL_FAIL)
                        || it.is(RedisClusterNode.NodeFlag.HANDSHAKE)
                        || it.is(RedisClusterNode.NodeFlag.NOADDR)))
            .validateClusterNodeMembership(false)
            .topologyRefreshOptions(clusterTopologyRefreshOptions)
            .build();
    return builder ->
        builder
            .commandTimeout(Duration.ofMillis(Long.parseLong(commandTimeout)))
            .clientOptions(clientOptions)
            .readFrom(ReadFrom.REPLICA_PREFERRED);
  }

  @Bean
  public RedisConnectionFactory redisConnectionFactory(
      LettuceClientConfigurationBuilderCustomizer customizer,
      DefaultClientResources defaultClientResources) {
    final RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(host);
    LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder =
        LettucePoolingClientConfiguration.builder();
    customizer.customize(builder);

    builder.clientResources(defaultClientResources);

    final LettucePoolingClientConfiguration lettucePoolingClientConfiguration = builder.build();

    final LettuceConnectionFactory lettuceConnectionFactory =
        new LettuceConnectionFactory(redisClusterConfiguration, lettucePoolingClientConfiguration);

    lettuceConnectionFactory.afterPropertiesSet();

    return lettuceConnectionFactory;
  }

  @Bean
  public RedisCacheManager defaultCacheManager(RedisConnectionFactory redisConnectionFactory) {
    RedisCacheConfiguration config =
        RedisCacheConfiguration.defaultCacheConfig()
            .disableCachingNullValues()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new GenericJackson2JsonRedisSerializer()));

    RedisCacheManager cacheManager =
        RedisCacheManager.builder(redisConnectionFactory)
            .cacheWriter(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
            .withCacheConfiguration("listFilesCache", config)
            .cacheDefaults(config)
            .build();

    cacheManager.setTransactionAware(false); // Depending on your requirement

    return cacheManager;
  }

  @Override
  public CacheErrorHandler errorHandler() {
    return new CacheErrorHandler() {
      @Override
      public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        log.warn("Cache Get Error for key {}: {}", key, exception.getMessage());
      }

      @Override
      public void handleCachePutError(
          RuntimeException exception, Cache cache, Object key, Object value) {
        log.warn("Cache Put Error for key {}: {}", key, exception.getMessage());
      }

      @Override
      public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        log.warn("Cache Evict Error for key {}: {}", key, exception.getMessage());
      }

      @Override
      public void handleCacheClearError(RuntimeException exception, Cache cache) {
        log.warn("Cache Clear Error: {}", exception.getMessage());
      }
    };
  }
}
