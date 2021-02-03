package com.geekbang.shengfq.week3.class7;

import java.util.concurrent.*;

public class TestThead {
    private static int result = 0;
    private static Object oo = new Object();

    public static void main(String[] args) throws Exception {
        TestThead testThead=new TestThead();
        testThead. method1();
        System.out.println("main is over");
    }


    /**
     * method8 [OK]
     * 全局变量锁确保同步执行
     */
    private static void method8() throws Exception {
        final Object object = oo;
        MyTask task=new MyTask();
        Thread t1 = new Thread(() -> {
            synchronized (object) {
                result=task.sum();
                //object.notifyAll();
            }
        });
        t1.start();
        synchronized (oo){
            t1.join();//当前线程阻塞,会释放oo对象锁
        }
        System.out.println("result:"+result);
    }

    /**
     * 方法1.
     */
    private  void method1() throws Exception {
        final Object object = oo;
        MyTask task=new MyTask();
        MyThread t1 = new MyThread("t1",object,task);
        t1.start();

        synchronized (oo){
            t1.join();
          //  object.wait();//当前线程阻塞,但是并不会释放oo对象锁
        }
        result=t1.getResult();
        System.out.println("result:"+result);
    }

    /**
     * 方法4.
     */
    private static void method4() throws Exception {
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "hello world";
            }
        });
        Thread t1 = new Thread(futureTask);
        t1.start();
        System.out.println("return value:" + futureTask.get());

    }

    /**
     * 方法2.
     */
    private static void method2() throws Exception {
        ExecutorService pool1 = Executors.newFixedThreadPool(1);
        Future<String> result = pool1.submit(new Callable<String>() {
            @Override
            public String call() {
                return "hello world";
            }
        });
        System.out.println("return value:" + result.get());

    }

    /**
     * 方法3.
     */
    private static void method3() throws Exception {
        ExecutorService pool1 = Executors.newSingleThreadExecutor();
        Future<String> result = pool1.submit(new Callable<String>() {
            @Override
            public String call() {
                return "hello world";
            }
        });
        System.out.println("return value:" + result.get());

    }

    /**
     * 方法7
     */
    private static void method7() throws Exception {
        ExecutorService pool1 = Executors.newSingleThreadExecutor();
        Future<String> result = (Future<String>) pool1.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("hello world");
            }
        });
        System.out.println("return value:" + result.get());

    }

    /**
     * 方法5
     */
    private static void method5() throws Exception {
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "hello world";
            }
        });
        ExecutorService pool1 = Executors.newSingleThreadExecutor();
        pool1.execute(futureTask);
        System.out.println("return value:" + futureTask.get());

    }

    /**
     * 方法6
     */
    private static void method6() throws Exception {
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "hello world";
            }
        });
        ExecutorService pool1 = Executors.newSingleThreadExecutor();
        pool1.submit(futureTask);
        System.out.println("return value:" + futureTask.get());
    }


}
/**
 * 任务定义
* **/
class MyTask{
    /**
     * 任务主体
     */
    public  int sum() {
        return fibo(36);
    }

    private  int fibo(int a) {
        if (a < 2)
            return 1;
        return fibo(a - 1) + fibo(a - 2);
    }


}
/**
 * 线程定义
 */
class MyThread extends Thread {
    private volatile int result = 0;
    private String name;
    private Object oo;
    private MyTask task;
    public void setOo(Object oo) {
        this.oo = oo;
    }

    public int getResult(){
        return result;
    }

    public MyThread(String name,Object monitor,MyTask task) {
        this.name = name;
        this.task=task;
        this.oo=monitor;
    }

    @Override
    public void run() {
        synchronized (oo) {
            result = task.sum();
            oo.notifyAll();
        }
    }

}