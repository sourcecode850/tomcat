
Digester基于规则的XML文档解析,主要用于XML到Java对象的映射。Struts就是用Digester来处理XML配置文件的。
如今Digester随着Struts的发展以及其的公用性而被提到commons中独自立项，
是apache的一个组件 apache commons-digester.jar,通过它可以很方便的从xml文件生成java对象

Digester 是 apache 基金组织下的一个开源项目，笔者对它的了解源于对 Struts 框架的研究，
是否有很多程序员想要一解各大开源框架的设计甚至想要自己写一个功能强大的框架时会碰到这样一个难题：
这些形形色色的用 XML 语言标记的框架配置文件，框架底层是用什么技术来解析呢？ DOM 解析耗费时间，
SAX 解析又过于繁琐，况且每次解析系统开销也会过大， 于是，大家想到需要用与 XML 结构相对应的 JavaBean
来装载这些信息，由此 Digester 应运而生。它的出现为 XML 转换为 JavaBean 对象的需求带来了方便的操作接口，
使得更多的类似需求得到了比较完美的解决方法， 不再需要程序员自己实现此类繁琐的解析程序了。
与此同时 SUN 也推出了 XML 和 JavaBean 转换工具类 JAXB

原文链接：https://blog.csdn.net/xingxing43/article/details/8484682


首先明白Digester是干什么的？它是apache开源项目Commons中的一个子项目，用于解析XML文档的工具。
Digester底层采用的是SAX解析方式，通过遍历XML文档规则来进行处理。项目中有需要将XML文件中的信息
解析为我们需要的内容时（如java类），使用Digester是非常方便的
原文链接：https://blog.csdn.net/qq_27468351/article/details/75448202


工作原理如下:
    Digester底层采用SAX(Simple API for XML)析XML文件，所以很自然的，对象转换由"事件"驱动，
    在遍历每个节点时，检查是否有匹配模式，如果有，则执行规则定义的操作，比如创建特定的Java对象，
    或调用特定对象的方法等。此处的XML元素根据匹配模式(matching pattern)识别，而相关操作由规则(rule)定义
原文链接：https://www.iteye.com/blog/uule-2340091

digester.book相关代码摘自下文
https://www.jianshu.com/p/4cdc422b269b

下面这篇博文讲解Digester不错
Apache Commons Digester 二（规则模块绑定-RulesModule、异步解析-asyncParse、xml变量Substitutor、带参构造方法）
https://www.cnblogs.com/chenpi/p/6947441.html