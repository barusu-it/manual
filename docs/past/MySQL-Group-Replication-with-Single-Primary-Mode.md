
### modify sudoers

    su
    chmod +w /etc/sudoers
    vi /etc/sudoers # add user with NOPASSWD
    chmod -w /etc/sudoers
    exit

### update & install mysql from yum repository

#### update yum repository

    sudo yum update -y

#### install mysql57-community-release-el7-11

    sudo yum install -y wget
    wget https://dev.mysql.com/get/mysql57-community-release-el7-11.noarch.rpm
    sudo yum localinstall -y mysql57-community-release-el7-11.noarch.rpm

#### view mysql version and config if you want to use other version of mysql

    sudo yum repolist all |grep mysql

#### install mysql-community-server

    sudo yum install -y mysql-community-server
    sudo service mysqld start

#### modify password of root

    sudo grep 'temporary password' /var/log/mysqld.log
    mysql -uroot -p

    alter user 'root'@'localhost' identified by 'Password@1';

#### stop and disable firewalld

    sudo service firewalld stop
    sudo systemctl disable firewalld

or add rule

    sudo firewall-cmd --permanent --add-port={3306/tcp,24901/tcp,24902/tcp,24903/tcp}
    sudo firewall-cmd --reload

#### disable selinux, because group_replication can not connect each other if selinux is enabled
    sudo vi /etc/selinux/config

    SELINUX=disabled # original value is enforcing

    sudo reboot

#### add hostname, because group_replication communicate with each other by host name

    sudo vi /etc/hosts

#### config group_replication

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
    loose-group_replication_group_name="b1e74de4-71ce-11e7-ab5b-000c29d06630"
    loose-group_replication_start_on_boot=off
    loose-group_replication_local_address="192.168.100.201:24901"
    loose-group_replication_group_seeds="192.168.100.201:24901,192.168.100.202:24901,192.168.100.203:24901"
    loose-group_replication_bootstrap_group=off
    loose-group_replication_single_primary_mode=false # not set if you're using single primary mode
    loose-group_replication_enforce_update_everywhere_checks=true

##### 1. config user

    mysql -uroot -p

    set sql_log_bin=0;
    create user rpl_user@'%' identified by 'Password@1';
    grant replication slave on *.* to rpl_user@'%';
    flush privileges;
    set sql_log_bin = 1;

##### 2. config master

    change master to master_user='rpl_user', master_password='Password@1' for channel 'group_replication_recovery';

##### 3. install plugin

    install plugin group_replication soname 'group_replication.so';
    show plugins;

##### 4. start group_replication

    set global group_replication_bootstrap_group = on; # only execute at primary member, don't execute at other member
    start group_replication;
    set global group_replication_bootstrap_group = off; # only execute at primary member, don't execute at other member

repeat 1-4 step at other member.

#### monitor group_replication

##### list members of group replication

    select * from performance_schema.replication_group_members;

##### get primary member

    select variable_value from performance_schema.global_status where variable_name= 'group_replication_primary_member';




