

## standalone

```shell script
wget https://archive.apache.org/dist/zookeeper/current/apache-zookeeper-3.5.6-bin.tar.gz
wget https://archive.apache.org/dist/zookeeper/current/apache-zookeeper-3.5.6-bin.tar.gz.sha512
sha512sum -c apache-zookeeper-3.5.6-bin.tar.gz
cd apache-zookeeper-3.5.6-bin
cp conf/zoo_sample.cfg conf/zoo.cfg
bin/zkServer.sh start
# it will start zookeeper and zookeeper admin server with 2181 and 8080 port.
# if you want to change port.
# modify zoo.cfg file
# update clientPort=2181 for changing zookeeper port
# and update admin.serverPort=8080 for changing zookeeper admin server port
```

### reference

https://zookeeper.apache.org/doc/current/zookeeperStarted.html

https://zookeeper.apache.org/doc/r3.5.3-beta/zookeeperAdmin.html#sc_adminserver_config
