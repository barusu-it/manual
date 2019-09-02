
### 根据大小查找文件

```shell script
find . -type f -size +200m -exec ls -l {} +; 
find . -type f -size +200m -print0 | xargs -0 du -hb | sort -nr| head -10 

```