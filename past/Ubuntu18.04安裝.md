
### sudo免密

```shell
sudo su -
chmod +w /etc/sudoers
# vi /etc/sudoers 修改文件
# 在'%sudo ALL=(ALL:ALL) NOPASSWD:ALL'之后添加一行
# %<user> ALL=(ALL:ALL) NOPASSWD:ALL
chmod -w /etc/suders

# 上述操作也可以用 sudo visudo 来修改，不过用起来不太习惯
```

### 远程桌面

在Ubuntu软件商店中搜索remote desktop viewer, 安装Remote Desktop Viewer

注意：连接window时，需要选择连接方式是RDP

### Wechat安装

[electronic-wechat下载] https://github.com/kooritea/electronic-wechat/releases

解压并移动到/opt目录，复制目录下的electronic-wechat.desktop到~/Desktop

双击点击信任即可。

### 搜狗输入法安装

#### 安装fcitx

```shell
sudo apt install fcitx fcitx-table
```
#### 切换输入法到fcitx

打开Input Method应用,选择'OK', 'Yes', 'fcitx', 'OK'

#### 卸载ibus

```shell
sudo apt autoremove ibus*
```

#### 安装搜狗输入法

下载搜狗输入法linux版,并双击deb下载文件,点击install

重启系统

#### 添加搜狗输入法

打开Fcitx Config Tool,取消'Only Show Current Language'复选框,搜索'Sogou Pinyin',并添加到列表里.

> 这里可选择列表中输入法,点击最下方列表中最右侧的按钮,可打开'Default Keyboard layout' 选项框,选择'Default',点击'OK',这样可选择打开应用后默认的输入法

重启系统

完成

### Chrome安装

```shell
sudo wget https://repo.fdzh.org/chrome/google-chrome.list -P /etc/apt/sources.list.d/
wget -q -O - https://dl.google.com/linux/linux_signing_key.pub  | sudo apt-key add -
sudo apt update
sudo apt install google-chrome-stable
```
