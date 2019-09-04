
### 问题描述

```shell
sudo apt install xxx
```

报如下错误:

```shell
E: Could not get lock /var/lib/dpkg/lock - open (11: Resource temporarily unavailable)
E: Unable to lock the administration directory (/var/lib/dpkg/), is another process using it?
```

发现与apt.systemd.daily进程有关

```shell
ps aux |grep -i apt
```

### 解决方案

将“系统设置——软件和更新——更新——自动检查更新”选项值设为“从不”。

### 参考

https://dslztx.github.io/blog/2017/08/27/Ubuntu%E4%B8%8B%E6%AF%8F%E6%97%A5%E6%9B%B4%E6%96%B0%E5%AF%BC%E8%87%B4APT%E6%9C%BA%E5%88%B6%E9%94%81%E5%86%B2%E7%AA%81/
