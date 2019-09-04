

### my.cnf

#### innodb_flush_log_at_trx_commit = 2 

能够获得接近MyISAM的读取性能 (相差百倍) 。

#### 

#### innodb_file_per_table=1 

能够保证 ibdata1 文件不会过大。失去控制。尤其是在运行 mysqlcheck -o –all-databases 的时候。

#### 足够大的 innodb_buffer_pool_size

推荐将数据全然保存在 innodb_buffer_pool_size ，即按存储量规划 innodb_buffer_pool_size 的容量。这样你能够全然从内存中读取数据。最大限度降低磁盘操作。

2.1.1 怎样确定 innodb_buffer_pool_size 足够大。数据是从内存读取而不是硬盘？
方法 1

    mysql> SHOW GLOBAL STATUS LIKE 'innodb_buffer_pool_pages_%';

发现 Innodb_buffer_pool_pages_free 为 0，则说明 buffer pool 已经被用光，须要增大 innodb_buffer_pool_size

InnoDB 的其它几个參数：

innodb_additional_mem_pool_size = 1/200 of buffer_pool
innodb_max_dirty_pages_pct 80%

方法 2

或者用iostat -d -x -k 1 命令，查看硬盘的操作。

2.1.2 server上是否有足够内存用来规划
运行 echo 1 > /proc/sys/vm/drop_caches 清除操作系统的文件缓存。能够看到真正的内存使用量。

## Reference

https://www.cnblogs.com/claireyuancy/p/7258314.html

https://www.cnblogs.com/zhouyusheng/p/8038224.html

https://www.zhihu.com/question/19719997

https://www.cnblogs.com/liujiacai/p/7605612.html

B-Tree

https://www.cnblogs.com/dongguacai/p/7239599.html

## Summary

* 使用InnoDB引擎
* innodb_flush_log_at_trx_commit = 2 能够获得接近的读取性能 (相差百倍)
* innodb_file_per_table=1 （这个是默认值）
* innodb_buffer_pool_size 调大 innodb_additional_mem_pool_size = 1/200 of buffer_pool， innodb_max_dirty_pages_pct 80%
* 数据预热
* 禁用 SWAP
* 降低磁盘写入操作 innodb_log_file_size 设置为 0.25 * innodb_buffer_pool_size， innodb_flush_log_at_trx_commit = 1 则每次改动写入磁盘， innodb_flush_log_at_trx_commit = 0/2 每秒写入磁盘
* 避免双写入缓冲 innodb_flush_method=O_DIRECT
* 充分使用索引
* 分析查询日志和慢查询日志 log-slow-queries
* 单条查询最后添加 LIMIT 1，停止全表扫描。
* 使用 IP 而不是域名做数据库路径。避免 DNS 解析问题
* 不用 MYSQL 内置的函数。由于内置函数不会建立查询缓存。
* EXPLAIN 你的 SELECT 查询
* 在Join表的时候使用相当类型的例，并将其索引
* 千万不要 ORDER BY RAND()
* 避免 SELECT *
* 永远为每张表设置一个ID，最好的是一个INT型的（推荐使用UNSIGNED）
* 使用 ENUM 而不是 VARCHAR（这个个人觉得要慎用，否则后面alter table会是个坑，为了提升一点点的性能，不是很值）
* 从 PROCEDURE ANALYSE() 取得建议
* 尽可能的使用 NOT NULL
* Prepared Statements
* 把IP地址存成 UNSIGNED INT （这个仁者见仁，有适合的场景可以使用）
* 垂直分割表（业务的关联性）
* 拆分大的 DELETE 或 INSERT 语句