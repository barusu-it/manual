

### merge forked repository

1. Code Tab, click 'New pull request'
2. choose 'base fork' and 'head fork', and click 'Create pull request'
3. click 'Merge pull request'

### use git command

```shell script
git remote -v 
git remote add upstream git@github.com:xxx/xxx.git
git fetch upstream
git merge upstream/master
git push 
```

or

```shell script
git remote -v 
git remote add upstream aaa
git remote -v
git remote remove upstream
git remote add upstream https://xxx/xxx.git
git remote -v
git fetch upstream
git merge upstream/master
git pull origin master
git push
```

### reference

https://blog.csdn.net/qq1332479771/article/details/56087333