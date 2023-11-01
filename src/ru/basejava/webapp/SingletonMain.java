package ru.basejava.webapp;

import java.util.Objects;

public class SingletonMain {
    public static void main(String[] args) {
        Singleton s1 = Singleton.getInstance();
        Singleton s2 = Singleton.getInstance();
        System.out.println(s1.getStr());
        System.out.println(s2.getStr());
        System.out.println(s1 == s2);

        Counter c = new Counter();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1_000_000; i++) {
                c.inc();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1_000_000; i++) {
                c.dec();
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(c.getCount());


        String str1 = new String("lock1");
        String str2 = new String("lock2");

        deadlock(str1,str2, "fstThread");
        deadlock(str2,str1, "scdThread");
    }

    private static void deadlock(Object lock1, Object lock2, String threadName){
        new Thread(()->{
            System.out.println(Thread.currentThread().getName() + " waiting for " + lock1);
            synchronized (lock1){
                System.out.println(Thread.currentThread().getName() + " acquired " + lock1);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName()+" waiting for "+lock2);
                synchronized (lock2){
                    System.out.println(Thread.currentThread().getName() + " acquired " + lock2);
                }
            }
        },threadName).start();
    }

}

class Counter {
    private int count;

    public int getCount() {
        return count;
    }

    public void inc() {
        count++;
    }

    public void dec() {
        count--;
    }
}

class Singleton {
    private static volatile Singleton instance;
    private String str;

    private Singleton() {
        str = "Hello world";
    }

    public String getStr() {
        return str;
    }

    public static Singleton getInstance() {
        Singleton localInstance = instance;
        if (localInstance == null) {
            synchronized (Singleton.class) {
                localInstance = instance;
                if (localInstance == null)
                    localInstance = instance = new Singleton();
            }
        }
        return localInstance;
    }
}