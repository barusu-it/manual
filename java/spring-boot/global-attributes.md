
## Spring 全局变量

```
<bean class="org.springframework.web.context.support.ServletContextAttributeExporter">
    <property name="attributes">
        <map>
            <entry key="appServer" value="${appServer}"/>
            <entry key="staticServer" value="${staticServer}"/>
            <entry key="cookieDomain" value="${cookieDomain}"/>
        </map>
    </property>
</bean>
```

### 静态变量注入

```
private static String path;

@Value("${path}")
private void setPath(String path){
    Class.path = path;
}
```