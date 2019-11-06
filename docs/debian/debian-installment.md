## Debian information

version: 8.7.1

Ubuntu version based in Debian

    18.04  bionic     buster  / sid
    17.10  artful     stretch / sid
    17.04  zesty      stretch / sid
    16.10  yakkety    stretch / sid
    16.04  xenial     stretch / sid
    15.10  wily       jessie  / sid
    15.04  vivid      jessie  / sid
    14.10  utopic     jessie  / sid
    14.04  trusty     jessie  / sid
    13.10  saucy      wheezy  / sid
    13.04  raring     wheezy  / sid
    12.10  quantal    wheezy  / sid
    12.04  precise    wheezy  / sid
    11.10  oneiric    wheezy  / sid
    11.04  natty      squeeze / sid
    10.10  maverick   squeeze / sid
    10.04  lucid      squeeze / sid


### open useful command alias by uncommenting the following line in .bashrc

    # some more ls aliases 
    #alias ll='ls -l'
    #alias la='ls -A'
    #alias l='ls -CF'

### sudo install by root

#### 1. install sudo
    apt update
    apt install sudo

#### 2. add write authority of sudoers file

    chmod +w /etc/sudoers

#### 3. open sudoers file

    # User privilege specification
    root    ALL=(ALL:ALL) ALL

#### 4. add one line below the line of root user privilege

    norxiva ALL=(ALL:ALL) NOPASSWD:ALL

### modify ip and hostnamme after clone virtual machine

* modify ip in /etc/network/interfaces 
* modify hostname in /etc/hosts and /etc/hostname
* at last reboot it

## apt-file (tool for finding out which package is the command located)

### install

    sudo apt install apt-file & apt-file update

### usage

    apt-file search add-apt-repository

    // software-properties-common: /usr/bin/add-apt-repository
    // software-properties-common: /usr/share/man/man1/add-apt-repository.1.gz

## add-apt-repository

### add ppa(e.g.)

    sudo add-apt-repository ppa:webupd8team/java

### remove ppa(e.g.)

    sudo add-apt-repository --remove ppa:webupd8team/java

## rz and sz (tranfer files tool)

    sudo apt install lrzsz

### usage

    rz
    sz <file>


## update-rc.d (service management tool in debian and ubnutu)

### usage

    update-rc.d [-n] [-f] name remove
    update-rc.d [-n] name defaults
    update-rc.d [-n] name
    update-rc.d [-n] name disable|enable [ S|2|3|4|5 ]

    sudo update-rc.d redis-server disable

    sudo update-rc.d redis-server enable

    sudo service --status-all
    # ==>
     [ + ]  acpi-fakekey
     [ - ]  acpi-support
     [ + ]  acpid
     [ ? ]  alsa-utils
     [ - ]  anacron
    ...

### reference

http://blog.teeceepee.com/blog/2013/12/30/remove-unnecessary-auto-start-services/

cdimage

https://cdimage.debian.org/mirror/cdimage/archive/8.10.0/amd64/iso-dvd/