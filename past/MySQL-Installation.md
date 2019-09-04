## Yum install

    sudo yum install -y wget
    wget https://dev.mysql.com/get/mysql57-community-release-el7-11.noarch.rpm
    sudo yum localinstall -y mysql57-community-release-el7-11.noarch.rpm
    sudo yum install -y mysql-community-server
    sudo service mysqld start
    sudo grep 'temporary password' /var/log/mysqld.log
    
    mysql -uroot -p
    alter user 'root'@'localhost' identified by 'Password@1';
    use mysql
    update user set host = '%' where user = 'root';
    exit

    sudo firewall-cmd --permanent --add-port=3306/tcp
    sudo firewall-cmd --reload

    sudo vi /etc/my.cnf

    server_id=1
    gtid_mode=ON
    enforce_gtid_consistency=ON
    master_info_repository=TABLE
    relay_log_info_repository=TABLE
    binlog_checksum=NONE
    log_slave_updates=ON
    log_bin=binlog
    binlog_format=ROW

    transaction_write_set_extraction=XXHASH64
    loose-group_replication_group_name=6faa8d32-5264-4c6d-aa95-bea040d14e75
    loose-group_replication_start_on_boot=off
    loose-group_replication_local_address=192.168.100.196:24901
    loose-group_replication_group_seeds=192.168.100.196:24901,192.168.100.197:24901,192.168.100.198:24901
    loose-group_replication_bootstrap_group=off

set sql_log_bin = 0;
grant replication slave on *.* to rpl_user@'%' identified by 'Password@1';
flush privileges;
set sql_log_bin = 1;

sudo service mysqld restart

change master to master_user = 'rpl_user', master_password = 'Password@1' for channel 'group_replication_recovery';

install plugin group_replication soname 'group_replication.so';
show plugins;

