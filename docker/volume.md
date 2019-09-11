
### docker volume

```shell script
docker volume create <volume_name>  

# --mount 
docker run -d -P \
    --name web \
    # -v my-vol:/wepapp \
    --mount source=my-vol,target=/webapp \
    training/webapp \
    python app.py

docker volume rm <volume_name>

docker volume prune

# if you want to rm docker container with rm volume
docker rm -v <container>

# e.g.
docker run -d -P \
    --name web \
    # -v /src/webapp:/opt/webapp \
    --mount type=bind,source=/src/webapp,target=/opt/webapp \
    training/webapp \
    python app.py

docker run --rm -it \
   # -v $HOME/.bash_history:/root/.bash_history \
   --mount type=bind,source=$HOME/.bash_history,target=/root/.bash_history \
   ubuntu:18.04 \
   bash

```



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