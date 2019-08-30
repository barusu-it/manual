
## caffeine
### 引入依赖

```
org.springframework.boot:spring-boot-starter-cache
com.github.ben-manes.caffeine:caffeine
```

### 启用缓存

添加 @EnableCaching 注解

```
@EnableCaching
@Configuration
class CachingConfig {

...

}
```

### application配置

```yaml
spring:
  cache:
    type: CAFFEINE
  # 如不设置cache-names, 则cache会动态创建, @org.springframework.cache.caffeine.CaffeineCacheManager
  cache-names:
    - cachename1
    - cachename2
  caffeine:
    spec: maximumSize=500,expireAfterWrite=5s
```

### 缓存使用

```

@Component
class Service {

...

   @Cacheable(value="cachename1", key="#param1")
   public String getParameter(String param1) {
   ...

}

```
 
### 参考

https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-caching.html#boot-features-caching-provider-caffeine

https://blog.csdn.net/ClementAD/article/details/53009899

