package org.springframework.cache.caffeine.redis;

import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.cache.support.NullValue;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.cache.RedisCacheElement;
import org.springframework.data.redis.cache.RedisCacheKey;
import org.springframework.data.redis.connection.DecoratedRedisConnection;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.serializer.*;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Function;

@Slf4j
@SuppressWarnings("WeakerAccess")
public class CaffeineRedisCache extends AbstractValueAdaptingCache {

    @SuppressWarnings("rawtypes") //
    private final RedisOperations redisOperations;
    private final RedisCacheMetadata cacheMetadata;
    private final CacheValueAccessor cacheValueAccessor;

    // caffeine fields
//    private final String name;
    private final com.github.benmanes.caffeine.cache.Cache<Object, Object> cache;
    // caffeine fields end

    public CaffeineRedisCache(String name, byte[] prefix, RedisOperations<? extends Object, ? extends Object> redisOperations,
                              long expiration, com.github.benmanes.caffeine.cache.Cache<Object, Object> cache) {
        this(name, prefix, redisOperations, expiration, cache, false);
    }

    public CaffeineRedisCache(String name, byte[] prefix, RedisOperations<? extends Object, ? extends Object> redisOperations,
                              long expiration, com.github.benmanes.caffeine.cache.Cache<Object, Object> cache, boolean allowNullValues) {

        super(allowNullValues);

        Assert.hasText(name, "CacheName must not be null or empty!");
        // caffeine code
        Assert.notNull(cache, "Cache must not be null");
        // caffeine code end

        RedisSerializer<?> serializer = redisOperations.getValueSerializer() != null ? redisOperations.getValueSerializer()
                : new JdkSerializationRedisSerializer();

        this.cacheMetadata = new RedisCacheMetadata(name, prefix);
        this.cacheMetadata.setDefaultExpiration(expiration);
        this.redisOperations = redisOperations;
        this.cacheValueAccessor = new CacheValueAccessor(serializer);

        if (allowNullValues) {

            if (redisOperations.getValueSerializer() instanceof StringRedisSerializer
                    || redisOperations.getValueSerializer() instanceof GenericToStringSerializer
                    || redisOperations.getValueSerializer() instanceof JacksonJsonRedisSerializer
                    || redisOperations.getValueSerializer() instanceof Jackson2JsonRedisSerializer) {
                throw new IllegalArgumentException(String.format(
                        "Redis does not allow keys with null value ¯\\_(ツ)_/¯. "
                                + "The chosen %s does not support generic type handling and therefore cannot be used with allowNullValues enabled. "
                                + "Please use a different RedisSerializer or disable null value support.",
                        ClassUtils.getShortName(redisOperations.getValueSerializer().getClass())));
            }
        }

        // caffeine code
//        this.name = name;
        this.cache = cache;
    }

    public <T> T get(Object key, Class<T> type) {

        ValueWrapper wrapper = get(key);
        //noinspection unchecked
        return wrapper == null ? null : (T) wrapper.get();
    }

    @Override
    public ValueWrapper get(Object key) {
        ValueWrapper caffeineValueWrapper = getCaffeineValueWrapper(key);
        if (Objects.nonNull(caffeineValueWrapper)) {
            return caffeineValueWrapper;
        }

        ValueWrapper redisValueWrapper = getRedisValueWrapper(getRedisCacheKey(key));

        if (Objects.nonNull(redisValueWrapper)) {
            putIfAbsentCaffeineValue(key, redisValueWrapper.get());
        }

        return redisValueWrapper;
    }

    public ValueWrapper getCaffeineValueWrapper(Object key) {
        if (this.cache instanceof LoadingCache) {
            Object value = ((LoadingCache<Object, Object>) this.cache).get(key);
            return toValueWrapper(value);
        }

        Object value = lookupCaffeineValue(key);
        return toValueWrapper(value);
    }

    public RedisCacheElement getRedisValueWrapper(final RedisCacheKey cacheKey) {

        Assert.notNull(cacheKey, "CacheKey must not be null!");

        Boolean exists = (Boolean) redisOperations.execute((RedisCallback<Boolean>) connection -> connection.exists(cacheKey.getKeyBytes()));

        if (!exists) {
            return null;
        }

        byte[] bytes = doLookup(cacheKey);

        // safeguard if key gets deleted between EXISTS and GET calls.
        if (bytes == null) {
            return null;
        }

        return new RedisCacheElement(cacheKey, fromStoreValue(deserialize(bytes)));
    }

    public <T> T get(final Object key, final Callable<T> valueLoader) {
        T object = getCaffeineValue(key, valueLoader);
        return Objects.nonNull(object) ? object : getRedisValue(key, valueLoader);

    }

    public <T> T getRedisValue(final Object key, final Callable<T> valueLoader) {
        RedisCacheElement cacheElement = new RedisCacheElement(getRedisCacheKey(key),
                new StoreTranslatingCallable(valueLoader)).expireAfter(cacheMetadata.getDefaultExpiration());
        BinaryRedisCacheElement rce = new BinaryRedisCacheElement(cacheElement, cacheValueAccessor);

        ValueWrapper val = get(key);
        if (val != null) {
            //noinspection unchecked
            return (T) val.get();
        }

        RedisWriteThroughCallback callback = new RedisWriteThroughCallback(rce, cacheMetadata);

        try {
            byte[] result = (byte[]) redisOperations.execute(callback);
            //noinspection unchecked
            return (T) (result == null ? null : fromStoreValue(cacheValueAccessor.deserializeIfNecessary(result)));
        } catch (RuntimeException e) {
            throw CacheValueRetrievalExceptionFactory.INSTANCE.create(key, valueLoader, e);
        }
    }

    public <T> T getCaffeineValue(Object key, final Callable<T> valueLoader) {
        //noinspection unchecked
        return (T) fromStoreValue(this.cache.get(key, new LoadFunction(valueLoader)));
    }

    @Override
    public void put(final Object key, final Object value) {

        putCaffeineValue(key, value);
        putRedisValue(key, value);
    }

    public void putRedisValue(final Object key, final Object value) {
        put(new RedisCacheElement(getRedisCacheKey(key), toStoreValue(value))
                .expireAfter(cacheMetadata.getDefaultExpiration()));
    }


    public void putCaffeineValue(Object key, Object value) {
        this.cache.put(key, toStoreValue(value));
    }

    @Override
    protected Object fromStoreValue(Object storeValue) {

        // we need this override for the GenericJackson2JsonRedisSerializer support.
        if (isAllowNullValues() && storeValue instanceof NullValue) {
            return null;
        }

        return super.fromStoreValue(storeValue);
    }

    public void put(RedisCacheElement element) {

        Assert.notNull(element, "Element must not be null!");

        redisOperations
                .execute(new RedisCachePutCallback(new BinaryRedisCacheElement(element, cacheValueAccessor), cacheMetadata));
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, final Object value) {

        ValueWrapper caffeineValueWrapper = putIfAbsentCaffeineValue(key, value);
        putIfAbsentRedisValue(key, value);
        return caffeineValueWrapper;
    }

    public ValueWrapper putIfAbsentRedisValue(Object key, final Object value) {

        return putIfAbsentRedisValue(new RedisCacheElement(getRedisCacheKey(key), toStoreValue(value))
                .expireAfter(cacheMetadata.getDefaultExpiration()));
    }

    public ValueWrapper putIfAbsentCaffeineValue(Object key, final Object value) {
        PutIfAbsentFunction callable = new PutIfAbsentFunction(value);
        Object result = this.cache.get(key, callable);
        return (callable.called ? null : toValueWrapper(result));
    }

    public ValueWrapper putIfAbsentRedisValue(RedisCacheElement element) {

        Assert.notNull(element, "Element must not be null!");

        new RedisCachePutIfAbsentCallback(new BinaryRedisCacheElement(element, cacheValueAccessor), cacheMetadata);

        return toWrapper(cacheValueAccessor.deserializeIfNecessary((byte[]) redisOperations.execute(
                new RedisCachePutIfAbsentCallback(new BinaryRedisCacheElement(element, cacheValueAccessor), cacheMetadata))));
    }

    public void evict(Object key) {
        evictCaffeineValue(key);
        evictRedisValue(key);
    }

    public void evictRedisValue(Object key) {
        evict(new RedisCacheElement(getRedisCacheKey(key), null));
    }

    public void evictCaffeineValue(Object key) {
        this.cache.invalidate(key);
    }

    public void evict(final RedisCacheElement element) {

        Assert.notNull(element, "Element must not be null!");
        redisOperations
                .execute(new RedisCacheEvictCallback(new BinaryRedisCacheElement(element, cacheValueAccessor), cacheMetadata));
    }

    public void clear() {
        clearCaffeine();
        clearRedis();
    }

    public void clearRedis() {
        redisOperations.execute(cacheMetadata.usesKeyPrefix() ? new RedisCacheCleanByPrefixCallback(cacheMetadata)
                : new RedisCacheCleanByKeysCallback(cacheMetadata));
    }

    public void clearCaffeine() {
        this.cache.invalidateAll();
    }

    // use redis name instead of caffeine cache name
    public String getName() {
        return cacheMetadata.getCacheName();
    }

    // use redis native operations instead of caffeine cache
    public Object getNativeCache() {
        return redisOperations;
    }

    private ValueWrapper toWrapper(Object value) {
        return (value != null ? new SimpleValueWrapper(value) : null);
    }

    @Override
    protected Object lookup(Object key) {
        Object value = lookupCaffeineValue(key);
        if (Objects.nonNull(value)) {
            return value;
        }

        value = lookupRedisValue(key);
        if (Objects.nonNull(value)) {
            putIfAbsentCaffeineValue(key, value);
        }

        return value;
    }

    protected Object lookupRedisValue(Object key) {
        return deserialize(doLookup(key));
    }

    protected Object lookupCaffeineValue(Object key) {
        return this.cache.getIfPresent(key);
    }

    private byte[] doLookup(Object key) {

        RedisCacheKey cacheKey = key instanceof RedisCacheKey ? (RedisCacheKey) key : getRedisCacheKey(key);

        return (byte[]) redisOperations.execute(new AbstractRedisCacheCallback<byte[]>(
                new BinaryRedisCacheElement(new RedisCacheElement(cacheKey, null), cacheValueAccessor), cacheMetadata) {

            @Override
            public byte[] doInRedis(BinaryRedisCacheElement element, RedisConnection connection) throws DataAccessException {
                return connection.get(element.getKeyBytes());
            }
        });
    }

    private Object deserialize(byte[] bytes) {
        return bytes == null ? null : cacheValueAccessor.deserializeIfNecessary(bytes);
    }

    private RedisCacheKey getRedisCacheKey(Object key) {
        return new RedisCacheKey(key).usePrefix(this.cacheMetadata.getKeyPrefix())
                .withKeySerializer(redisOperations.getKeySerializer());
    }

    private class StoreTranslatingCallable implements Callable<Object> {

        private Callable<?> valueLoader;

        public StoreTranslatingCallable(Callable<?> valueLoader) {
            this.valueLoader = valueLoader;
        }

        @Override
        public Object call() throws Exception {
            return toStoreValue(valueLoader.call());
        }
    }

    static class RedisCacheMetadata {

        private final String cacheName;
        private final byte[] keyPrefix;
        private final byte[] setOfKnownKeys;
        private final byte[] cacheLockName;
        private long defaultExpiration = 0;

        public RedisCacheMetadata(String cacheName, byte[] keyPrefix) {

            Assert.hasText(cacheName, "CacheName must not be null or empty!");
            this.cacheName = cacheName;
            this.keyPrefix = keyPrefix;

            StringRedisSerializer stringSerializer = new StringRedisSerializer();

            // name of the set holding the keys
            this.setOfKnownKeys = usesKeyPrefix() ? new byte[]{} : stringSerializer.serialize(cacheName + "~keys");
            this.cacheLockName = stringSerializer.serialize(cacheName + "~lock");
        }

        public boolean usesKeyPrefix() {
            return (keyPrefix != null && keyPrefix.length > 0);
        }

        public byte[] getKeyPrefix() {
            return this.keyPrefix;
        }

        public byte[] getSetOfKnownKeysKey() {
            return setOfKnownKeys;
        }

        public byte[] getCacheLockKey() {
            return cacheLockName;
        }

        public String getCacheName() {
            return cacheName;
        }

        public void setDefaultExpiration(long seconds) {
            this.defaultExpiration = seconds;
        }

        public long getDefaultExpiration() {
            return defaultExpiration;
        }

    }

    static class CacheValueAccessor {

        @SuppressWarnings("rawtypes") //
        private final RedisSerializer valueSerializer;

        @SuppressWarnings("rawtypes")
        CacheValueAccessor(RedisSerializer valueRedisSerializer) {
            valueSerializer = valueRedisSerializer;
        }

        byte[] convertToBytesIfNecessary(Object value) {

            if (value == null) {
                return new byte[0];
            }

            if (valueSerializer == null && value instanceof byte[]) {
                return (byte[]) value;
            }

            return valueSerializer.serialize(value);
        }

        Object deserializeIfNecessary(byte[] value) {

            if (valueSerializer != null) {
                return valueSerializer.deserialize(value);
            }

            return value;
        }
    }

    static class BinaryRedisCacheElement extends RedisCacheElement {

        private byte[] keyBytes;
        private byte[] valueBytes;
        private RedisCacheElement element;
        private boolean lazyLoad;
        private CacheValueAccessor accessor;

        public BinaryRedisCacheElement(RedisCacheElement element, CacheValueAccessor accessor) {

            super(element.getKey(), element.get());
            this.element = element;
            this.keyBytes = element.getKeyBytes();
            this.accessor = accessor;

            lazyLoad = element.get() instanceof Callable;
            this.valueBytes = lazyLoad ? null : accessor.convertToBytesIfNecessary(element.get());
        }

        @Override
        public byte[] getKeyBytes() {
            return keyBytes;
        }

        public long getTimeToLive() {
            return element.getTimeToLive();
        }

        public boolean hasKeyPrefix() {
            return element.hasKeyPrefix();
        }

        public boolean isEternal() {
            return element.isEternal();
        }

        public RedisCacheElement expireAfter(long seconds) {
            return element.expireAfter(seconds);
        }

        @Override
        public byte[] get() {

            if (lazyLoad && valueBytes == null) {
                try {
                    valueBytes = accessor.convertToBytesIfNecessary(((Callable<?>) element.get()).call());
                } catch (Exception e) {
                    throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e.getMessage(), e);
                }
            }
            return valueBytes;
        }
    }

    static abstract class AbstractRedisCacheCallback<T> implements RedisCallback<T> {

        private static long WAIT_FOR_LOCK_TIMEOUT = 300;
        private final BinaryRedisCacheElement element;
        private final RedisCacheMetadata cacheMetadata;

        public AbstractRedisCacheCallback(BinaryRedisCacheElement element, RedisCacheMetadata metadata) {
            this.element = element;
            this.cacheMetadata = metadata;
        }

        @Override
        public T doInRedis(RedisConnection connection) throws DataAccessException {
            waitForLock(connection);
            return doInRedis(element, connection);
        }

        public abstract T doInRedis(BinaryRedisCacheElement element, RedisConnection connection) throws DataAccessException;

        protected void processKeyExpiration(RedisCacheElement element, RedisConnection connection) {
            if (!element.isEternal()) {
                connection.expire(element.getKeyBytes(), element.getTimeToLive());
            }
        }

        protected void maintainKnownKeys(RedisCacheElement element, RedisConnection connection) {

            if (!element.hasKeyPrefix()) {

                connection.zAdd(cacheMetadata.getSetOfKnownKeysKey(), 0, element.getKeyBytes());

                if (!element.isEternal()) {
                    connection.expire(cacheMetadata.getSetOfKnownKeysKey(), element.getTimeToLive());
                }
            }
        }

        protected void cleanKnownKeys(RedisCacheElement element, RedisConnection connection) {

            if (!element.hasKeyPrefix()) {
                connection.zRem(cacheMetadata.getSetOfKnownKeysKey(), element.getKeyBytes());
            }
        }

        protected boolean waitForLock(RedisConnection connection) {

            boolean retry;
            boolean foundLock = false;
            do {
                retry = false;
                if (connection.exists(cacheMetadata.getCacheLockKey())) {
                    foundLock = true;
                    try {
                        Thread.sleep(WAIT_FOR_LOCK_TIMEOUT);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    retry = true;
                }
            } while (retry);

            return foundLock;
        }

        protected void lock(RedisConnection connection) {
            waitForLock(connection);
            connection.set(cacheMetadata.getCacheLockKey(), "locked".getBytes());
        }

        protected void unlock(RedisConnection connection) {
            connection.del(cacheMetadata.getCacheLockKey());
        }
    }

    static abstract class LockingRedisCacheCallback<T> implements RedisCallback<T> {

        private final RedisCacheMetadata metadata;

        public LockingRedisCacheCallback(RedisCacheMetadata metadata) {
            this.metadata = metadata;
        }

        @Override
        public T doInRedis(RedisConnection connection) throws DataAccessException {

            if (connection.exists(metadata.getCacheLockKey())) {
                return null;
            }
            try {
                connection.set(metadata.getCacheLockKey(), metadata.getCacheLockKey());
                return doInLock(connection);
            } finally {
                connection.del(metadata.getCacheLockKey());
            }
        }

        public abstract T doInLock(RedisConnection connection);
    }

    static class RedisCacheCleanByKeysCallback extends LockingRedisCacheCallback<Void> {

        private static final int PAGE_SIZE = 128;
        private final RedisCacheMetadata metadata;

        RedisCacheCleanByKeysCallback(RedisCacheMetadata metadata) {
            super(metadata);
            this.metadata = metadata;
        }

        @Override
        public Void doInLock(RedisConnection connection) {

            int offset = 0;
            boolean finished;

            do {
                // need to paginate the keys
                Set<byte[]> keys = connection.zRange(metadata.getSetOfKnownKeysKey(), (offset) * PAGE_SIZE,
                        (offset + 1) * PAGE_SIZE - 1);
                finished = keys.size() < PAGE_SIZE;
                offset++;
                if (!keys.isEmpty()) {
                    connection.del(keys.toArray(new byte[keys.size()][]));
                }
            } while (!finished);

            connection.del(metadata.getSetOfKnownKeysKey());
            return null;
        }
    }

    static class RedisCacheCleanByPrefixCallback extends LockingRedisCacheCallback<Void> {

        private static final byte[] REMOVE_KEYS_BY_PATTERN_LUA = new StringRedisSerializer().serialize(
                "local keys = redis.call('KEYS', ARGV[1]); local keysCount = table.getn(keys); if(keysCount > 0) then for _, key in ipairs(keys) do redis.call('del', key); end; end; return keysCount;");
        private static final byte[] WILD_CARD = new StringRedisSerializer().serialize("*");
        private final RedisCacheMetadata metadata;

        public RedisCacheCleanByPrefixCallback(RedisCacheMetadata metadata) {
            super(metadata);
            this.metadata = metadata;
        }

        @Override
        public Void doInLock(RedisConnection connection) throws DataAccessException {

            byte[] prefixToUse = Arrays.copyOf(metadata.getKeyPrefix(), metadata.getKeyPrefix().length + WILD_CARD.length);
            System.arraycopy(WILD_CARD, 0, prefixToUse, metadata.getKeyPrefix().length, WILD_CARD.length);

            if (isClusterConnection(connection)) {

                // load keys to the client because currently Redis Cluster connections do not allow eval of lua scripts.
                Set<byte[]> keys = connection.keys(prefixToUse);
                if (!keys.isEmpty()) {
                    connection.del(keys.toArray(new byte[keys.size()][]));
                }
            } else {
                connection.eval(REMOVE_KEYS_BY_PATTERN_LUA, ReturnType.INTEGER, 0, prefixToUse);
            }

            return null;
        }
    }

    static class RedisCacheEvictCallback extends AbstractRedisCacheCallback<Void> {

        public RedisCacheEvictCallback(BinaryRedisCacheElement element, RedisCacheMetadata metadata) {
            super(element, metadata);
        }

        @Override
        public Void doInRedis(BinaryRedisCacheElement element, RedisConnection connection) throws DataAccessException {

            connection.del(element.getKeyBytes());
            cleanKnownKeys(element, connection);
            return null;
        }
    }

    static class RedisCachePutCallback extends AbstractRedisCacheCallback<Void> {

        public RedisCachePutCallback(BinaryRedisCacheElement element, RedisCacheMetadata metadata) {

            super(element, metadata);
        }

        @Override
        public Void doInRedis(BinaryRedisCacheElement element, RedisConnection connection) throws DataAccessException {

            if (!isClusterConnection(connection)) {
                connection.multi();
            }

            if (element.get().length == 0) {
                connection.del(element.getKeyBytes());
            } else {
                connection.set(element.getKeyBytes(), element.get());

                processKeyExpiration(element, connection);
                maintainKnownKeys(element, connection);
            }

            if (!isClusterConnection(connection)) {
                connection.exec();
            }
            return null;
        }
    }

    static class RedisCachePutIfAbsentCallback extends AbstractRedisCacheCallback<byte[]> {

        public RedisCachePutIfAbsentCallback(BinaryRedisCacheElement element, RedisCacheMetadata metadata) {
            super(element, metadata);
        }

        @Override
        public byte[] doInRedis(BinaryRedisCacheElement element, RedisConnection connection) throws DataAccessException {

            waitForLock(connection);

            byte[] keyBytes = element.getKeyBytes();
            byte[] value = element.get();

            if (!connection.setNX(keyBytes, value)) {
                return connection.get(keyBytes);
            }

            maintainKnownKeys(element, connection);
            processKeyExpiration(element, connection);

            return null;
        }
    }

    static class RedisWriteThroughCallback extends AbstractRedisCacheCallback<byte[]> {

        public RedisWriteThroughCallback(BinaryRedisCacheElement element, RedisCacheMetadata metadata) {
            super(element, metadata);
        }

        @Override
        public byte[] doInRedis(BinaryRedisCacheElement element, RedisConnection connection) throws DataAccessException {

            try {

                lock(connection);

                try {

                    byte[] value = connection.get(element.getKeyBytes());

                    if (value != null) {
                        return value;
                    }

                    if (!isClusterConnection(connection)) {

                        connection.watch(element.getKeyBytes());
                        connection.multi();
                    }

                    value = element.get();

                    if (value.length == 0) {
                        connection.del(element.getKeyBytes());
                    } else {
                        connection.set(element.getKeyBytes(), value);
                        processKeyExpiration(element, connection);
                        maintainKnownKeys(element, connection);
                    }

                    if (!isClusterConnection(connection)) {
                        connection.exec();
                    }

                    return value;
                } catch (RuntimeException e) {
                    if (!isClusterConnection(connection)) {
                        connection.discard();
                    }
                    throw e;
                }
            } finally {
                unlock(connection);
            }
        }
    }

    private enum CacheValueRetrievalExceptionFactory {

        INSTANCE;

        private static boolean isSpring43;

        static {
            isSpring43 = ClassUtils.isPresent("org.springframework.cache.Cache$ValueRetrievalException",
                    ClassUtils.getDefaultClassLoader());
        }

        public RuntimeException create(Object key, Callable<?> valueLoader, Throwable cause) {

            if (isSpring43) {
                try {
                    Class<?> execption = ClassUtils.forName("org.springframework.cache.Cache$ValueRetrievalException",
                            this.getClass().getClassLoader());
                    Constructor<?> c = ClassUtils.getConstructorIfAvailable(execption, Object.class, Callable.class,
                            Throwable.class);
                    return (RuntimeException) c.newInstance(key, valueLoader, cause);
                } catch (Exception ex) {
                    // ignore
                }
            }

            return new RedisSystemException(
                    String.format("Value for key '%s' could not be loaded using '%s'.", key, valueLoader), cause);
        }
    }

    private static boolean isClusterConnection(RedisConnection connection) {

        while (connection instanceof DecoratedRedisConnection) {
            connection = ((DecoratedRedisConnection) connection).getDelegate();
        }

        return connection instanceof RedisClusterConnection;
    }


    // caffeine methods below

    private class PutIfAbsentFunction implements Function<Object, Object> {

        private final Object value;

        private boolean called;

        public PutIfAbsentFunction(Object value) {
            this.value = value;
        }

        @Override
        public Object apply(Object key) {
            this.called = true;
            return toStoreValue(this.value);
        }
    }

    private class LoadFunction implements Function<Object, Object> {

        private final Callable<?> valueLoader;

        public LoadFunction(Callable<?> valueLoader) {
            this.valueLoader = valueLoader;
        }

        @Override
        public Object apply(Object o) {
            try {
                return toStoreValue(valueLoader.call());
            } catch (Exception ex) {
                throw new ValueRetrievalException(o, valueLoader, ex);
            }
        }
    }

    // new local clear method
    public void clearLocal(Object key) {
        log.debug("clear local cache, the key is : {}", key);
        if(key == null) {
            cache.invalidateAll();
        } else {
            cache.invalidate(key);
        }
    }
}
