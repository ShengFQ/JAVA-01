###netty作业-自定义实现网关
 学号:G20210579020387 
 日期:2021-01-25
 主题: 使用netty搭建http服务作为一个简单的gateway.
功能
	
	1.网关作为统一入库,根据http请求的url,dispatch到内网服务请求转发.
	2.通过在http header区增加token,实现filter,请求和响应header都需要filter.
	3.内网服务有多个,通过一个随机策略,将当前请求路由到内网的http服务,实现路由转发.
	
实现方式
	
	1.简单的实现就是在netty的ServerInboundHandler中拦截添加filter和router.[完成作业]
	2.这个网关是一个积木组件,通过组件化将模块组装成为一个标准可扩展,高内聚低耦合的网关产品.[实验作品]
	3.网关的应用场景通常是高并发低延迟访问,如果能够重用线程资源,提高CPU利用率,提高io的性能,就是一个可商用的产品[优秀产品]
	
程序结构

		GatewayApplication.java //服务启动类
		GatewayServer.java //http网关服务监听服务启动
		GatewayServerInitializer.java //网关服务初始化器
		GatewayServerInboundHandler.java //基于事件处理的接入channel处理器
		GatewayServerOutboundHandler.java //处理网关服务器业务逻辑输出
		
	IGatewayHttpRouter.java //路由接口,可基于不同策略实现不同的路由功能
		GatewayHttpRandomRouter.java//随机策略路由实现
	IHttpRequestFilter.java //请求过滤器接口,对request拦截,附加参数
		TokenHttpRequestFilter.java //请求过滤器实现
	IHttpResponseFilter.java //响应过滤器接口,对Response拦截,附加参数
		TokenHttpResponseFilter.java //响应过滤器实现
	
改造1:
	
		1.将后端的代理访问由同步访问改成并发访问.但是经过压测,性能并没有得到改善,反而降低
		2.想要实现对变更开放,对修改关闭,filter,router通过加注解实现动态过滤和路由,不用再修改原有接口层代码的实现.
			
心得笔记:
		
		影响netty性能的因素:
			1.不要阻塞EventLoop(最重要性能点)
		2.系统参数优化 (tcp)
		3.缓冲区优化 (tcp)
		SO_REVBUF/SO_SNDBUF/SO_BACKLOG/REUSEXXX
		4.心跳机制/断线重连
		5.内存与bytebuffer优化
		directbuffer/heapbuffer
		6.ioRatio
		watermark 高低水位,枯水期降低阈值,满水期升高阈值
		TranfficShaping

		
		API网关的性能对比:
			1.在大流量时代,我们遇到了什么问题,才设计了网关这个组件
			2.业内网关起到什么作用:proxy,filter,router
			3.业内业务网关:springcloud-gateway和zuul的区别
				zuul:pre,routing,post,阻塞io实现,性能低,不推荐使用.
				zuul2:netty,service discovery,load balancing,connection pooling,retries,proxy,gzip,websocket
				springcloud-gateway:流控,服务降级,熔断,负载均衡,灰度策略,过滤,权限拦截,等级拦截,业务规则,参数校验,缓存策略.
			4.网关的原理是怎样的,上节课,我们通过proxy forward转发,filter,router等基本功能实现简单网关.



关于作业和老师demo的思考:
@助教-邓大明  大明哥, 我把k神的案例 gateway run起来,并且按你的建议,把机器重启,只开这几个测试下性能数据,发现延迟还是挺高,吞吐量还是低,创建了上千个线程没有关闭,  我自己实现的并没有使用线程池访问代理,反而吞吐高,延迟低,  这让我得到一个怀疑, k神那里并发httpclient使用线程池,是不是存在问题......

我本机的配置:CPU4核,内存8G.本gateway-demo是io密集型应用.
老师的demo boss线程池1,worker线程池16. 而且接收到请求后又用并发线程池发起http请求.
我的作业 boss线程池1,worker线程池4.接收到请求同步发起http请求.

大明哥电脑CPU8核,内存16G,吞吐量高2倍,延迟低3倍.他的电脑性能可能更适合跑demo程序,也就说明demo配置的关键数据,bossGroup/workerGroup线程池大小跟CPU核心数有密切联系,启动异步线程.
还有并发线程池发起http请求并不是一个高效的办法,大量的线程处于waiting状态.

性能测试:
server1 server2:
/Users/sheng/workspace/github/kking/JavaCourseCodes/02nio/nio01/target/classes/java0/nio01/HttpServer01.class
/Users/sheng/workspace/github/kking/JavaCourseCodes/02nio/nio01/target/classes/java0/nio01/HttpServer02.class
kimmking gateway:
/Users/sheng/workspace/github/kking/JavaCourseCodes/02nio/nio02/target/classes/io/github/kimmking/gateway/NettyServerApplication.class

my homework:
/Users/sheng/workspace/github/ShengFQ/course/target/classes/com/geekbang/shengfq/week2/netty/homework/GatewayApplication.class

瓶颈:网络/内存/CPU
<pre>

我自个的同步httpclient访问:
bossGroup=1,workerGroup=4
wrk -c 40 -t 1 -d 60s http://localhost:8888
Running 1m test @ http://localhost:8888
  1 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    35.11ms   19.54ms 233.37ms   86.52%
    Req/Sec     1.19k   257.56     1.86k    66.00%
  71442 requests in 1.00m, 7.85MB read
Requests/sec:   1188.58
Transfer/sec:    133.68KB


  kimmking的并发线程池访问:创建了大量的proxyService-thread 处于waiting和 I/O dispatcher 157,堆内存60%
 bossGroup=1,workerGroup=16
 wrk -c 40 -t 1 -d60s http://localhost:8888
Running 1m test @ http://localhost:8888
  1 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    80.01ms  171.81ms 871.37ms   95.57%
    Req/Sec   553.13    214.74   710.00     86.67%
  881 requests in 1.00m, 83.45KB read
Requests/sec:     14.67
Transfer/sec:      1.39KB

 kimmking的并发线程池访问:创建了大量的proxyService-thread 处于waiting和 I/O dispatcher 157,堆内存100%
 bossGroup=1,workerGroup=4
  wrk -c 40 -t 1 -d60s http://localhost:8888
Running 1m test @ http://localhost:8888
  1 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    80.15ms  205.48ms 978.75ms   95.05%
    Req/Sec   515.27    229.72   810.00     73.33%
  808 requests in 1.00m, 76.54KB read
Requests/sec:     13.46
Transfer/sec:      1.27KB

 kimmking的并发线程池访问:创建了指数级的proxyService-thread 处于waiting和 I/O dispatcher 1008,堆内存50%
 bossGroup=1,workerGroup=16
wrk -c 400 -t 1 -d60s http://localhost:8888
Running 1m test @ http://localhost:8888
  1 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   276.00ms  503.87ms   1.84s    84.89%
    Req/Sec   521.18    202.34     1.00k    77.27%
  1178 requests in 1.00m, 111.59KB read
  Socket errors: connect 150, read 0, write 0, timeout 13
Requests/sec:     19.63
Transfer/sec:      1.86KB

kimmking的非线程池异步http访问: 创建了165个IO dispatcher线程, 堆内存100%占用.
 bossGroup=1,workerGroup=16
 wrk -c 40 -t 1 -d60s http://localhost:8888
	Running 1m test @ http://localhost:8888
  1 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    81.16ms  187.55ms 812.52ms   93.54%
    Req/Sec   523.91    195.53   730.00     81.82%
  619 requests in 1.00m, 58.64KB read
Requests/sec:     10.31
Transfer/sec:      0.98KB


  </pre>