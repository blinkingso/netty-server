package com.yz.netty.atomics;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.locks.LockSupport;

/**
 * java中的atomic类
 * AtomicBoolean
 * AtomicInteger
 * AtomicIntegerArray
 * AtomicIntegerFieldUpdater
 * AtomicLong
 * AtomicLongArray
 * AtomicLongFieldUpdater
 * AtomicMarkableReference
 * AtomicReference
 * AtomicReferenceArray
 * AtomicReferenceFieldUpdater
 * AtomicStampedReference
 * DoubleAccumulator
 * DoubleAdder
 * LongAccumulator
 * LongAdder
 * Striped64
 */
public class Counter {

    private volatile int count;
    private static final AtomicIntegerFieldUpdater<Counter> updater = AtomicIntegerFieldUpdater.newUpdater(Counter.class, "count");

    private String name;

    // 可以解决ABA问题
    private final AtomicMarkableReference<String> mr = new AtomicMarkableReference<>(name, true);

    public Counter(String name) {
        this.count = 0;
        this.name = name;
    }

    public void increment() {
        updater.addAndGet(this, 1);
    }

    public int getCount() {
        return count;
    }

    public static void main(String[] args) {
        final Counter counter = new Counter("Hello world");
        for (int i = 0; i < 100; i++) {
            new Thread(counter::increment).start();
        }
        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(100));
        System.out.println(counter.getCount());

        counter.mr.compareAndSet("mu", "andrew", true, false);

    }
}
