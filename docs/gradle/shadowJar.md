
### Usage

```groovy
plugins {
    id 'java'
    id 'application'
    id 'com.github.johnrengelman.shadow' version '5.1.0'
}

mainClassName = "<classpath>"

shadowJar {
    archiveClassifier.set('fat')
}

```

### shadowJar在5.x+后 classifier properties 已经过时了，可以使用 archiveClassifier 属性

```groovy
shadowJar {
  archiveClassifier.set('fat')
}
```

### reference

https://imperceptiblethoughts.com/shadow/introduction/

https://stackoverflow.com/questions/55949702/how-to-specify-archive-classifier-for-shadowjar-plugin-in-gradle-5