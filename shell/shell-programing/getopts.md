

### getopts vs getopt

1. getopts 是 Shell 内建命令，getopt 是一个独立外部工具
2. getopts 使用语法简单，getopt 使用语法较复杂
3. getopts 不支持长参数（如：--option ），getopt 支持
4. getopts 不会重排所有参数的顺序，getopt 会重排参数顺序（这里的区别下面会说明）
5. getopts 出现的目的是为了代替 getopt 较快捷的执行参数分析工作

### getopts example

```shell script
#!/bin/bash
while getopts 'd:Dm:f:t:' OPT; do
    case $OPT in
        d)
            DEL_DAYS="$OPTARG";;
        D)
            DEL_ORIGINAL='yes';;
        f)
            DIR_FROM="$OPTARG";;
        m)
            MAILDIR_NAME="$OPTARG";;
        t)
            DIR_TO="$OPTARG";;
        ?)
            echo "Usage: `basename $0` [options] filename"
    esac
done
  
shift $(($OPTIND - 1))
```

### getopt example


```shell script
#!/bin/bash
 
ARGS=`getopt -o "ao:" -l "arg,option:" -n "getopt.sh" -- "$@"`
 
eval set -- "${ARGS}"
 
while true; do
    case "${1}" in
        -a|--arg)
        shift;
        echo -e "arg: specified"
        ;;
        -o|--option)
        shift;
        if [[ -n "${1}" ]]; then
            echo -e "option: specified, value is ${1}"
            shift;
        fi
        ;;
        --)
        shift;
        break;
        ;;
    esac
done
```

```shell script
# ./getopt.sh -a
arg: specified
# ./getopt.sh -a -o Apple
arg: specified
option: specified, value is Apple
# ./getopt.sh --arg --option Apple
arg: specified
option: specified, value is Apple
```

### reference

https://www.cnblogs.com/yxzfscg/p/5338775.html