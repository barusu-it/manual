环境信息

三台虚拟机节点（192.168.100.171<debian171>， 192.168.100.172<debian172>， 192.168.100.173<debian173>）

Debian jessie 8.5 

MariaDB 10.1.14

安装MariaDB和Galera

#添加MariaDB 10.1源

	sudo apt-get install software-properties-common
	sudo apt-key adv --recv-keys --keyserver keyserver.ubuntu.com 0xcbcb082a1bb943db
	sudo add-apt-repository 'deb [arch=amd64,i386] http://mirrors.tuna.tsinghua.edu.cn/mariadb/repo/10.1/debian jessie main'
#apt安装

	sudo apt-get install -y rsync galera-3 mariadb-server
#配置Galera，修改galera块的配置

	sudo vi /etc/mysql/my.cnf
	#
	# * Galera-related settings
	#
	[galera]
	# Mandatory settings
	#wsrep_on=ON
	wsrep_on=ON
	#wsrep_provider=
	wsrep_provider=/usr/lib/galera/libgalera_smm.so
	#wsrep_cluster_address=
	wsrep_cluster_address=gcomm://debian171,debian172,debian173
	wsrep_cluster_name=debian17x_cluster
	wsrep_sst_method=rsync
	#binlog_format=row
	binlog_format=row
	#default_storage_engine=InnoDB
	default_storage_engine=InnoDB
	#innodb_autoinc_lock_mode=2
	innodb_autoinc_lock_mode=2
	#
	# Allow server to accept connections on all interfaces.
	#
	#bind-address=0.0.0.0
	bind-address=0.0.0.0
	#
	# Optional setting
	#wsrep_slave_threads=1
	#innodb_flush_log_at_trx_commit=0
其中wsrep_cluster_address根据实际IP对应的hostname进行配置

	character_set_server = utf8
在mysqld块中设置字符集为utf8

#停止MariaDB

	debian171:~$ sudo systemctl stop mysql
	debian172:~$ sudo systemctl stop mysql
	debian173:~$ sudo systemctl stop mysql
#复制/etc/mysql/debian.cnf

由于在debian中需要使用debian-sys-maint’@’localhost‘用户对MariaDB进行操作，故需要同步复制这3台主机的debian.cnf，保证3台主机的password一致

启动MariaDB和Galera集群

#启动debian171的MySQL服务

	debian171:~$ sudo systemctl start mysql
#在debian171上启动galera集群

	debian171:~$ sudo galera_new_cluster
#查看cluster size参数

	debian171:~$ mysql -u root -p -e 'SELECT VARIABLE_VALUE as "cluster size" FROM INFORMATION_SCHEMA.GLOBAL_STATUS WHERE VARIABLE_NAME="wsrep_cluster_size"'
	+--------------+
	| cluster size |
	+--------------+
	| 1            |
	+--------------+
Good!

#启动debian172的MySQL服务

	debian172:~$ sudo systemctl start mysql
	debian171:~$ mysql -u root -p -e 'SELECT VARIABLE_VALUE as "cluster size" FROM INFORMATION_SCHEMA.GLOBAL_STATUS WHERE VARIABLE_NAME="wsrep_cluster_size"'
	+--------------+
	| cluster size |
	+--------------+
	| 2            |
	+--------------+
#启动debian173的MySQL服务

	debian173:~$ sudo systemctl start mysql
	debian171:~$ mysql -u root -p -e 'SELECT VARIABLE_VALUE as "cluster size" FROM INFORMATION_SCHEMA.GLOBAL_STATUS WHERE VARIABLE_NAME="wsrep_cluster_size"'
	+--------------+
	| cluster size |
	+--------------+
	| 3            |
	+--------------+
Well done!

重启MariaDB和Galera集群

#确定最新的节点

	cat /var/lib/mysql/grastate.dat
	# GALERA saved state
	version: 2.1
	uuid:    289127a0-4196-11e6-87e3-8372b037f5bc
	seqno:   -1
	cert_index:
其中seqno最大的节点为最新节点

#在最新节点上启动galera集群

	galera_new_cluster
#启动其他节点

	systemctl start mysql
参考资料：

debian源：https://downloads.mariadb.org/mariadb/repositories/#mirror=tuna&version=10.1&distro_release=jessie--jessie&distro=Debian

注：如为Debian Wheezy，MariaDB安装时，包名会有所不同，可参见：https://mariadb.org/installing-mariadb-galera-cluster-on-debian-ubuntu/

https://blog.sprinternet.at/2016/03/mariadb-10-1-galera-cluster-on-debian-8-jessie/