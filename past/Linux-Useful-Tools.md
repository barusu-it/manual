## axel ( a useful download tool)

### installation

    sudo apt install axel

### usage
    axel -n <thread number> <url>

## scp (transfer file between machines)

### usage

    scp <localfile> <username>:<remote_ip>:<remote_path>

## ssh-keygen (create ssh key)

### How to ssh remote machine without password

#### 1. create ssh key

    ssh-keygen -t rsa
    # press enter key straightly

#### 2. scp \~/.ssh/id_rsa.pub to remote machine (\~/.ssh)

#### 3. append id_rsa.pub to ~/.ssh/authorized_keys file

    cat <pub file> >> ~/.ssh/authorized_keys

## xclip (clip in command line)

### installation

    sudo apt install xclip

### copy to clipboard

    ls -al | xclip
    pwd | xclip

### copy file content to clipboard

    xclip /etc/apt/source.list

### output to standard output

    xclip -o
    xclip -o > ~/output.txt

## expect

### reference

https://www.cnblogs.com/yangmingxianshen/p/7967040.html

https://www.cnblogs.com/scue/p/4028735.html

https://www.cnblogs.com/arlenhou/p/learn_expect.html

http://www.jb51.net/article/119541.htm

https://likegeeks.com/expect-command/

https://www.lifewire.com/linus-unix-command-expect-2201096

http://expect.sourceforge.net/


