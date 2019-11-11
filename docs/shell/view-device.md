

### ALL

```shell script
dmidecode

dmesg
```

### CPU

```shell script
cat /proc/cpuinfo

dmesg | grep CPU
# getconf LONG_BIT
```

### MEMORY

```shell script
cat /proc/meminfo

free -m

top
```

### Disk Partition

```shell script
fdisk -l

cat /proc/partitions
```

### NETWORK

```shell script
ethtool eth0
ethtool -i eth1

dmesg | grep eth0

/etc/sysconfig/network-scripts/ifcfg-eth0

ifconfig
```

### BIOS

```shell script
lspci
```

### CD ROM

```shell script
mount /dev/cdrom mount_point
```


### USB

```shell script
fdisk -l

mount /dev/sda1 mount_point
lsusb
```


### Reference

https://www.cnblogs.com/zjiizj/archive/2010/02/25/1673503.html


### System

```shell script
uname -a               # 查看内核/操作系统/CPU信息
head -n 1 /etc/issue   # 查看操作系统版本
cat /proc/cpuinfo      # 查看CPU信息
hostname               # 查看计算机名
lspci -tv              # 列出所有PCI设备
lsusb -tv              # 列出所有USB设备
lsmod                  # 列出加载的内核模块
env                    # 查看环境变量
```

### Resource

```shell script
free -m                # 查看内存使用量和交换区使用量
df -h                  # 查看各分区使用情况
du -sh <目录名>        # 查看指定目录的大小
grep MemTotal /proc/meminfo   # 查看内存总量
grep MemFree /proc/meminfo    # 查看空闲内存量
uptime                 # 查看系统运行时间、用户数、负载
cat /proc/loadavg      # 查看系统负载
```



### Disk Partition

```shell script
mount | column -t      # 查看挂接的分区状态
fdisk -l               # 查看所有分区
swapon -s              # 查看所有交换分区
hdparm -i /dev/hda     # 查看磁盘参数(仅适用于IDE设备)
dmesg | grep IDE       # 查看启动时IDE设备检测状况
```

### Network

```shell script
ifconfig               # 查看所有网络接口的属性
iptables -L            # 查看防火墙设置
route -n               # 查看路由表
netstat -lntp          # 查看所有监听端口
netstat -antp          # 查看所有已经建立的连接
netstat -s             # 查看网络统计信息
```


### User

```shell script
w                      # 查看活动用户
id <用户名>            # 查看指定用户信息
last                   # 查看用户登录日志
cut -d: -f1 /etc/passwd   # 查看系统所有用户
cut -d: -f1 /etc/group    # 查看系统所有组
crontab -l             # 查看当前用户的计划任务
```


### CPU

```shell script
# 查看CPU信息（型号）
cat /proc/cpuinfo | grep name | cut -f2 -d: | uniq -c
#       8  Intel(R) Xeon(R) CPU            E5410   @ 2.33GHz
# (看到有8个逻辑CPU, 也知道了CPU型号)

cat /proc/cpuinfo | grep physical | uniq -c
#       4 physical id      : 0
#       4 physical id      : 1
# (说明实际上是两颗4核的CPU)

getconf LONG_BIT
#    32
# (说明当前CPU运行在32bit模式下, 但不代表CPU不支持64bit)

cat /proc/cpuinfo | grep flags | grep ' lm ' | wc -l
#    8
# (结果大于0, 说明支持64bit计算. lm指long mode, 支持lm则是64bit)


# 再完整看cpu详细信息, 不过大部分我们都不关心而已.
dmidecode | grep 'Processor Information'
```
### Memory

```shell script
# 查看内 存信息
cat /proc/meminfo
```

### Other

```shell script
uname -a
# Linux euis1 2.6.9-55.ELsmp #1 SMP Fri Apr 20 17:03:35 EDT 2007 i686 i686 i386 GNU/Linux


cat /etc/issue | grep Linux
# Red Hat Enterprise Linux AS release 4 (Nahant Update 5)

# 查看机器型号
dmidecode | grep "Product Name" 

# 查看网卡信息
dmesg | grep -i eth
```

### Reference

https://my.oschina.net/hunterli/blog/140783