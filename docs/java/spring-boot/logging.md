
### logging 乱码 (logback)

疑似与系统默认字符集不是 UTF-8 有关，只能在 logback.xml 中配置 charset 来解决

```xml
<appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
        <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        <charset>UTF-8</charset>
    </encoder>
</appender>
```

### 如何使用 spring base.xml 里的 LOG_FILE 和 LOG_PATH

需要在 application.yml 文件中指定 logging.file 或 logging.path

https://docs.spring.io/spring-boot/docs/1.3.1.RELEASE/reference/html/howto-logging.html#howto-configure-logback-for-logging

