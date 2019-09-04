

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

https://github.com/wg/wrk/issues/336

### reference

https://github.com/wg/wrk/wiki