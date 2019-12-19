

## install on centos

```
# remove old version of docker
sudo yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine

# install using the repository
sudo yum install -y yum-utils \
  device-mapper-persistent-data \
  lvm2
sudo yum-config-manager \
    --add-repo \
    https://download.docker.com/linux/centos/docker-ce.repo
yum install docker-ce docker-ce-cli containerd.io
systemctl start docker

# verify docker engine
docker run hello-world

```

reference

https://docs.docker.com/install/linux/docker-ce/centos/

## install docker-compose

```
sudo curl -L "https://github.com/docker/compose/releases/download/1.25.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# test docker-compose
docker-compose --version
# return: docker-compose version 1.25.0, build 1110ad01
```

reference

https://docs.docker.com/compose/install/

https://linuxize.com/post/how-to-install-and-use-docker-compose-on-centos-7/