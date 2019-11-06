
### 转日期

to_date(string timestamp) 返回日期时间字段中的日期部分

### 转月

month(string timestamp) 返回日期中的月份

```
hive (temp)> select month('2016-12-08 10:03:01') from dual;
12

hive (temp)> select month('2016-11-08') from dual;
11
```


### reference

https://www.jianshu.com/p/e30395941f9c