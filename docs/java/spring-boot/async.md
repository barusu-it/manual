
### 使用说明

https://www.baeldung.com/spring-async

### 自定义 AsyncConfigurer 时，会报 'Bean 'asyncConfig' of type [it.barusu.tutorial.noteligible.AsyncConfig$$EnhancerBySpringCGLIB$$cd74405e] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)'

spring boot 版本为 2.2.1

```java
package it.barusu.tutorial.noteligible;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {


    @Override
    public Executor getAsyncExecutor() {
        log.info("loading async executor.");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.initialize();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(1000);
        executor.setKeepAliveSeconds(300);
        executor.setThreadNamePrefix("brs-executor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

}
```

这里根据下面 spring-cloud 的 issue 可以看到，需要额外在 @Configuration 或 @Bean 上添加 @Role，如下:

```java
@EnableAsync
@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class AsyncConfig implements AsyncConfigurer {
    // implement code
}
```

问题解决！

reference

https://github.com/spring-cloud/spring-cloud-sleuth/issues/1022

