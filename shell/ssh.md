
### 指定私钥文件

```shell script
ssh -i /path/to/private_key $username@$hostname
```

### ssh “permissions are too open” error

```shell script
chmod 400 $private_key_file
# or
chmod 600 $private_key_file
```