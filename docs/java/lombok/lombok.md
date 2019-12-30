
### how to set variables to logback.xml

use -D

```
java -DVAR1=VAL1 -jar $JAR
```

http://logback.qos.ch/manual/configuration.html#variableSubstitution

### gradle config

```groovy
compile 'org.projectlombok:lombok:1.18.3'
```

above 1.5.x+

```groovy
compileOnly 'org.projectlombok:lombok:1.18.3'
annotationProcessor 'org.projectlombok:lombok:1.18.3'
testCompileOnly 'org.projectlombok:lombok:1.18.3'
testAnnotationProcessor 'org.projectlombok:lombok:1.18.3'
```

### reference

https://www.iteye.com/blog/takeme-2437973