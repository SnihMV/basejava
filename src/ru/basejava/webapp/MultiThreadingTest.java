package ru.basejava.webapp;

public class MultiThreadingTest {
}

class DeadLock {
    public static void main(String[] args) {
        String lock1 = "lock1";
        String lock2 = "lock2";
        new Thread(() -> deadLock(lock1, lock2), "T1").start();
        new Thread(() -> deadLock(lock2, lock1), "T2").start();

    }

    private static void deadLock(Object lock1, Object lock2) {
        System.out.println(Thread.currentThread().getName() + " before " + lock1);
        synchronized (lock1) {
            System.out.println(Thread.currentThread().getName() + " just acquired " + lock1);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + " before " + lock2);
            synchronized (lock2) {
                System.out.println(Thread.currentThread().getName() + " just acquired " + lock2);
            }
        }
    }
}

class RaceCondition {
    public static void main(String[] args) {
        Counter c = new Counter();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1_000_000; i++) {
                c.incr();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1_000_000; i++) {
                c.decr();
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
    }

    static class Counter {
        private int count;

        public void incr() {
            count++;
        }

        public void decr() {
            count--;
        }

        public int getCount() {
            return count;
        }
    }

}


class Singleton {
    private static volatile Singleton instance;

    private Singleton() {
    }

    public static Singleton getInstance() {
        Singleton localInstance = instance;
        if (localInstance == null)
            synchronized (Singleton.class) {
                localInstance = instance;
                if (localInstance == null)
                    instance = localInstance = new Singleton();
            }
        return localInstance;
    }
}

class Sngltn {
    private Sngltn() {
    }

    public static Sngltn getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final Sngltn INSTANCE = new Sngltn();
    }
}


