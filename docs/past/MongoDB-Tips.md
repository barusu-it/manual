## Problems

### Failed to fetch https://repo.mongodb.org/apt/ubuntu/dists/xenial/mongodb-org/3.6/InRelease  Operation timed out after 0 milliseconds with 0 out of 0 bytes received

use http instead of https


## create database and authorize

    use <dbname>
    db.createUser({user: "<username>", pwd:"<password>", roles: [{role:"readWrite", db: "<dbname>"}]})

## import & export

    mongoimport --host <ip> --port <port> --username <username> --password <password> --collection <table_name> --db <dbname> --file <json_file>

    mongoexport --host <ip> --port <port> --username <username> --password <password> --collection <table_name> --db <dbname> --out <json_file>

## export with query

    mongoexport --host 172.16.6.58 --port 27017 --username user --password admin --collection mockApiDefinition --db mock-server --query "{name: 'CHECK_PASSWORD'}" --out mockApiDefinition.json

## remove document

    db.getCollection('mockApiDefinition').remove({name: 'RECHARGE'})

## execute js by mongo command

    mongo 127.0.0.1:27017/test userfindone.js



## optimize

https://blog.csdn.net/happy_jijiawei/article/details/53737858

https://www.jianshu.com/p/b77a33fbe824

https://www.cnblogs.com/wang726zq/p/6797751.html

## Summary

* mongostat
* db.test.find({age: “20”}).hint({age:1 }).explain(); 
* limit
* 热数据法
* 范式化/反范式化
* 索引
* db.getProfilingLevel()
* 读写分离