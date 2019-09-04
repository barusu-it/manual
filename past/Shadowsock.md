## Server安装

### 环境

* OS: Ubuntu 14.04 LTS
* Cloud: AWS
* User: root

这里推荐使用14.04，其他版本多少会存在问题

### 安装python-pip及shadowsock

```shell
apt install build-essential python-pip
pip install shadowsocks
```
### 安装setuptools和wheel

```shell
pip install setuptools
pip install wheel

#if Ubuntu 16.04 and above.
pip install --upgrade setuptools
pip install --upgrade wheel
```

### 配置shadowsock

配置文件/etc/shadowsocks.json

```
{
  "server": "0.0.0.0",
  "server_port": 8388,
  "local_address": "127.0.0.1",
  "local_port": 1080,
  "password": "<your_password>",
  "timeout": 300,
  "method": "aes-256-cfb"
}
```

替换下密码

### 关闭防火墙

```shell
ufw disable
```

### 关闭iptables

```shell
iptables -P INPUT ACCEPT
iptables -P FORWARD ACCEPT
iptables -P OUTPUT ACCEPT
iptables -F
```

### 启动服务及关闭服务

```shell
ssserver -c /etc/shadowsocks.json -d start
ssserver -c /etc/shadowsocks.json -d stop
```

### 添加开机启动

修改/etc/rc.local,在'exit 0'之前添加启动服务脚本

## Client安装

### Windows

[官方教程](https://github.com/shadowsocks/shadowsocks-windows/wiki/Shadowsocks-Windows-%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E)

1. 需要安装.NET Framework 4.6.2 和 Microsoft Visual C++ 2015 Redistributable
2. 下载Shadowsocks客户端
3. 启动shadowsocks.exe后，右键配置服务器IP，端口，密码等
4. 右键选择启动代理（可以在右键菜单中选择代理模式，默认是PAC代理）

### Ubuntu 18.04

#### 安装shadowsocks客户端

```shell
sudo su -
apt install build-essential python-pip
pip install setuptools wheel
pip install --upgrade setuptools wheel
pip install shadowsocks
```

上述安装方式也可以通过apt直接安装

```shell
sudo apt install shadowsocks
```

编辑配置文件/etc/shadowsocks.json（apt直接安装的有默认配置文件/etc/shadowsocks/config）

```
{
  "server": "<server_ip>",
  "server_port": 8388,
  "local_address": "127.0.0.1",
  "local_port": 1080,
  "password": "<your_password>",
  "timeout": 300,
  "method": "aes-256-cfb"
}
```

启动shadowsocks客户端

这里也需要root或sudo执行，否则会有权限问题

```shell
sudo sslocal -c /etc/shadowsocks.json -d start
```

停止服务

```shell
sudo sslocal -c /etc/shadowsocks.json -d stop
```

##### 安装代理服务

这时会发现还是无法翻墙，这里就需要代理服务客户端来配合，这里使用privoxy

这里没有用 polipo，主要安装是会与原生的软件抢占53端口

```shell
apt install privoxy
```

修改配置文件/etc/privoxy/config，在第1337行打开注解，修改为：

```
forward-socks5t / 127.0.0.1:1080 .
```

重启privoxy服务

```shell
sudo service prixovy restart
```

设置 http_proxy 和https_proxy 或直接设置浏览器或系统的代理即可

```shell
export http_proxy=http://localhost:8118
export https_proxy=http://localhost:8118
```

##### PAC方式代理

安装GenPAC

```shell
sudo pip install genpac
sudo pip install --upgrade genpac
```
生成pac文件

```shell
mkdir -p ~/config
cd ~/config
sudo genpac --proxy="SOCKS5 127.0.0.1:1080" -o autoproxy.pac --gfwlist-url="https://raw.githubusercontent.com/gfwlist/gfwlist/master/gfwlist.txt"
```

设置系统代理

进入设置：系统设置 -> 网络 -> 网络代理，选择automatic，配置url：file:///home/xxx/config/autoproxy.pac

完成

注：PAC的模式不太稳定，在部分网络环境下，无法翻墙

##### 参考

http://www.diosfun.com/2018/06/29/init-env/ubuntu18-04%E5%AE%89%E8%A3%85shadowsocks%E5%AE%A2%E6%88%B7%E7%AB%AF

##### polipo安装（补充）

```shell
apt install polipo
```

修改配置文件/etc/polipo/config

```
# This file only needs to list configuration variables that deviate
# from the default values. See /usr/share/doc/polipo/examples/config.sample
# and "polipo -v" for variables you can tweak and further information.
logSyslog = false
logFile = "/var/log/polipo/polipo.log"

socksParentProxy = "127.0.0.1:1080"
socksProxyType = socks5

chunkHighMark = 50331648
objectHighMark = 16384

serverMaxSlots = 64
serverSlots = 16
serverSlots1 = 32

proxyAddress = "0.0.0.0"
proxyPort = 8123
```

重启服务并设置代理环境变量

```shell
sudo /etc/init.d/polipo restart
export http_proxy=http://127.0.0.1:8123/ 
```
##### shadowsocks启动python报错'...undefined symbol: EVP_CIPHER_CTX_cleanup'

主要原因是libcrypto.so的版本升级造成

解决方法

修改'~/.local/lib/python2.7/site-packages/shadowsocks/crypto/openssl.py'文件，替换cleanup方法为reset
```
:%s/cleanup/reset/
```
