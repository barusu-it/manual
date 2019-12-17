
### dubbo telnet 调用接口

```
telnet $dubbo_service_ip ${dubbo_port:20880}
# 然后回车进入 dubbo console

# 查看服务列表
dubbo> ls

# 执行 dubbo 服务
dubbo> invoke $dubbo_service_name.$method_name($json_request)
```


https://blog.csdn.net/u013960139/article/details/73863480


### protocol config

org.apache.dubbo.config.ProtocolConfig

### java.util.concurrent.RejectedExecutionException: Thread pool is EXHAUSTED! 

dubbo thread is not enough, set dubbo.protocol.threads (default: 200)


### 基于 MDC 的 dubbo trace filter

具体代码可以参考 tutorial/spring-boot-dubbo*

https://www.jianshu.com/p/9c4ae199ae92
https://my.oschina.net/LucasZhu/blog/2046356
https://www.cnblogs.com/mumuxinfei/p/9226881.html
https://www.cnblogs.com/mumuxinfei/p/9231310.html

### reference

http://dubbo.apache.org/zh-cn/docs/user/references/xml/dubbo-protocol.html

https://github.com/dubboclub/dubbokeeper