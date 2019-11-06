# Spark Quick Start

## download spark package

    axel -n 10 http://mirror.bit.edu.cn/apache/spark/spark-2.2.1/spark-2.2.1-bin-hadoop2.7.tgz

## decompress and mkdir spark home directory

    sudo mkdir /usr/local/spark
    tar xvf spark-2.2.1-bin-hadoop2.7.tgz
    sudo mv spark-2.2.1-bin-hadoop2.7 /usr/local/spark

## config environment (~/.bashrc)

    export SPARK_HOME=/usr/local/spark/spark-2.2.1-bin-hadoop2.7/
    export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin
    
## run example

    run-example SparkPi

## hadoop-common-2.7.1-bin

when throw the following exception:

    Could not locate executable null\bin\winutils.exe in the Hadoop binaries.

then use the package below and set $HADOOP_HOME and $PATH

https://github.com/SweetInk/hadoop-common-2.7.1-bin

## Problem

### Initial job has not accepted any resources; check your cluster UI to ensure that workers are registered and have sufficient resources

    start-all.sh

check jps check worker is up

    1174 Jps
    1002 Master
    1100 Worker

modify spark-env.sh

    SPARK_EXECUTOR_MEMORY=2G
    SPARK_WORKER_MEMORY=1500m

## spark-shell

## reference

https://github.com/karthikj1/Hadoop-2.7.1-Windows-64-binaries
https://blog.csdn.net/fly_leopard/article/details/51250443

    


