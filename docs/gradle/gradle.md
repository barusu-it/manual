
### show dependencies

```shell script
gradlew ${module_name}:dependencies
```

### get gradle version

```groovy
if (GradleVersion.current() >= GradleVersion.version('5.1')) {
// do some things
}
```

### implementation vs compile/api

在使用多module开发时，需要注意project(":module-name") 只会引入compile/api依赖的包，implementation不会引入

https://www.jianshu.com/p/f34c179bc9d0

### How to view gradle build script dependencies
```
gradlew buildEnvironment
```


### How to view gradle dependencies
```
gradlew -q <module_name>:dependencyInsight --dependency <group_name>
```

### How to skip test task of gradle

use -x to skip task

```
gradlew clean build -x test
```

### upgrade gradle version

```
gradlew wrapper --gradle-version=$version
```

### get system env

```
home = "$System.env.HOME"

home = System.getenv('HOME')
```