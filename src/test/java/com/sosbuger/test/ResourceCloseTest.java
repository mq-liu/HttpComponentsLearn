package com.sosbuger.test;

import java.io.Closeable;
import java.io.IOException;

/**
 * try是否会关闭资源
 * 会自动关闭资源
 */
public class ResourceCloseTest {

    public static void main(String[] args) {

        try (CloseTest closeTest = new CloseTest()) {
            closeTest.show();
        } catch (Exception e) {
            // 可以捕获方法的里的异常, 也可以捕获close中的异常
            System.out.println("异常捕获: " + e.getMessage());
        }
        // 当有异常发生的时候, 线程会中断
        System.out.println("thread closed ?");
    }

}

/**
 * 资源实现自动关闭, 必须要实现AutoCloseable接口
 *
 * @see java.lang.AutoCloseable
 */
class CloseTest implements Closeable {

    public void close() {
        System.out.println("object closed ...");
        throw new RuntimeException("资源关闭时的异常");
    }


    void show() {
        System.out.println("obj  show ...");
        // throw  new RuntimeException("不可预知的异常");
    }
}
