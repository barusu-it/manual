package org.springframework.cache.caffeine.redis;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;
import org.springframework.data.redis.cache.DefaultRedisCachePrefix;
import org.springframework.data.redis.cache.RedisCachePrefix;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class CaffeineRedisCacheManager extends AbstractTransactionSupportingCacheManager {

    @SuppressWarnings("rawtypes") //
    private final RedisOperations redisOperations;

    private boolean usePrefix = false;
    private RedisCachePrefix cachePrefix = new DefaultRedisCachePrefix();
    private boolean loadRemoteCachesOnStartup = false;
    private boolean dynamic = true;

    // 0 - never expire
    private long defaultExpiration = 0;
    private Map<String, Long> expires = null;

    private Set<String> configuredCacheNames;

    private final boolean cacheNullValues;

    // caffeine cache
    private final ConcurrentMap<String, Cache> caffeineCacheMap = new ConcurrentHashMap<>(16);
    private Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();

    private CacheLoader<Object, Object> cacheLoader;


    @SuppressWarnings("rawtypes")
    public CaffeineRedisCacheManager(RedisOperations redisOperations) {
        this(redisOperations, Collections.emptyList());
    }

    @SuppressWarnings("rawtypes")
    public CaffeineRedisCacheManager(RedisOperations redisOperations, Collection<String> cacheNames) {
        this(redisOperations, cacheNames, false);
    }

    @SuppressWarnings("rawtypes")
    public CaffeineRedisCacheManager(RedisOperations redisOperations, Collection<String> cacheNames, boolean cacheNullValues) {

        this.redisOperations = redisOperations;
        this.cacheNullValues = cacheNullValues;
        setCacheNames(cacheNames);
    }


    public void setCacheNames(Collection<String> cacheNames) {

        Set<String> newCacheNames = CollectionUtils.isEmpty(cacheNames) ? Collections.emptySet()
                : new HashSet<>(cacheNames);

        this.configuredCacheNames = newCacheNames;
        this.dynamic = newCacheNames.isEmpty();

    }

    public void setUsePrefix(boolean usePrefix) {
        this.usePrefix = usePrefix;
    }

    public void setCachePrefix(RedisCachePrefix cachePrefix) {
        this.cachePrefix = cachePrefix;
    }

    public void setDefaultExpiration(long defaultExpireTime) {
        this.defaultExpiration = defaultExpireTime;
    }

    public void setExpires(Map<String, Long> expires) {
        this.expires = (expires != null ? new ConcurrentHashMap<>(expires) : null);
    }

    public void setLoadRemoteCachesOnStartup(boolean loadRemoteCachesOnStartup) {
        this.loadRemoteCachesOnStartup = loadRemoteCachesOnStartup;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.cache.support.AbstractCacheManager#loadCaches()
     */
    @Override
    protected Collection<? extends Cache> loadCaches() {

        Assert.notNull(this.redisOperations, "A redis template is required in order to interact with data store");

        Set<Cache> caches = new LinkedHashSet<>(
                loadRemoteCachesOnStartup ? loadAndInitRemoteCaches() : new ArrayList<>());

        Set<String> cachesToLoad = new LinkedHashSet<>(this.configuredCacheNames);
        cachesToLoad.addAll(this.getCacheNames());

        if (!CollectionUtils.isEmpty(cachesToLoad)) {

            for (String cacheName : cachesToLoad) {
                caches.add(createCache(cacheName));
            }
        }

        return caches;
    }

    protected Collection<? extends Cache> addConfiguredCachesIfNecessary(Collection<? extends Cache> caches) {

        Assert.notNull(caches, "Caches must not be null!");

        Collection<Cache> result = new ArrayList<>(caches);

        for (String cacheName : getCacheNames()) {

            boolean configuredCacheAlreadyPresent = false;

            for (Cache cache : caches) {

                if (cache.getName().equals(cacheName)) {
                    configuredCacheAlreadyPresent = true;
                    break;
                }
            }

            if (!configuredCacheAlreadyPresent) {
                result.add(getCache(cacheName));
            }
        }

        return result;
    }

    @Deprecated
    protected Cache createAndAddCache(String cacheName) {

        Cache cache = super.getCache(cacheName);
        return cache != null ? cache : createCache(cacheName);
    }

    @Override
    protected Cache getMissingCache(String name) {
        return this.dynamic ? createCache(name) : null;
    }

    @SuppressWarnings("unchecked")
    protected CaffeineRedisCache createCache(String cacheName) {
        long expiration = computeExpiration(cacheName);
        return new CaffeineRedisCache(cacheName, (usePrefix ? cachePrefix.prefix(cacheName) : null), redisOperations, expiration,
                createNativeCaffeineCache(cacheName), cacheNullValues);
    }

    protected long computeExpiration(String name) {
        Long expiration = null;
        if (expires != null) {
            expiration = expires.get(name);
        }
        return (expiration != null ? expiration : defaultExpiration);
    }

    protected List<Cache> loadAndInitRemoteCaches() {

        List<Cache> caches = new ArrayList<>();

        try {
            Set<String> cacheNames = loadRemoteCacheKeys();
            if (!CollectionUtils.isEmpty(cacheNames)) {
                for (String cacheName : cacheNames) {
                    if (null == super.getCache(cacheName)) {
                        caches.add(createCache(cacheName));
                    }
                }
            }
        } catch (Exception ex) {
            if (log.isWarnEnabled()) {
                log.warn("Failed to initialize cache with remote cache keys.", ex);
            }
        }

        return caches;
    }

    @SuppressWarnings("unchecked")
    protected Set<String> loadRemoteCacheKeys() {
        return (Set<String>) redisOperations.execute((RedisCallback<Set<String>>) connection -> {

            // we are using the ~keys postfix as defined in RedisCache#setName
            Set<byte[]> keys = connection.keys(redisOperations.getKeySerializer().serialize("*~keys"));
            Set<String> cacheKeys = new LinkedHashSet<>();

            if (!CollectionUtils.isEmpty(keys)) {
                for (byte[] key : keys) {
                    cacheKeys.add(redisOperations.getKeySerializer().deserialize(key).toString().replace("~keys", ""));
                }
            }

            return cacheKeys;
        });
    }

    @SuppressWarnings("rawtypes")
    protected RedisOperations getRedisOperations() {
        return redisOperations;
    }

    protected RedisCachePrefix getCachePrefix() {
        return cachePrefix;
    }

    protected boolean isUsePrefix() {
        return usePrefix;
    }

    @Override
    protected Cache decorateCache(Cache cache) {

        if (isCacheAlreadyDecorated(cache)) {
            return cache;
        }

        return super.decorateCache(cache);
    }

    protected boolean isCacheAlreadyDecorated(Cache cache) {
        return isTransactionAware() && cache instanceof TransactionAwareCacheDecorator;
    }


    // caffeine methods

//    public void setCacheNames(Collection<String> cacheNames) {
//        if (cacheNames != null) {
//            for (String name : cacheNames) {
//                this.cacheMap.put(name, createCaffeineCache(name));
//            }
//            this.dynamic = false;
//        }
//        else {
//            this.dynamic = true;
//        }
//    }
//

    public void setCaffeine(Caffeine<Object, Object> caffeine) {
        Assert.notNull(caffeine, "Caffeine must not be null");
        doSetCaffeine(caffeine);
    }

    public void setCaffeineSpec(CaffeineSpec caffeineSpec) {
        doSetCaffeine(Caffeine.from(caffeineSpec));
    }

    public void setCacheSpecification(String cacheSpecification) {
        doSetCaffeine(Caffeine.from(cacheSpecification));
    }

    public void setCacheLoader(CacheLoader<Object, Object> cacheLoader) {
        if (!ObjectUtils.nullSafeEquals(this.cacheLoader, cacheLoader)) {
            this.cacheLoader = cacheLoader;
//            refreshKnownCaches();
        }
    }

    // use cacheNullValues, but this field is final
//    public void setAllowNullValues(boolean allowNullValues) {
//        if (this.allowNullValues != allowNullValues) {
//            this.allowNullValues = allowNullValues;
//            refreshKnownCaches();
//        }
//    }

//    public boolean isAllowNullValues() {
//        return this.allowNullValues;
//    }


    //
//    @Override
//    public Collection<String> getCacheNames() {
//        return Collections.unmodifiableSet(this.cacheMap.keySet());
//    }

    // use parent getCache method
//    @Override
//    public Cache getCache(String name) {
//        Cache cache = this.cacheMap.get(name);
//        if (cache == null && this.dynamic) {
//            synchronized (this.cacheMap) {
//                cache = this.cacheMap.get(name);
//                if (cache == null) {
//                    cache = createCaffeineCache(name);
//                    this.cacheMap.put(name, cache);
//                }
//            }
//        }
//        return cache;
//    }

    // use new create cache method
//    protected Cache createCaffeineCache(String name) {
//        return new CaffeineCache(name, createNativeCaffeineCache(name), isAllowNullValues());
//    }

    protected com.github.benmanes.caffeine.cache.Cache<Object, Object> createNativeCaffeineCache(String name) {
        if (this.cacheLoader != null) {
            return this.cacheBuilder.build(this.cacheLoader);
        }
        else {
            return this.cacheBuilder.build();
        }
    }

    private void doSetCaffeine(Caffeine<Object, Object> cacheBuilder) {
        if (!ObjectUtils.nullSafeEquals(this.cacheBuilder, cacheBuilder)) {
            this.cacheBuilder = cacheBuilder;
//            refreshKnownCaches();
        }
    }

    // don't refresh all caches
//    private void refreshKnownCaches() {
//        for (Map.Entry<String, Cache> entry : this.cacheMap.entrySet()) {
//            entry.setValue(createCaffeineCache(entry.getKey()));
//        }
//    }

    // new useful method

    public void clearLocal(String cacheName, Object key) {
        Cache cache = this.getCache(cacheName);
        if(cache == null) {
            return ;
        }

        CaffeineRedisCache caffeineRedisCache = (CaffeineRedisCache) cache;
        caffeineRedisCache.clearLocal(key);
    }
}
