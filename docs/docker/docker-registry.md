
### docker login / docker logout 

### docker search 

搜索镜像

```shell script
docker search centos
```

### docker pull / docker push

拉取/推送镜像

```shell script
docker push <registry_host>:<port>/<container_name>:<tag>
```

### curl 查看镜像

```shell script
curl <registry_host>:<port>/v2/_catalog
{"repositories":["ubuntu"]}
```


