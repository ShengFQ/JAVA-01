####学习笔记 学号:G20210579020387 2021-01-10
class1

学习程度:了解<熟悉<熟练<理解

本次课程的要点
    
    字节码阅读(了解)
    java类加载(熟练)
    jvm内存模型(熟练)
    jvm的启动运行参数(了解)
    常用的性能指标(了解)
    
课外延伸

    jvm故障问题排查思路和工具(理解)
    jvm常见面试题(了解)       
    JSR133(了解)
理解

    java程序的启动都是从类加载开始,知道java类加载器的加载原理和加载过程,帮助我打开JVM运行时这个未知的大门.常见的加载异常,
    ClassNotFoundExcetion.
    
    工作中,常遇到的线上问题,如内存溢出和内存泄漏,以前都是未知的,学习了jvm知识后,能从底层上分析内存溢出的原因和内存泄漏的分类.对jvm这个
    黑盒子打开.
    
    jvm内存结构(并发编程中的锁,同步)
        stack (不可共享)局部变量,成员变量的指针,方法的形参,入参,
            线程栈 Xss1m 表示线程栈初始化1m大小
        heap(内存分配和内存回收的主要区域,我们可操控的,可共享) 对象,static变量,数组. 多线程下可以共享访问,基础类型的成员变量不共享.
            young-gen
                eden-space
                survive-space 0
                survive-space 1
            old-gen    
        Non-heap
            Metaspace 方法区 常量池
            CCS compressed class space 
            code cache
            
     JMM内存模型
        虽然各个线程自己使用的局部变量都在自己的栈上，但是可以共享堆
        上的对象，特别地各个不同线程访问同一个对象实例的基础类型的成员变量，会给每
        个线程一个变量的副本   


故障排查

    如果CPU使用率飙升,怎么排查
       收集不同的性能指标,CPU,内存,磁盘,网络
       分析应用日志
       分析GC日志
       查看jvm进程id ps -ef 
       导出线程栈转储并分析 jstack pid
       导出堆转储并分析 jmap dump 
常用指令集
<pre>
    查看进程号 ps -ef |grep java
    查看剩余内存 free -m ,free -h ,top
    获取堆转储  jmap -dump:format=b,file= pid.hprof pid_java
    
</pre>