
### 根据大小查找文件

```shell script
find . -type f -size +200m -exec ls -l {} +; 
find . -type f -size +200m -print0 | xargs -0 du -hb | sort -nr| head -10 

```

### 排除特定目录

```shell script
find . -name "$pattern" ! -path "$exclude_path_pattern"
```

注意 $pattern 和 $exclude_path_pattern 都需要引号引起来

### 不递归

```shell script
find . -name "$pattern" -maxdepth 1
```

### 多条件

```shell script
# sample
find . -type f \( -iname "*.c" -or -iname "*.asm" \)
```

### Reference

https://www.cyberciti.biz/faq/find-command-exclude-ignore-files/

https://www.cyberciti.biz/faq/find-command-exclude-ignore-files/