
## spring boot + resteasy

### 依赖

```groovy
ext.resteasyVersion = '3.6.2.Final'

compile "org.jboss.resteasy:resteasy-spring-boot-starter:3.0.0.Final"
compile "org.jboss.resteasy:resteasy-validator-provider-11:${resteasyVersion}"
```

### root context 配置

```java
@Component
@ApplicationPath("/resteasy-api")
public class JaxrsApplication extends Application {
}
```

### Jaxrs 接口样例

```java
@Path("/deliver")
public interface ResteasyServiceBEndpoint {

    @POST
    @Path("/oops")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Map<String, String> oops(@Valid UserRequest request);

}
```

### resteasy client 类似于 feign 的实现（非分布式模式）

```java
@Slf4j
@Configuration
public class ResteasyClientConfig {

    @Bean
    public RestClientProxyFactoryBean<ResteasyServiceBEndpoint> resteasyServiceBEndpointRestClientProxyFactoryBean(
            @Value("${base-url.service-b}") String url) {
        RestClientProxyFactoryBean<ResteasyServiceBEndpoint> restClientProxyFactoryBean = new RestClientProxyFactoryBean<>();
        restClientProxyFactoryBean.setBaseUri(URI.create(url));
        restClientProxyFactoryBean.setServiceInterface(ResteasyServiceBEndpoint.class);

        return restClientProxyFactoryBean;
    }

    @Bean
    public ResteasyServiceBEndpoint resteasyServiceBEndpointClient(
            RestClientProxyFactoryBean<ResteasyServiceBEndpoint> resteasyServiceBEndpointRestClientProxyFactoryBean)
            throws Exception {
        return resteasyServiceBEndpointRestClientProxyFactoryBean.getObject();

    }
}
```

### reference

https://github.com/resteasy/resteasy-spring-boot/blob/3.0.0.Final/sample-app/pom.xml