
## 仅 clone 部分目录 (git的实现：基于sparse clone变通方法)

```shell script
# init local repository
mkdir -p project/springboot
cd project/springboot
git init

# pull remote all objects info
git remote add -f origin https://github.com/barusu-it/tutorial.git

# enable sparse clone
git config core.sparsecheckout true
echo "springboot" >> .git/info/sparse-checkout

# pull origin master
git pull origin master

# done.
```
