
# @SpringBootApplication 的 exclude 没有生效的情况下

可以在 application.yml 文件中添加 spring.autoconfigure.exclude 属性

```yaml
spring:
  autoconfigure:
    exclude: xxx.classname1,xxx.classname2
```
