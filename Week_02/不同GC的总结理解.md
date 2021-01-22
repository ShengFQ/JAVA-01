###作业 week2-jvm03
 学号:G20210579020387 
 日期:2021-01-14
 主题:不同GC的理解和总结
 
 GC的共同点
 清理算法:
    
    标记清除算法(mark and sweep): 并行GC和CMS的基本原理. 优点:可以处理循环依赖,只扫描部分对象.时间长,碎片多.
    标记复制算法(mark and copy) : 暂停时间长,碎片少.
    标记清除整理算法(mark-sweep-compact):时间长,碎片少.
  
 清理策略:
    
    分代清理:young-gen GC /old-gen GC/Eden GC
    分区清理:分为2048个regions.优先清理垃圾最多的.

 
 各个GC的特点,优点,缺点
    
    串行GC:不能充分利用多核CPU,JVM在GC时只能使用单个核心. CPU的利用率高,暂停时间长.
    并行GC: 支持指定GC线程数,默认为CPU核心数. nThread=count*5/8+3. 主要场景是增加吞吐量,对系统资源的高效使用,适用于大并发服务场景.总暂停时间长.
    CMSGC:不对老年代进行整理,和应用线程一起并发执行,主要场景是为了降低GC停顿导致的系统延时.
    G1GC:将STW停顿的时间和分布编程可配置的,堆不再分代回收,而是按2048个块(regions).以增量的方式进行,垃圾最多的块优先收集.
  
 各个GC的适用场景和GC参数
 
    1.如果系统考虑吞吐量优先,CPU资源都用来最大程度处理业务,使用并行GC.
    2.如果系统考虑低延迟优先,每次GC时间尽量短,用CMSGC.
    3.如果内存堆较大,对GC时间可控,使用G1GC.
 
 
 GC的健康-故障-隐患排查思路
    
    1.监控工具-性能指标监控
        micrometer-jvm
        堆内存使用
        暂停时间       
    2.指定启动参数和垃圾收集器
        jvm的启动参数配置
            -XX:ParallelGCThread= STW阶段的并行worker线程数. n=core_count(core_count<=8) || n=core_count*5/8+3(core_count>8)
            -XX:ConcGCThreads= 并发标记的GC线程数,默认是ParallelGCThread的1/4.
    3.收集GC日志,从日志中观察关键的事件和内存使用率
               GC暂停时间
               FullGC的频率
               堆内存内年轻代/老年代的GC频率和CPU占用率    
    
    
    
    