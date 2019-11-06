
## grep 排除指定文件或目录

```shell script
grep -n "springboot" --exclude=$excluded_file_pattern <file_pattern>
grep -n "springboot" --exclude-dir=$excluded_directory_pattern <file_pattern>
# --exlude / --exlude_dir 可以有多个
```
