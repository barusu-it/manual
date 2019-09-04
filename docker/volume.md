

### if volumes is too big, remove it

```shell script
docker volume rm $(docker volume ls -qf dangling=true)
# or
docker volume ls -qf dangling=true | xargs -r docker volume rm
```
    
### Reference

http://www.docker.org.cn/page/resources.html

exec into container

https://www.cnblogs.com/zhuxiaojie/p/5947270.html