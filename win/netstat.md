
### 查看端口占用进程

```shell script
netstat -ano | findstr 8080
```

### 查看进程任务

```shell script
tasklist | findstr $pid

# sample of killing task
taskkill /f /t /im /javaw.exe
```

### 参考

https://blog.csdn.net/jiangwei0910410003/article/details/18967441
