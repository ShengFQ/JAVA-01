package com.geekbang.shengfq.week1.gc;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
/**
 * gc日志分析
 *
 * 变种
 * 比如让缓存的对象变多，在限制堆内存的情况下，就可以模拟 内存溢出 。
 * 增加运行时长，比如加到30分钟或者更长，我们就可以用前面介绍过的 VisualVM 等工具来实时监控
 * 和观察。
 * 当然，我们也可以使用全局静态变量来缓存，用来模拟 内存泄漏 ，以及进行堆内存Dump的试验和分
 * 析。
 * 加大每次生成的数组的大小，可以用来模拟 大对象/巨无霸对象 （大对象/巨无霸对象主要是G1中的概
 * 念，比如超过1MB的数组，具体情况在后面的课程中再进行探讨）
 *
 * @author teacher
 * @date 2021-01-12
 * */
public class GCLogAnalysis {
    private static Random random = new Random();
    private static Object[] cachedGarbage ;
    public static void main(String[] args) {
        // 当前毫秒时间戳
        long startMillis = System.currentTimeMillis();
        // 持续运行毫秒数; 可根据需要进行修改
        long timeoutMillis = TimeUnit.SECONDS.toMillis(1);
        // 结束时间戳
        long endMillis = startMillis + timeoutMillis;
        LongAdder counter = new LongAdder();
        System.out.println("正在执行...");
        // 缓存一部分对象; 进入老年代
        //default 2000,设置为200000之后,修改堆最大值50m,就会发生oom,heap
        int cacheSize = 20000;
        //Object[] cachedGarbage = new Object[cacheSize];
         cachedGarbage = new Object[cacheSize];
// 在此时间范围内,持续循环
        while (System.currentTimeMillis() < endMillis) {
// 生成垃圾对象
            Object garbage = generateGarbage(100*1024);
            counter.increment();
            int randomIndex = random.nextInt(2 * cacheSize);
            if (randomIndex < cacheSize) {
                cachedGarbage[randomIndex] = garbage;
            }
        }
        System.out.println("执行结束!共生成对象次数:" + counter.longValue());
    }

    // 生成对象
    private static Object generateGarbage(int max) {
        int randomSize = random.nextInt(max);
        int type = randomSize % 4;
        Object result = null;
        switch (type) {
            case 0:
                result = new int[randomSize];
                break;
            case 1:
                result = new byte[randomSize];
                break;
            case 2:
                result = new double[randomSize];
                break;
            default:
                StringBuilder builder = new StringBuilder();
                String randomString = "randomString-Anything";
                while (builder.length() < randomSize) {
                    builder.append(randomString);
                    builder.append(max);
                    builder.append(randomSize);
                }
                result = builder.toString();
                break;
        }
        return result;
    }
}
