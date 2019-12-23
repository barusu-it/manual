
### install elasticsearch from archive on Linux or MacOS

```shell script
wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.5.0-linux-x86_64.tar.gz
wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.5.0-linux-x86_64.tar.gz.sha512
shasum -a 512 -c elasticsearch-7.5.0-linux-x86_64.tar.gz.sha512 
tar -xzf elasticsearch-7.5.0-linux-x86_64.tar.gz
```

reference

https://www.elastic.co/guide/en/elasticsearch/reference/current/targz.html

### network.host config

```
network.host: 0.0.0.0
or
network.host: _local_,_enp0s8:ipv4_
```

### Exception: can not run elasticsearch as root

use other user to run elasticsearch

### max file descriptors [4096] for elasticsearch process is too low, increase to at least [65535]

vi /etc/security/limits.conf

append content below:

```
* soft nofile 65535
* hard nofile 65535
```

and reboot system.

check config

```
ulimit -Hn
```

reference

https://blog.csdn.net/jiahao1186/article/details/90235771

### max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]

modify temporally with command below:

```
sysctl -w vm.max_map_count=262144
```

or

vi /etc/sysctl.conf

append content below:

```
vm.max_map_count=262144
```

and reboot system.

reference

https://github.com/docker-library/elasticsearch/issues/111

https://www.elastic.co/guide/en/elasticsearch/reference/6.8/zip-targz.html


### the default discovery settings are unsuitable for production use; at least one of [discovery.seed_hosts, discovery.seed_providers, cluster.initial_master_nodes] must be configured

core configuration:

```yaml
node.name: "node-1"
discovery.seed_hosts: ["127.0.0.1", "[::1]"]
cluster.initial_master_nodes: ["node-1"]
```

reference

https://juejin.im/post/5cb81bf4e51d4578c35e727d




