
### get gradle version

```groovy
if (GradleVersion.current() >= GradleVersion.version('5.1')) {
// do some things
}
```

### implementation vs compile/api

在使用多module开发时，需要注意project(":module-name") 只会引入compile/api依赖的包，implementation不会引入

https://www.jianshu.com/p/f34c179bc9d0