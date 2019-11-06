


### 使用私钥登录sftp

```shell script
sftp -o "IdentityFile=/path/to/private_key" -P $port $username@$hostname
```