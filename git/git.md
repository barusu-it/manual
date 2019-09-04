
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