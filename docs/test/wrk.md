
### Usage

```shell script
# show help
wrk -h
# example
wrk -t10 -c100 -d20s <http_url>
```

### compilation

ubuntu/debain

```shell script
sudo apt-get install build-essential libssl-dev git -y
git clone https://github.com/wg/wrk.git wrk
cd wrk
make
# move the executable to somewhere in your PATH, ex:
sudo cp wrk /usr/local/bin
```

### Question: Makefile:64: recipe for target 'obj/bytecode.o' failed

https://github.com/wg/wrk/issues/362

use sudo make

### 连接未释放问题

疑似连接未释放，导致大量TIME_WAIT和CLOSE_WAIT，连接用尽的问题

https://github.com/wg/wrk/issues/336

### reference

https://github.com/wg/wrk/wiki