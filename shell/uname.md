
# uname

```shell script

uname -s
# 内核名称
# Linux

uname -r
# 内核发行版
# 2.6.18-371.1.2.el5

uname -v
# 内核版本
#1 SMP Tue Oct 22 12:57:43 EDT 2013

uname -n
# 主机名
# dev-machine

uname -m
# i686
# i686: 32bit; X86_64: 64bit

uname -i
# i386
# i386: 32bit; X86_64: 64bit

uname -p
# i686

uname -o
# 操作系统
# GNU/Linux

uname -a
# Linux dev-machine 2.6.18-371.1.2.el5 #1 SMP Tue Oct 22 12:57:43 EDT 2013 i686 i686 i386 GNU/Linux
# -i和-p输出为unknown则默认会被省略

# Redhat / CentOS
cat /etc/redhat_release
# CentOS release 5.10 (Final)

cat /etc/issue
# 发行版
# Linux Mint Olivia \n \l:

```