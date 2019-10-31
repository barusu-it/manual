
### 断点续传

```shell script
wget -c -t 0 $download_url
```

-c 支持断点续传
-t 0 重试次数, 0 则不限次数