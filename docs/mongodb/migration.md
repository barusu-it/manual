
### mongodb migrate to mysql

#### export from mongodb

```shell script
mongoexport -h <server_ip> -u <username> -p <password> -d <db_name> -c <collection_name> \
-f <field_name1,field_name2,field_name3...> --type=csv -o <output_file_path>
```

#### import to mysql

load sql

```
load data local infile '<input_csv_file_path>'
into table `<table_name>`
fields terminated by ',' optionally enclosed by '"'
lines terminated by '\n'
ignore 1 lines;
```

```shell script
mysql -u<user_name> -p -D<database_name> --default-character-set=utf8 --local-infile=1 < <load_sql_file>
```

#### convert empty to null

```shell script
sed -i 's/,,/,\\N,/g' <file> && sed -i 's/,,/,\\N,/g' <file> && sed -i 's/,$/,\\N/g' <file>
```

#### reference

https://blog.csdn.net/m0_37886429/article/details/88396729

