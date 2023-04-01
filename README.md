# SafeTools-Boot [Java 8]

安全工具脚手架，名字灵感来源于spring-boot（虽然也不翻译为脚手架）

用于快速搭建安全工具，使聚焦于具体实现逻辑。

---

## TODO：

1.记得编写文档注释

3.ui

---

## 使用教程：

**1.需要修改的地方(即所谓只关注的地方)：**

1-1 查阅下列，或者在源代码中查看（文档注释带有**"[+] "**的函数)

```md
org.sec.input.Logo#PrintLogo
org.sec.input.Command
org.sec.start.CommandChoice
```

1-2 函数中需要修改的地方(**函数体中为 [+] 的地方**):

```java
// 如:
/** [+] 只是个测试用例*/
public static void justTest(){
    ...
    // [+]    
    ...
}
```



