
## deployment

### standalone

1. 使用默认 h2 存储
2. centos 7

启动服务

```
./bin/startup.sh
```


#### reference

http://ishiker.com/ArtInfo/30362.html
https://www.jianshu.com/p/12254d350543
http://dubbo.apache.org/en-us/docs/admin/ops/skywalking.html

### cluster

https://www.jianshu.com/p/4bd310850dd0
https://www.jianshu.com/p/6f3e3e989959
https://www.jianshu.com/p/8157866deb86
https://blog.csdn.net/mafei6827/article/details/80689628

## javaagent

```
java -javaagent:/path/to/agent/skywalking-agent.jar -Dskywalking.agent.service_name=$service_name \
-Dskywalking.collector.backend_service=$skywalking_server
# $skywalking_server example: 192.168.56.102:11800
```

### 注意：不要在 idea 的运行启动项里启动 skywalking agent，会报如下错误：

```
Caused by: java.lang.ClassFormatError: Duplicate interface name "org/apache/skywalking/apm/agent/core/plugin/interceptor/enhance/EnhancedInstance" 
```

## apm-toolkit-logback 使用

添加依赖

```groovy
compile 'org.apache.skywalking:apm-toolkit-logback-1.x:6.5.0'
```

logback.xml (配置 logback conversionRule，添加 TID Converter)

```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration>
    <conversionRule conversionWord="tid" converterClass="org.apache.skywalking.apm.toolkit.log.logback.v1.x.LogbackPatternConverter" />
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] [%tid] %-5level %logger{36} - %msg%n</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>
    <root level="info">
        <appender-ref ref="CONSOLE"/>
    </root>
</configuration>
```

### reference

https://www.cnblogs.com/hellxz/p/logback_skywalking_trace_id.html


## plugin development

https://github.com/apache/skywalking/blob/master/docs/en/guides/Java-Plugin-Development-Guide.md

## 竞品对比

https://skywalking.apache.org/zh/blog/2019-03-29-introduction-of-skywalking-and-simple-practice.html
