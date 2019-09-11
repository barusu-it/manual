
### 获取镜像

```shell script
docker pull <repository_name><:tag>
```

### 运行镜像

```shell script
docker run -it --rm ubuntu:18.04 bash
# -it: -i 交互式操作, -t 终端
# --rm 容器退出后删除镜像

docker run --name webserver -d -p 80:80 nginx
```

容器中可以运行一些网络应用，要让外部也可以访问这些应用，可以通过 -P 或 -p 参数来指定端口映射。

当使用 -P 标记时，Docker 会随机映射一个 49000~49900 的端口到内部容器开放的网络端口。

可以使用 udp 标记来指定 udp 端口

```shell script
docker run -d -p 127.0.0.1:5000:5000/udp training/webapp python app.py
```

-p 标记可以多次使用来绑定多个端口

```shell script
docker run -d \
    -p 5000:5000 \
    -p 3000:80 \
    training/webapp \
    python app.py
```

-h HOSTNAME 或者 --hostname=HOSTNAME 设定容器的主机名，它会被写到容器内的 /etc/hostname 和 /etc/hosts。但它在容器外部看不到，既不会在 docker container ls 中显示，也不会在其他的容器的 /etc/hosts 看到。

--dns=IP_ADDRESS 添加 DNS 服务器到容器的 /etc/resolv.conf 中，让容器用这个服务器来解析所有不在 /etc/hosts 中的主机名。

--dns-search=DOMAIN 设定容器的搜索域，当设定搜索域为 .example.com 时，在搜索一个名为 host 的主机时，DNS 不仅搜索 host，还会搜索 host.example.com。

### 查看映射端口配置

使用 docker port 来查看当前映射的端口配置，也可以查看到绑定的地址

```shell script
$ docker port nostalgic_morse 5000
127.0.0.1:49155.
```




### 镜像差异

```shell script
docker diff <container_id/container_name>
```

### commit镜像

```shell script
docker commit --author <author> --message "comment" <container> <image_name>:<tag>
```

**慎用docker commit，主要是镜像运行后有多个其他无关的文件会修改，最终导致镜像臃肿。**

请使用Dockerfile构建新镜像

### docker history

```shell script
docker history <image>:<tag>
```


### 构建镜像

```shell script
docker build -t <image>:<tag> <context_path>
# 这里是上下文路径

# e.g.
docker build -t nignx:v3 .

docker build -t ubuntu:18.04 -f ../Dockerfile .

# build from git
docker build https://github.com/twang2218/gitlab-ce-zh.git#:11.1

# build from tar.gz
docker build http://server/context.tar.gz

docker build - < context.tar.gz

# build from sysio
docker build - < Dockerfile
# or
cat Dockerfile | docker build -
```

.dockerignore 文件可以过滤不需要传输到docker daemon的文件

-f <dockerfile_path> 指定dockerfile路径

### 显示镜像

```shell script
# show image list
docker image ls
# 如果ls出来存在镜像名和tag为<none>的情况，一般为docker pull/ docker build 产生的新镜像与旧镜像同名导致，
# 此类镜像被称为dangling image

docker image ls -a
# 此操作可以显示中层镜像，是其他镜像的依赖，不用删除

docker image ls <repository_name><:tag>

docker image ls -f since=ubuntu:18.04
# -f 即 filter
# 显示该镜像之后创建的镜像

docker image ls -f label=<label> 
# 显示label为<label>的镜像

docker image ls -q
# 显示已退出的镜像

# 显示格式
docker image ls --format "{{.ID}}: {{.Repository}}"
5f515359c7f8: redis
05a60462f8ba: nginx
fe9198c04d62: mongo
00285df0df87: <none>

docker image ls --format "table {{.ID}}\t{{.Repository}}\t{{.Tag}}"
IMAGE ID            REPOSITORY          TAG
5f515359c7f8        redis               latest
05a60462f8ba        nginx               latest
fe9198c04d62        mongo               3.2
00285df0df87        <none>              <none>

docker image ls --digest
# 显示摘要sha256

```

### 删除镜像

```shell script
docker image rm

docker image rm <id前三位以上>
docker image rm <container_name>
docker image rm node@sha256:<digest_value>
# 根据摘要删除

# 结合image ls命令来删除
docker image rm $(docker image ls -q redis)
docker image rm $(docker image ls -q -f before=mongo:3.2)
```

### 查看 docker 信息

```shell script
docker info
```

### 进入容器

```shell script
docker exec -it <container_id> /bin/sh

docker exec -it <container_id> bash

```

### 清理docker磁盘

```shell script
docker system prune
docker system prune -a -f --volumes
```

### 查看docker空间大小

```shell script
docker system df -v
```

### docker error: no space left on device

由于此次问题定位时，容器已经重启，部分日志已删除（为了腾空间），故此次问题的原因根据google搜索后，可能有2种情况导致：

1. 该container的devicemapper磁盘空间满，导致dubbo registry file 无法更新
2. 该container的inode满，导致无法更新

### docker import

格式：docker import [选项] <文件>|<URL>|- [<仓库名>[:<标签>]]

压缩包可以是本地文件、远程 Web 文件，甚至是从标准输入中得到。压缩包将会在镜像 / 目录展开，并直接作为镜像第一层提交。

```shell script
docker import \
    http://download.openvz.org/template/precreated/ubuntu-16.04-x86_64.tar.gz \
    openvz/ubuntu:16.04

Downloading from http://download.openvz.org/template/precreated/ubuntu-16.04-x86_64.tar.gz
sha256:412b8fc3e3f786dca0197834a698932b9c51b69bd8cf49e100c35d38c9879213
```

### docker history

查看镜像历史

```shell script
docker history openvz/ubuntu:16.04
IMAGE               CREATED              CREATED BY          SIZE                COMMENT
f477a6e18e98        About a minute ago                       214.9 MB            Imported from http://download.openvz.org/template/precreated/ubuntu-16.04-x86_64.tar.gz
``` 

### docker save & docker load

保存镜像到文件 & 加载镜像

该方法不推荐，建议使用dockerHub或私有registry

保存镜像

```shell script
docker save alpine -o filename
file filename

filename: POSIX tar archive

# gzip
docker save alpine | gzip > alpine-latest.tar.gz
```

加载镜像

```shell script
docker load -i alpine-latest.tar.gz
Loaded image: alpine:latest
```

如果我们结合这两个命令以及 ssh 甚至 pv 的话，利用 Linux 强大的管道，我们可以写一个命令完成从一个机器将镜像迁移到另一个机器，并且带进度条的功能：

```shell script
docker save <image_name> | bzip2 | pv | ssh <username>@<hostname> 'cat | docker load'
```

### docker run

```shell script
docker run -it <container_name_or_id> /bin/bash
```

如需要daemon运行，添加 -d option

启动已终止的镜像

Usage

docker container start [OPTIONS] CONTAINER [CONTAINER...]

### docker container logs

查看容器日志

```shell script
docker container logs <container_name_or_id>
```

The docker logs command is an alias of docker container logs . They show the logs of a single container.

### 终止容器

docker stop / docker container stop <container_name_or_id>

### 进入容器

```shell script
docker exec -it <container> bash
```

### 导出导入容器

```shell script
docker export <container> > <tar_file>

cat <tar_file> | docker import - <repository_name>
# e.g.
cat ubuntu.tar | docker import - test/ubuntu:v1.0

# from url
docker import http://example.com/exampleimage.tgz example/imagerepo
```

### 删除容器

```shell script
docker container rm <container>

# delete all stopped container
docker container prune
```


### reference

https://codeday.me/bug/20180312/141007.html

http://yourbay.me/all-about-tech/2019/01/20/docker-no-space-left-on-device/

https://www.cnblogs.com/charlieroro/p/9233266.html

https://blog.fundebug.com/2018/01/10/how-to-clean-docker-disk/

https://blog.csdn.net/chengm8/article/details/49890261

http://www.ruanyifeng.com/blog/2011/12/inode.html

https://www.jianshu.com/p/09c476d60dbb

https://yeasy.gitbooks.io/docker_practice/image/list.html




