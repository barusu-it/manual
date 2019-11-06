

主要是解决跨架构的镜像构建问题，将不同的架构的镜像打一个组，其他客户端拉取镜像时即可自动根据自身服务器的架构拉取不同的镜像

### 设置环境变量后才能使用

```shell script
# Linux、macOS

$ export DOCKER_CLI_EXPERIMENTAL=enabled

# Windows

$ set $env:DOCKER_CLI_EXPERIMENTAL=enabled

```

### 创建 manifest 列表

```
# $ docker manifest create MANIFEST_LIST MANIFEST [MANIFEST...]
$ docker manifest create username/test \
      username/x8664-test \
      username/arm64v8-test
```

当要修改一个 manifest 列表时，可以加入 -a,--amend 参数。

### 设置 manifest 列表

```
# $ docker manifest annotate [OPTIONS] MANIFEST_LIST MANIFEST
$ docker manifest annotate username/test \
      username/x8664-test \
      --os linux --arch x86_64

$ docker manifest annotate username/test \
      username/arm64v8-test \
      --os linux --arch arm64 --variant v8
```

这样就配置好了 manifest 列表。

### 查看 manifest 列表

```
$ docker manifest inspect username/test
```

### 推送 manifest 列表

最后我们可以将其推送到 Docker Hub。

```
$ docker manifest push username/test
```