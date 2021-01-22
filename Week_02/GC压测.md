
###作业 week2-jvm03
 学号:G20210579020387 
 日期:2021-01-14
 主题:使用压测工具（wrk或sb） ， 演练gateway-server-0.0.1-SNAPSHOT.jar示例
   
  启动脚本
  
    java -XX:+UseSerialGC -Xms128m -Xmx512m -Xloggc:gc.serial.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -jar gateway-server-0.0.1-SNAPSHOT.jar
	java -XX:+UseParallelGC -Xms128m -Xmx512m -Xloggc:gc.parallel.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -jar gateway-server-0.0.1-SNAPSHOT.jar
	java -XX:+UseConcMarkSweepGC -Xms128m -Xmx512m -Xloggc:gc.cms.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -jar gateway-server-0.0.1-SNAPSHOT.jar
	java -XX:+UseG1GC -Xms128m -Xmx512m -Xloggc:gc.G1.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps -jar gateway-server-0.0.1-SNAPSHOT.jar
	
  
  压测脚本
    
    wrk -t8 -c40 -d30s http://localhost:8088/api/hello
    Running 30s test @ http://localhost:8088/api/hello
    
  串行GC压测
  
    8 threads and 40 connections
    Thread Stats   Avg      Stdev     Max   +/- Stdev
        Latency    28.90ms   60.30ms 682.35ms   90.21%
        Req/Sec   634.45    312.43     1.64k    60.66%
      149677 requests in 30.06s, 17.87MB read
    Requests/sec:   4979.01
    Transfer/sec:    608.70KB
    
  并行GC压测
    
     8 threads and 40 connections
      Thread Stats   Avg      Stdev     Max   +/- Stdev
        Latency    17.57ms   37.77ms 519.51ms   90.97%
        Req/Sec     0.88k   357.35     1.98k    63.76%
      210976 requests in 30.09s, 25.19MB read
    Requests/sec:   7011.74
    Transfer/sec:    857.22KB
  
  CMSGC压测
    
    Thread Stats   Avg      Stdev     Max   +/- Stdev
        Latency    17.79ms   39.94ms 571.26ms   91.91%
        Req/Sec   836.22    319.40     1.79k    65.98%
      199411 requests in 30.10s, 23.81MB read
    Requests/sec:   6624.99
    Transfer/sec:    809.93KB
   
  G1GC压测
    
    Thread Stats   Avg      Stdev     Max   +/- Stdev
        Latency    16.71ms   35.53ms 539.88ms   91.00%
        Req/Sec     0.89k   374.19     2.13k    64.24%
      212133 requests in 30.10s, 25.33MB read
    Requests/sec:   7047.71
    Transfer/sec:    861.61KB
    
 
 
 从GC日志来看:
 
    串行GC :频繁进行young区的GC, 堆内存(年轻代使用率9%,老年代使用率 31%)
    并行GC: 频繁进行PSYoungGen区的GC, 堆内存(年轻代使用率24.9%,老年代使用率24.9%)
    CMSGC:频繁进行ParNew区的GC,堆内存(年轻代 52%,标记交换区:30%)
    G1GC:频繁进行young-eden区的GC, 堆内存(年轻代使用率 71%)
 从压测效果来看:
 
    Latency 延迟时间:G1GC <并行GC <= CMSGC < 串行GC
    
    Requests/sec 吞吐量(TPS/QPS): G1GC > 并行GC > CMSGC > 串行GC