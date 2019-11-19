

### 优雅关闭 docker 

使用 tini 作为 init(pid 1) 进程

before

```
ENTRYPOINT ["/docker-entrypoint.sh"]
```

after modified

```
# Add Tini
ENV TINI_VERSION v0.18.0
ADD https://github.com/krallin/tini/releases/download/${TINI_VERSION}/tini /tini
RUN chmod +x /tini

ENTRYPOINT ["/usr/local/bin/tini", "--", "/docker-entrypoint.sh"]
```

### reference

https://my.oschina.net/u/2552286/blog/3039592
