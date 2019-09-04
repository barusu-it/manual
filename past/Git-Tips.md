## window idea git log 乱码

https://blog.csdn.net/elegant__/article/details/76180154

https://www.jianshu.com/p/406b6256d41a


GitLab之添加SSH Keys（Windows）

https://www.jianshu.com/p/a8de7dc19a46

准备工作：
安装Git Bash工具

1 是否存在id_rsa.pub文件
进入C:\Users\username.ssh目录，检查是否有id_rsa.pub文件，如果存在跳至第3步。

2 生成id_rsa.pub文件
在bash中输入 ssh-keygen -t rsa -C "yourEmail@example.com"，将邮箱替换为自己邮箱即可。

1.png
需要注意的是如果填写了passphrase，则后面对GitLab的操作，需要再次输入。否则，回车即可。
最终显示如下：

2.png
3 复制id_rsa.pub内容
打开id_rsa.pub文件，并复制全部内容

4 添加SSH Keys
进入GitLab账户，在SSH Keys页面

3.png

将复制的内容粘贴至Key的文本区域中，填写Title（可能会自动生成），点击Add key，即完成SSH Key的添加。

5 添加完成
完成以上步骤后可使用SSH连接GitLab，进行相应操作。

6 测试验证
第一次使用SSH连接GitLab会有一个RSA指纹确认，输入yes即可。

4.png

此外，如果在生成SSH key时输入过passphrase，则进行操作时需要继续输入对应的内容。

作者：Darkmoss
链接：https://www.jianshu.com/p/a8de7dc19a46
來源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。


## tags

    # tag list
    git tag

    # create local tag
    git tag -a <tag_name> -m "<comment>"

    # push tag to remote
    git push origin <tag_name>

    # push all local tags to remote
    git push origin --tags

    # delete remote tag
    git push origin --delete tag <tag_name>

## markdown

https://blog.csdn.net/u010177286/article/details/50358720