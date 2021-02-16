####学习笔记 学号:G20210579020387 2021-02-14

###aop的实现原理
1.cglib
2.jdk proxy
###spring bean的装配方式

####xml or announce
1.xml -->ClassPathXmlApplicationContext("application.xml");
2.annotation -->AnnotationConfigApplicationContext("xxx.xxx"")
Student student_04 = (Student) annotationContext.getBean("student_04");

####beanFactory
DefaultListableBeanFactory beanFactory=new DefaultListableBeanFactory();
ConstructorArgumentValues cargs=new ConstructorArgumentValues();
AbstractBeanDefinition bean=new RootBeanDefinition(xxx.class,cargs,null);

MutablePropertyValues propertyValues = new MutablePropertyValues();
AbstractBeanDefinition bean=new RootBeanDefinition(xxx.class,cargs,null,propertyValues);

beanFactory.registerBeanDefinition("student_05", bean);
 Classroom classroomC = (Classroom) beanFactory.getBean("classroom_c");
###单例模式的总结
1.饿汉式,类加载时就会初始化对象,线程安全,造成不必要资源开销.
2.懒汉式,通过静态方法加载对象,线程不安全,资源开销按需分配.
3.同步懒汉式,通过静态方法加载对象,线程安全,加锁范围大
4.局部同步懒汉式,锁构造对象的局部代码块,线程不安全
5.双重检查DCL懒汉式,双重判断,线程安全
6.静态内部类获取外部类实例,线程安全,不会默认初始化.
7.枚举类实例,线程同步安全,效率高.
####单例模式
why?
--只需要一个实例,作为工厂生产实例的内存空间,一般这种实例不需要频繁的创建和销毁,在进程启动的整个生命周期都存在,负责加载进程资源,协调管理其他资源的生命周期.
比如我们遇到过的各种Manager管理类，各种Factory工厂类；
Spring 框架应用中的 ApplicationContext、数据库中的连接池等也都是单例模式。

how?
如何判断对象是否只有一个.//同一个类的不同对象的hashcode不同
如何判断线程是否安全?//用多线程执行任务,获取对象的hashcode是否一致.

###方式
<ul>
<li>饿汉 static instance=new Instance(); //初始化就加载
<li>懒汉 method(){return instance;} //需要时加载
<li>线程安全 synchronized method //锁同步块
<li>伪线程安全 synchronized (Class){}
<li>双检查同步块 
<li>内部类获取单例 **
<li>枚举值获取单例 **

</ul>

###maven/spring 的 profile 机制，都有什么用法?

一.实现多环境构建
使用场景 dev/test/pro
根据activeProfile来切换setting.xml中设置的私服地址
开发环境，应用需要连接一个可供调试的数据库单机进程
生产环境，应用需要使用正式发布的数据库，通常是高可用的集群
测试环境，应用只需要使用内存式的模拟数据库

二.区分bean构建条件
通过@Profile注解可以为一个Bean赋予对应的profile名称

三.可以代码配置当前启动的应用环境
在Web应用程序中，通过WebApplicationInitializer可以对当前的ServletContext进行配置。
JVM启动参数,java -jar application.jar -Dspring.profiles.active=dev
spring-boot-maven-plugin插件也支持设定profile，其原理也是通过启动参数实现

###hibernate和mybatis的异同点
数据库持久层:
    ORM-hibernate/mybatis
    事务管理
    数据源
    连接池
    级联查询
    
相同点
1.广义上都是持久层框架,java领域的持久层发展:JDBC->JPA
持久层框架就是复用JDBC,动态化数据源,可配置的连接池通过ORM技术,提高开发效率.
2.都是ORM框架,有实体关系映射到数据库结构表.支持逆向生成表结构.
不同点:
开发效率--性能内存--可维护性

开发效率维度
hibernate对于单表操作场景,开发速度快,效率高.用户不需要开发SQL.对于多表级联查询,也是可以通过JAVA BEAN配置和注解实现,但是对报表场景比较吃力.
支持,HQL,Criteria,native SQL操作数据库.是jpa的标准实现.

mybatis是半自动的ORM框架,需要定义mapper/xml操作数据库.支持NATIVE SQL.写SQL比较繁琐.

性能内存维度
hibernate是重量级的ORM框架,跟javabean集成度高,支持1/2/3级缓存,对内存需求比较大.
mybatis轻量级的orm框架,只支持1级缓存,对内存需求小.

可维护性
hibernate的查询经过映射后,对SQL进行了封装,不直观.
mybatis是原生SQL,参数可视化,还有mybatis-generator/mybatis-plus之类的插件支持.


持久层的发展
jdbc-->datasource->spring jdbc
>datasource---daoTemplate---dao


jpa-->entityManager->spring orm












































