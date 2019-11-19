
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

