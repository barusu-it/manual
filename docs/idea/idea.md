
### Exception Breakpoint

在断点窗口中可以创建异常断点，异常断点指定的是特定的 exception 类

### This custom Spring bean has not yet been parsed.

https://intellij-support.jetbrains.com/hc/en-us/community/posts/206274479-This-custom-Spring-bean-has-not-yet-been-parsed-

### Error running $classname: Command line is too long. Shorten command line for $classname

modify .idea/workspace.xml

append content below in \<component name="PropertiesComponent"\>

    <property name="dynamic.classpath" value="true" />

### cancel import package.*

Menu Path: Setting -> Editor -> Code Style -> Java -> Imports

modify \[Class count to use import with '*':\] from 5 to 999

modify \[Names count to use static import with '*':\] from 3 to 999

### format all code

* select project directory
* select menu: Code -> Format Code
* choose optimise imports if you need

### memory not enough

modify idea.exe.vmoptions in the folder of idea.exe

change -Xmx512m to -Xmx1024m

### This custom Spring bean has not yet been parsed.

If you put your cursor on the bean that has the warning a quick fix should pop up that will allow you to parse the bean definition.  You'll also get the benefit of code completion in the custom tags after that.

### import google style

Menu: File -> Settings -> Editor —> Code Style -> Java, import google style scheme

### set hard wrap

File -> Settings -> Editor -> Code Style, hard wrap at '120' to '100'

### Warning:(xx, xx) Not annotated parameter overrides @NotNull parameter

add @NotNull annotation to fix it.