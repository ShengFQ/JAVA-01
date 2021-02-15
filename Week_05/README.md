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