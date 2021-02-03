###作业 week4-juc03
 学号:G20210579020387 
 日期:2021-02-03
 主题:JUC 的理解和总结
###并发编程课堂案例实践-week4-01

##Join.java
>场景一.synchronized(thread1)
thread1.join() 当前主线程阻塞,让thread1执行完成再执行主线程.会释放掉当前对象锁,就是thread1对象锁.
所以, main_thread执行打印0-19,让出时间给thread1,thread1获取到this对象锁执行打印完成,main_thread继续获取
 当前锁继续执行下去.

>场景二. synchronized(oo) 使用oo是全局对象锁
 synchronized(oo)
 thread1.join()当前主线程阻塞,让thread1执行完成再执行主线程.会释放掉当前对象锁,而不是当前同步块的oo对象锁.
所以,thread1执行同步块要获得oo对象锁获取不到,锁让main持有,程序处于阻塞状态.

>场景三.synchronized(oo) 使用oo是监视器,产生信号量
oo.wait();//能够让当前线程处于等待状态,释放掉oo对象锁,发出信号量,让别的线程去持有oo对象锁.
所以,结果跟场景一一致.

####WaitAndNotify.java 生产者消费者
>wait() //当前线程释放掉对象锁,处于主动等待阻塞状态
notifyAll() //唤醒被对象锁标记的线程重新获得锁进入就绪状态
都是调用当前对象锁上的信号量

####关于基础并发的总结
	thread.join();必须在同步块中调用,阻塞当前线程,thread线程获得执行权.
	Object.wait()/notify()/notifyAll()必须在当前锁定Object锁的前提下调用,否则抛IllegalMonitorStateException异常
	

	

####AtomicMain.java 原子类对象
Count.java SyncCount.java AtomicCount.java 
启动多线程计数(计数太小看不出差异)
对普通对象基础类型变量累加,并发累加会导致结果脏读.
对原子类对象进行自旋,不用加锁也能起到同步原子操作的效果.
将add方法加上synchronized内置锁,并发安全.
将累计变量加上volatile,提升可见性,并不安全,因为volatile不能保证原子性操作.

####LongDemo.java 原子操作与
多线程操作原子类,无需加锁,能实现并发安全操作.
AtomicObject 原理是自旋
LongAdder 原理不清楚???

####ConditionDemo.java 可重入锁阻塞条件案例
Lock lock=new ReentrantLock();
Condition notFull=lock.newCondition();//信号量,当前线程持有notFull信号量
notFull.await();//持有notFull信号量,当前线程阻塞
notFull.signal();//持有notFull信号量的其他线程可以就绪

####ReentrantLockDemo.java 可重入锁的互斥访问案例
Count.java //可重入锁(公平锁)
Count2.java //可重入读写锁(读共享,写互斥)

锁的定义是具有资源排他访问的状态量,
可重入锁的访问是线性顺序执行的.读写是公平的,顺序执行.

####ReentrantReadWriteLockDemo.java 可重入读写锁
读锁可以被共享访问,非互斥.
写锁是互斥访问.

####ReentrantReadWriteLockDemo2.java 可重入读写锁案例
	  * 1.首先开启读锁去缓存中取数据
     * 2.数据不存在，则释放读锁，开启写锁
     * 3.释放写锁
     * 4.开启读锁
     * 5.释放读锁
####LockMain.java 死锁的案例
Count3.java 通过内部两个变量锁控制同步访问共享数据,两个不同的线程在同步块中分别持有不同的变量锁,去访问对方持有的变量锁,都没有释放,又都想获取,导致死锁.
减少死锁的办法:
知道死锁产生的原因:存在互斥条件访问,只要去掉互斥条件访问就行.多发生在synchronized嵌套


####ExceptionDemo.java ExecutorService线程池执行器异常捕获案例
submit(Runnable);//子线程中发生了异常,在主线程中可以catch到异常.
execute(Runnable);//不会捕获异常

###ExecutorServiceDemo.java  这是代码常用的
ScheduledExecutorService.java 定时线程池
ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);
//如果是CPU密集型应用,线程数量=core数. 如果是IO密集型应用,线程数量=2*core数.
Future<T> result=s.submit(new  Callable()); //带返回值,可以定制线程池返回结果

####NewCachedThreadPoolDemo.java 
ExecutorService executorService = Executors.newCachedThreadPool();//可自由伸缩大小的线程池
ExecutorService executorService = Executors.newFixedThreadPool(4);//固定大小的线程池.

ExecutorService executorService = Executors.newSingleThreadExecutor();//只有一个线程,这种方式,确保有一个线程执行,就算异常了,还是会在启动一个,保证所有任务的执行顺序按照任务的添加顺序执行.


##关于线程池的思考总结:
>常用的线程池: 
JDK自带的:
Executors.newFixedThreadPool();
只需要一个线程执行,而且是顺序的: Executors.newSingleThreadExecutor();
需要定时执行: Executors.newScheduledThreadPool();
除了单线程池线程池,其他线程池都无法保证任务的执行顺序是有序的,所以如果是对共享数据的访问,仍然需要自己添加互斥逻辑.

>自定义线程池:
ThreadPoolExecutor(int coreSize,int maxSize,BlockingQueue,RejectExecutionHandler)

>ExecutorService提交任务给线程池的方式:
>>不同点:
Future<T> result=executor.submit();方法能够在主线程返回异常信息,并且将返回结果封装到Future对象,如果要定制返回结果,提交的任务需要实现Callable接口.
executor.execute();方法提交任务给线程池,主线程无法捕获异常,无法返回结果.

>>相同点:
executor.submit()/execute()方法是异步执行的,提交了立即返回.

>任务行为封装对象:
Runable.run();//无返回值的任务
Callable.call();//有返回值的任务,executor.submit()联用.

>任务监控对象(回调函数):

<pre>
Future<T> 
	boolean cancel();
	boolean isCancel();
	boolean isDone();
	T get();//会阻塞当前线程,类似于join();
	T get(timeout);

</pre>	


####class7 -并发高级
<pre>
Condition.java [一般用于看源码]
	void await() --> Object.wait()
	void awaitUninterruptibly() 等待信号
	boolean await(timeout)-->等待信号,超时返回false
	void signal() -->Object.notify()
	void signalAll() -->Object.notifyAll()
通过 Lock.newCondition()创建。
可以看做是 Lock 对象上的信号。类似于 wait/notify

LockSupport.java -->Thread类的静态方法,作用本线程
	void park(Object);//暂停当前线程
	void parkNanos();//暂停,有超时
	void parkUntile();
	void park();//强制暂停
	void unpark(Thread);//恢复某个线程,自己无法恢复自己
	
	Semaphore(permits=integer,fair=true) -->信号量
	作用:
	1.准入数量N
 	2.N=1 则等价于独占锁,常用在写锁场景,线性执行.
 	场景:同一时间控制并发线程数,防止海量线程冲垮CPU性能
 	permits=1表示独占锁,一般用于写锁场景,写锁场景就是修改局部变量数据,读锁尽可能不用锁或者可重入.
 	
 	CompletableFuture.java
 		supplyAsync(Supplier)
 		thenAccept(Consumer)
 		thenApplyAsync(Function)
 		
</pre>

###CompletableFutureDeme.java
<font color=red>没看懂</font>















