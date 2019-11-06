
### 'build.plugins.plugin[io.spring.gradle.dependencymanagement.org.apache.maven.plugins:maven-antrun-plugin].dependencies.dependency.scope' for junit:junit:jar must be one of [compile, runtime, system] but is 'test'. in log4j:log4j:1.2.16

通过显示依赖 1.2.17+ 的版本可以解决

```groovy
implementation "log4j:log4j:1.2.17"
```

### springBoot plugin 设置mainClassName

```groovy
springBoot {
    mainClassName = "<classname>"
}
```

if application plugin has been applied, you can use:

```groovy
mainClassName = "<classname>"
```

### reference

https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/html/