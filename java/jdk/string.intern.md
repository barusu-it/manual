
## String intern() 的应用

```
String s0 = ”kvill”; 
String s1 = ”kvill”; 
String s2 = ”kv” + “ill”; 
System.out.println( s0 == s1 ); # true
System.out.println( s0 == s2 ); # true
# 由于3个变量都是常量kvill的引用，故返回 true
```

```
String s0 = ”kvill”; 
String s1 = new String(”kvill”); 
String s2 = ”kv” + new String(“ill”); 
System.out.println( s0 == s1 ); # false 
System.out.println( s0 == s2 ); # false
System.out.println( s1 == s2 ); # false
# 由于上述变量 s1 和 s2 是 String 对象的引用，故返回 false
```

```
String s0 = “kvill”; 
String s1 = new String(”kvill”); 
String s2 = new String(“kvill”); 
System.out.println( s0 == s1 ); # false
System.out.println( “**********” ); 
s1.intern(); 
s2 = s2.intern(); // 把常量池中“kvill”的引用赋给s2 
System.out.println( s0 == s1); # false
# 这里 s1.intern() 的引用并没有给 s1 导致 
System.out.println( s0 == s1.intern() ); # true 
System.out.println( s0 == s2 ); # true
```

```
String s1 = new String("kvill"); 
String s2 = s1.intern(); 
System.out.println( s1 == s1.intern() ); # false 
# 这里 s1 还指向 String 对象的引用
System.out.println( s1 + " " + s2 ); 
System.out.println( s2 == s1.intern() ); # true
```


### reference

https://www.cnblogs.com/Qian123/p/5707154.html