### win7远程登录，提示'凭据无法工作'

使用了网上许多方法，都不行，最后发现和用户的域有关

### 删除目录

```shell
rd /s /q <dirname>
```

### 查看端口被占用的进程

```shell
netstat -aon | findstr <port>
# 最后一个字段显示端口占用进程的ID
tasklist | findstr <process_id>
```
