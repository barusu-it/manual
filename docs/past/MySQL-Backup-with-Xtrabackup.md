
## Installing Percona XtraBackup from Percona apt repository

### 1. Fetch the repository packages from Percona web:

    $ wget https://repo.percona.com/apt/percona-release_0.1-4.$(lsb_release -sc)_all.deb

### 2. Install the downloaded package with dpkg. To do that, run the following commands as root or with sudo:

    $ sudo dpkg -i percona-release_0.1-4.$(lsb_release -sc)_all.deb

Once you install this package the Percona repositories should be added. You can check the repository setup in
the /etc/apt/sources.list.d/percona-release.list file.

### 3. Remember to update the local cache:

    $ sudo apt-get update

### 4. After that you can install the package:

    $ sudo apt-get install percona-xtrabackup-24

## Installing Percona XtraBackup from Percona yum repository

### 1. Install the Percona repository

You can install Percona yum repository by running the following command as a root user or with sudo:

    yum install http://www.percona.com/downloads/percona-release/redhat/0.1-4/percona-release-0.1-4.noarch.rpm

You should see some output such as the following:

    Retrieving http://www.percona.com/downloads/percona-release/redhat/0.1-4/percona-release-0.1-4.noarch.rpm
    Preparing...          ########################################### [100%]
        1:percona-release ########################################### [100%]

Note: RHEL/Centos 5 doesn’t support installing the packages directly from the remote location so you’ll need to
download the package first and install it manually with rpm:

    $ wget http://www.percona.com/downloads/percona-release/redhat/0.1-4/percona-release-0.1-4.noarch.rpm
    $ rpm -ivH percona-release-0.1-4.noarch.rpm

### 2. Testing the repository

Make sure packages are now available from the repository, by executing the following command:

    yum list | grep percona

You should see output similar to the following:

    ...
    percona-xtrabackup-20.x86_64 2.0.8-587.rhel5 percona-release-x86_64
    percona-xtrabackup-20-debuginfo.x86_64 2.0.8-587.rhel5 percona-release-x86_64
    percona-xtrabackup-20-test.x86_64 2.0.8-587.rhel5 percona-release-x86_64
    percona-xtrabackup-21.x86_64 2.1.9-746.rhel5 percona-release-x86_64
    percona-xtrabackup-21-debuginfo.x86_64 2.1.9-746.rhel5 percona-release-x86_64
    percona-xtrabackup-22.x86_64 2.2.13-1.el5 percona-release-x86_64
    percona-xtrabackup-22-debuginfo.x86_64 2.2.13-1.el5 percona-release-x86_64
    percona-xtrabackup-debuginfo.x86_64 2.3.5-1.el5 percona-release-x86_64
    percona-xtrabackup-test.x86_64 2.3.5-1.el5 percona-release-x86_64
    percona-xtrabackup-test-21.x86_64 2.1.9-746.rhel5 percona-release-x86_64
    percona-xtrabackup-test-22.x86_64 2.2.13-1.el5 percona-release-x86_64
    ...

### 3. Install the packages

You can now install Percona XtraBackup by running:

    yum install percona-xtrabackup-24

Warning: In order to sucessfully install Percona XtraBackup libev package will need to be installed first.
libev package can be installed from the EPEL repositories.

## Installing Percona XtraBackup using downloaded rpm packages

Download the packages of the desired series for your architecture from the download page. Following example will
download Percona XtraBackup 2.4.4 release package for CentOS 7:

    $ wget https://www.percona.com/downloads/XtraBackup/Percona-XtraBackup-2.4.4/binary/redhat/7/x86_64/percona-xtrabackup-24-2.4.4-1.el7.x86_64.rpm

Now you can install Percona XtraBackup by running:

    $ yum localinstall percona-xtrabackup-24-2.4.4-1.el7.x86_64.rpm

Note: When installing packages manually like this, you’ll need to make sure to resolve all the dependencies and
install missing packages yourself.


## full backup
    sudo time innobackupex --user=root --password=root /home/norxiva/db_snapshot/backup/

## incremental backup
    sudo time innobackupex --user=root --password=root --incremental-basedir=/home/norxiva/db_snapshot/backup/2018-02-04_05-31-03 --incremental /home/norxiva/db_snapshot/backup/


## recover backup

```bash
# recover full
sudo innobackupex --apply-log --redo-only <full_backup_path>
# recover first incremental backup
sudo innobackupex --apply-log --redo-only <full_backup_path> --incremental-dir=<1st incremental path>
# recover last incremental backup
sudo innobackupex --apply-log <full_backup_path> --incremental-dir=<last incremental path>

# copy back
sudo innobackupex --copy-back <full_backup_path>

sudo chown -R mysql:mysql <mysql_datadir>
sudo service mysql start
```

## 问题汇总

### centos 7 yum安装 percona-xtrabackup 时/etc/my.cnf冲突问题

mysql 5.7 缺少 [mysql-community-libs-compat-5.7.21-1.el7.x86_64.rpm](http://ftp.ntu.edu.tw/MySQL/Downloads/MySQL-5.7/mysql-community-libs-compat-5.7.21-1.el7.x86_64.rpm​)
