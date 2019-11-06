
一个小工具来监控（cp，mv，dd，tar等）的命令进度

```shell script
tar -czf - ./Downloads/ | (pv -p --timer --rate --bytes > backup.tgz)
```

### reference

https://www.howtoing.com/monitor-copy-backup-tar-progress-in-linux-using-pv-command/