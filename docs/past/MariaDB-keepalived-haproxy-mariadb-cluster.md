环境信息

三台虚拟机节点（192.168.100.171<debian171>， 192.168.100.172<debian172>， 192.168.100.173<debian173>）

Debian jessie 8.5 

MariaDB 10.1.14

Keepalived 1.2.13

HA-Proxy 1.5.8

MariaDB（Galera集群）搭建

参见“搭建MariaDB Galera Cluster（Debian 8）”

安装keepalived和haproxy

#apt安装

    sudo apt-get install keepalived haproxy

#配置/etc/haproxy/haproxy.cfg

    global
        log /dev/log    local0
        log /dev/log    local1 notice
        chroot /var/lib/haproxy
        stats socket /run/haproxy/admin.sock mode 660 level admin
        stats timeout 30s
        user haproxy
        group haproxy
        daemon

        # Default SSL material locations
        ca-base /etc/ssl/certs
        crt-base /etc/ssl/private

        # Default ciphers to use on SSL-enabled listening sockets.
        # For more information, see ciphers(1SSL). This list is from:
        #  https://hynek.me/articles/hardening-your-web-servers-ssl-ciphers/
        ssl-default-bind-ciphers ECDH+AESGCM:DH+AESGCM:ECDH+AES256:DH+AES256:ECDH+AES128:DH+AES:ECDH+3DES:DH+3DES:RSA+AESGCM:RSA+AES:RSA+3DES:!aNULL:!MD5:!DSS
        ssl-default-bind-options no-sslv3

    defaults
        log     global
        mode    tcp
        option  httplog
        option  dontlognull
        timeout connect 5000
        timeout client  50000
        timeout server  50000
        errorfile 400 /etc/haproxy/errors/400.http
        errorfile 403 /etc/haproxy/errors/403.http
        errorfile 408 /etc/haproxy/errors/408.http
        errorfile 500 /etc/haproxy/errors/500.http
        errorfile 502 /etc/haproxy/errors/502.http
        errorfile 503 /etc/haproxy/errors/503.http
        errorfile 504 /etc/haproxy/errors/504.http

    frontend http-in
        bind *:3307
        default_backend mariadb

    backend mariadb
        server server1 192.168.100.171:3306 maxconn 32
        server server2 192.168.100.172:3306 maxconn 32
        server server3 192.168.100.173:3306 maxconn 32

注意：mode要配置为tcp，其他节点的配置相同

#配置/etc/keepalived/keepalived.conf

    global_defs {
        notification_email {
            example@163.com
        }
        notification_email_from example@163.com
        smtp_server smtp.163.com
        stmp_connect_timeout 30
        router_id debian171
    }

    vrrp_script chk_haproxy {
        script "/etc/keepalived/chk_haproxy.sh"
        interval 2
    }

    vrrp_instance VI_1 {
        state BACKUP
        nopreempt
        interface eth0
        virtual_router_id 51
        priority 150
        adver_int 1
        authentication {
            auth_type PASS
            auth_pass saga
        }
        virtual_ipaddress {
            192.168.100.170/24 dev eth0 scope global
        }
        track_script {
            chk_haproxy
        }
        notify_backup "/etc/init.d/haproxy restart"
        notify_fault "/etc/init.d/haproxy stop"
    }

注意：其他节点的配置内容基本一样，但要除去nopreempt（不抢占）选项，并将priority分别调低为100和50，debian171作为MASTER，priority需要比SLAVE大至少50，此处state设置为BACKUP，但实际为MASTER，主要是避免多次抢占带来的资源浪费

#编写chk_haproxy脚本

    #!/bin/bash
    #
    if [ $(ps -C haproxy --no-header | wc -l) -eq 0 ]; then
       /etc/init.d/keepalived stop
    fi
 

参考资料：

keepalived-the-definitive-guide.pdf

http://cbonte.github.io/haproxy-dconv/configuration-1.5.html

/usr/share/doc/haproxy/configuration.txt.gz

keepalived.conf man document（man keepalived.conf）

http://www.cnblogs.com/tae44/p/4717334.html