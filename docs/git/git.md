
### clone a tag

```shell script
git clone <git_url>
# show tag list
git tag 
git checkout <tag_name>
# the branch status is 'detached HEAD', you can not modify.
# if you want to modify code, use this
git checkout -b <new_branch_name> <tag_name>

```

### clone special branch

```shell script
git clone -b <branch_name> <git_url>
```

### show remote branch

```shell script
git branch -r
```

### error: cannot stat ‘file’: Permission denied

一般情况是编辑器、浏览器或资源管理器等应用占用导致，关闭相关应用即可，必要时重启一下
