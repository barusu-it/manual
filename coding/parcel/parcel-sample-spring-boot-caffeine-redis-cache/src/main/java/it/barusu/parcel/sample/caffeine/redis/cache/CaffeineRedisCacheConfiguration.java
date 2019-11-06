package it.barusu.parcel.sample.caffeine.redis.cache;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.RedisCacheExtraProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.caffeine.redis.CaffeineRedisCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.util.List;

@Configuration
//@AutoConfigureBefore({RedisCacheConfiguration.class, CaffeineCacheConfiguration.class})
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnBean(RedisTemplate.class)
//@ConditionalOnMissingBean(CacheManager.class)
@EnableConfigurationProperties(CacheProperties.class)
@ConditionalOnClass({Caffeine.class, CaffeineCacheManager.class})
public class CaffeineRedisCacheConfiguration {

    private final CacheProperties cacheProperties;
    private final RedisCacheExtraProperties redisCacheExtraProperties;
    private final Caffeine<Object, Object> caffeine;
    private final CaffeineSpec caffeineSpec;
    private final CacheLoader<Object, Object> cacheLoader;

    public CaffeineRedisCacheConfiguration(CacheProperties cacheProperties,
                                           RedisCacheExtraProperties redisCacheExtraProperties,
                                           ObjectProvider<Caffeine<Object, Object>> caffeine,
                                           ObjectProvider<CaffeineSpec> caffeineSpec,
                                           ObjectProvider<CacheLoader<Object, Object>> cacheLoader) {
        this.cacheProperties = cacheProperties;
        this.redisCacheExtraProperties = redisCacheExtraProperties;
        this.caffeine = caffeine.getIfAvailable();
        this.caffeineSpec = caffeineSpec.getIfAvailable();
        this.cacheLoader = cacheLoader.getIfAvailable();
    }

    @Bean
    public CaffeineRedisCacheManager cacheManager(RedisTemplate<Object, Object> redisTemplate) {
        CaffeineRedisCacheManager cacheManager = new CaffeineRedisCacheManager(redisTemplate);
        cacheManager.setUsePrefix(true);
        List<String> cacheNames = this.cacheProperties.getCacheNames();
        if (!cacheNames.isEmpty()) {
            cacheManager.setCacheNames(cacheNames);
        }

        setCacheBuilder(cacheManager);
        if (this.cacheLoader != null) {
            cacheManager.setCacheLoader(this.cacheLoader);
        }

        if (redisCacheExtraProperties.getExpires() != null) {
            cacheManager.setExpires(redisCacheExtraProperties.getExpires());
        }

        return cacheManager;
    }

    private void setCacheBuilder(CaffeineRedisCacheManager cacheManager) {
        String specification = this.cacheProperties.getCaffeine().getSpec();
        if (StringUtils.hasText(specification)) {
            cacheManager.setCacheSpecification(specification);
        } else if (this.caffeineSpec != null) {
            cacheManager.setCaffeineSpec(this.caffeineSpec);
        } else if (this.caffeine != null) {
            cacheManager.setCaffeine(this.caffeine);
        }
    }

}
