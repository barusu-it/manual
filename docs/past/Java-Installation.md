## Install java 8 on Debian with apt

    # deprecated command (Ubuntu 18.04 还是可以使用这个ppa)
    sudo add-apt-repository ppa:webupd8team/java
    # use following command
    echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | sudo tee /etc/apt/sources.list.d/webupd8team-java.list
    echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | sudo tee -a /etc/apt/sources.list.d/webupd8team-java.list
    sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886

    sudo apt update
    sudo apt install oracle-java8-installer

如果下载jdk慢,可以将下载的tar.gz包,放在 /var/cache/oracle-jdk8-installer 目录下

切换java版本
```shell
sudo update-alternatives --config java
```

## Reference

http://www.webupd8.org/2014/03/how-to-install-oracle-java-8-in-debian.html

http://www.webupd8.org/2015/02/install-oracle-java-9-in-ubuntu-linux.html

架构题

http://baijiahao.baidu.com/s?id=1576944804166718725&wfr=spider&for=pc

vector arraylist和linkedlist的区别

https://www.cnblogs.com/zkk-wust/p/7250776.html

Servlet的生命周期

https://blog.csdn.net/danielzhou888/article/details/70835418

反射

http://www.importnew.com/20339.html

IOC AOP

https://www.cnblogs.com/gaopeng527/p/5290997.html

索引原理

https://www.cnblogs.com/aspwebchh/p/6652855.html

oracle中 rownum与rowid的理解，一千条记录我查200到300的记录怎么查？

https://zhidao.baidu.com/question/209684465.html

谈谈hibernate的理解，一级和二级缓存的作用，在项目中Hibernate都是怎么使用缓存的。

一级缓存是session，不可卸载，二级缓存是sessionFacotory管理，可卸载，且需要留意并发策略

sendRedirect, foward区别

https://blog.csdn.net/xuxurui007/article/details/7755792