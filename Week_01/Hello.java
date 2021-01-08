package com.geekbang.shengfq.week1;
/**
 * 类加载器demo
 * */
public class Hello {

    private int a;
    private int b;

    private double sum=0.0d;
    private int count=0;
    private int[] array={1,2,3,4,5,6};
    public Hello(int a,int b){
        this.a=a;
        this.b=b;
    }
    static{
        System.out.println("Hello class Initialized");
    }

    public static void main(String[] args) {
       Hello instance=new Hello(1,1);
       int result=instance.add();
        System.out.println(String.format("result:%s",result));
        result=instance.sub();
        System.out.println(String.format("result:%s",result));
        result=instance.mul();
        System.out.println(String.format("result:%s",result));
        result=instance.div();
        System.out.println(String.format("result:%s",result));
        double value=instance.getAvg();
        System.out.println(String.format("avg:%s",value));
    }

    private int add(){
        int c= a+b;
        System.out.printf("add  %s",c);
        return c;
    }

    private int sub(){
        return a-b;
    }

    private int mul(){
        return a*b;
    }

    private int div(){
        if(a==0 || b==0){
            throw new IllegalArgumentException("参数不能为0");
        }
        return a/b;
    }

    private double getAvg(){
        for(int i:array){
            sum+=i;
            count++;
        }
        return sum/count;
    }
}
