
tickTime=2000    ##Zookeeper最小时间单元，单位毫秒(ms)，默认值为3000
dataDir=/var/lib/zookeeper    ##Zookeeper服务器存储快照文件的目录，必须配置
dataLogDir=/var/lib/log     ##Zookeeper服务器存储事务日志的目录，默认为dataDir
clientPort=2181    ##服务器对外服务端口，一般设置为2181
initLimit=5    ##Leader服务器等待Follower启动并完成数据同步的时间，默认值10，表示tickTime的10倍
syncLimit=2    ##Leader服务器和Follower之间进行心跳检测的最大延时时间，默认值5，表示tickTime的5倍

server.1=IP1:2888:3888
server.2=IP2:2888:3888
server.3=IP3:2888:3888


创建 myid 文件

modify LOG_DIR

ZOO_LOG_DIR=$ZOOBINDIR/../logs

http://www.cnblogs.com/cyfonly/p/5626532.html