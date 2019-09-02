
### 查看 docker 信息

```shell script
docker info
```

### 进入容器

```shell script
docker exec -it <container-id> /bin/sh
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


### reference

https://codeday.me/bug/20180312/141007.html

http://yourbay.me/all-about-tech/2019/01/20/docker-no-space-left-on-device/

https://www.cnblogs.com/charlieroro/p/9233266.html

https://blog.fundebug.com/2018/01/10/how-to-clean-docker-disk/

https://blog.csdn.net/chengm8/article/details/49890261

http://www.ruanyifeng.com/blog/2011/12/inode.html

https://www.jianshu.com/p/09c476d60dbb




