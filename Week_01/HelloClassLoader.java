package com.geekbang.shengfq.week2;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/**
 * 自定义xlass类加载器,读取加密的字节码文件
 * xlass文件需要放在resources根目录下
 * @author sheng
 * @date 2021-01-08
 * */
public class HelloClassLoader extends ClassLoader {
    /**
     * main方法首次加载
     * */
    public static void main(String[] args) {
        try {
            Class<?> clazz = new HelloClassLoader().findClass("Hello");
            Method declaredMethod = clazz.getDeclaredMethod("hello");
            declaredMethod.invoke(clazz.newInstance());
        } catch (ClassNotFoundException | NoSuchMethodException |
                IllegalAccessException | InstantiationException |
                InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    /**
     * 重写方法加载器
     * @param className 加载的类名
     * */
    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        byte[] classData = loadClass2Memory(className);
        return defineClass(className, classData, 0, classData.length);
    }
    /**
     * 将字节码文件内容读取到二进制字节数组
     * @param className 加载的类名
     * */
    private byte[] loadClass2Memory(String className) throws ClassNotFoundException {
        byte[] fileBytes;
        try {
            fileBytes = loadFromFile(className.replace("//", ".") + ".xlass");
        } catch (IOException e) {
            e.printStackTrace();
            throw new ClassNotFoundException("xlass 文件未读取");
        }
        return fileBytes;
    }
    /**
     * 读取class文件到内存
     * @param path 字节码文件的相对路径
     * */
    private byte[] loadFromFile(String path) throws IOException {
        //从项目的resources目录下找文件 Hello.xlass需要放在resources目录下
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
        if(inputStream==null){
            throw new FileNotFoundException("file not exists!!");
        }
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int nextValue;
        while ((nextValue = inputStream.read()) != -1) {
            bout.write(nextValue);
        }
        return decode(bout.toByteArray());
    }
    /**
     * 解码字节码
     *  @param xlass  原始字节码数组
     * */
    private byte[] decode(byte[] xlass) {
        for (int i = 0; i < xlass.length; i++) {
            xlass[i] = (byte) (255 - xlass[i]);
        }
        return xlass;
    }
}