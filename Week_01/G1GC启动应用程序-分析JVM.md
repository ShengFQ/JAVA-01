###作业 week1-jvm02
 学号:G20210579020387 日期:2021-01-10 标题:使用G1GC启动一个应用程序,分析jvm的情况

启动脚本
	
	java -XX:+UseG1GC com/geekbang/shengfq/week1/gc/GCLogAnalysis
	
	java -XX:+UseG1GC -XX:+UnlockExperimentalVMOptions -XX:G1NewSizePercent=2 com/geekbang/shengfq/week1/gc/GCLogAnalysis
	
	java -XX:+UseG1GC -XX:+UnlockExperimentalVMOptions -XX:G1NewSizePercent=2 -XX:ConcGCThreads=4 com/geekbang/shengfq/week1/gc/GCLogAnalysis
	
	 java -XX:+UseG1GC -Xms128m -Xmx512m -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps com/geekbang/shengfq/week1/gc/GCLogAnalysis
	
目录
	
	1.使用的jvm检测工具
	2.关键指标解读
	3.性能分析评估
	4.调整参数实验
	5.验证G1GC的优点和缺点

当前系统的配置
<pre>	
操作系统: Mac OS X 10.13.6 体系结构: x86_64 处理程序数: 4
提交的虚拟内存: 8,015,056 KB 总物理内存: 8,388,608 KB 空闲物理内存: 17,252 KB
总交换空间: 3,145,728 KB 空闲交换空间: 1,775,104 KB
</pre>

预期	
	
	使用G1GC能减少GC卡顿时间,通过对性能参数的调整,验证G1GC的优点和缺点.
	堆内存 128m-512m
	代码中,我设置缓存的对象数大小:20000个,生成的垃圾对象100*1024个.运行导致OOM:java heap space 	error.
	
分析
	
	拿到gc.demo.log 日志后

CommandLine flags: 

-XX:InitialHeapSize=134217728 -XX:MaxHeapSize=536870912 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseG1GC 
 	
    2021-01-13T15:48:58.719-0800: 0.124: [GC pause (G1 Evacuation Pause) (young), 0.0036036 secs]  //G1转移暂停,纯年轻代模式,只清理年轻代空间
 
    [Parallel Time: 2.9 ms, GC Workers: 4] //后面的活动由4个worker线程执行,消耗2.9ms
 	[Code Root Fixup: 0.0 ms] //释放用于管理并行活动的内部数据,一般接近0.
        [Code Root Purge: 0.0 ms] //清理其他部分数据,
     [Clear CT: 0.1 ms]
        [Other: 0.5 ms] //其他活动消耗的时间,大部分是并行执行.
      [Choose CSet: 0.0 ms]
      [Ref Proc: 0.2 ms]
      [Ref Enq: 0.0 ms]
      [Redirty Cards: 0.1 ms]
      [Humongous Register: 0.0 ms]
      [Humongous Reclaim: 0.1 ms]
      [Free CSet: 0.0 ms]
        [Eden: 14.0M(14.0M)->0.0B(15.0M) //暂停前和暂停后,Eden区的使用量/总量.
         Survivors: 0.0B->2048.0K  //GC暂停前后,存活区的使用量.
        Heap: 17.6M(128.0M)->5313.8K(128.0M)] //暂停前后,整个堆内存的使用量和总容量.
   
         [Times: user=0.01 sys=0.00, real=0.01 secs]  //GC事件的持续时间
   
   worker线程的行为
   
    [Parallel Time: 3.3 ms, GC Workers: 4]
      [GC Worker Start (ms): Min: 148.2, Avg: 148.2, Max: 148.3, Diff: 0.0]
      [Ext Root Scanning (ms): Min: 0.1, Avg: 0.2, Max: 0.3, Diff: 0.1, Sum: 0.7]
      [Update RS (ms): Min: 0.0, Avg: 0.0, Max: 0.1, Diff: 0.1, Sum: 0.1]
         [Processed Buffers: Min: 0, Avg: 1.5, Max: 4, Diff: 4, Sum: 6]
      [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Object Copy (ms): Min: 2.8, Avg: 2.9, Max: 3.1, Diff: 0.3, Sum: 11.8]
      [Termination (ms): Min: 0.0, Avg: 0.1, Max: 0.3, Diff: 0.3, Sum: 0.5]
         [Termination Attempts: Min: 1, Avg: 1.0, Max: 1, Diff: 0, Sum: 4]
      [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [GC Worker Total (ms): Min: 3.3, Avg: 3.3, Max: 3.3, Diff: 0.0, Sum: 13.2]
      [GC Worker End (ms): Min: 151.5, Avg: 151.5, Max: 151.5, Diff: 0.0]
   
  
   G1:纯年轻代GC :并发标记 concurrent marking
   
   1.initial mark(初始标记)
        2021-01-13T15:48:58.812-0800: 0.217: [GC pause (G1 Humongous Allocation) (young) (initial-mark), 0.0043280 secs]
   
   2.root region scan (root区扫描)
        2021-01-13T15:48:59.069-0800: 0.474: [GC concurrent-root-region-scan-start]
        2021-01-13T15:48:59.069-0800: 0.474: [GC concurrent-root-region-scan-end, 0.0001163 secs]
   
   3.concurrent mark(并发标记)
        2021-01-13T15:48:58.817-0800: 0.222: [GC concurrent-mark-start]
        2021-01-13T15:48:58.818-0800: 0.224: [GC concurrent-mark-end, 0.0015991 secs]
   
   4.remark(再次标记)
        2021-01-13T15:48:58.985-0800: 0.390: [GC remark 2021-01-13T15:48:58.985-0800: 0.390: [Finalize Marking, 0.0000870 secs]
   5.cleanup(清理)
        2021-01-13T15:48:58.987-0800: 0.392: [GC cleanup 144M->144M(369M), 0.0003798 secs]
   
   
   Evacuation Pause(mixed) 转移暂停:混合模式 不止清理年轻代,还有一部分老年代区域加入.
   
   FULL GC (一般在堆内存空间较小时触发)
   2021-01-13T15:48:59.376-0800: 0.781: 
   [Full GC (Allocation Failure)  442M->384M(512M), 0.0511523 secs]
    [Eden: 0.0B(25.0M)->0.0B(25.0M) 
    Survivors: 0.0B->0.0B 
    Heap: 442.4M(512.0M)->384.3M(512.0M)], 
    [Metaspace: 2707K->2707K(1056768K)]
 [Times: user=0.04 sys=0.00, real=0.05 secs] 
   
   
   ---------------------
   修改堆内存大小为512-1024m之后,full GC没有发生了.
   
   	java -XX:+UseG1GC -Xms512m -Xmx1024m  -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps com/geekbang/shengfq/week1/gc/GCLogAnalysis
   
   	
   	[Eden: 25.0M(25.0M)->0.0B(21.0M) Survivors: 0.0B->4096.0K Heap: 33.6M(512.0M)->12.0M(512.0M)]
	[Eden: 124.0M(124.0M)->0.0B(132.0M) Survivors: 13.0M->18.0M Heap: 330.0M(816.0M)->236.2M(858.0M)]
 	[Eden: 132.0M(132.0M)->0.0B(144.0M) Survivors: 18.0M->19.0M Heap: 412.9M(858.0M)->296.6M(892.0M)]
   [Eden: 74.0M(228.0M)->0.0B(320.0M) Survivors: 21.0M->21.0M Heap: 439.7M(919.0M)->380.9M(940.0M)]
    [Eden: 320.0M(320.0M)->0.0B(226.0M) Survivors: 21.0M->43.0M Heap: 789.8M(940.0M)->517.6M(1020.0M)]
   
   Heap
 garbage-first heap   total 1044480K, used 529985K [0x0000000780000000, 0x0000000780101fe0, 0x00000007c0000000)
  region size 1024K, 44 young (45056K), 43 survivors (44032K)
 Metaspace       used 2714K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 296K, capacity 386K, committed 512K, reserved 1048576K
   
  运行结果:
   执行结束!共生成对象次数:5293

</pre>