package com.yz.chat.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class StatisticDataUtil {
    private StatisticDataUtil() {
    }

    private static final AtomicInteger ACTIVE_CHANNELS = new AtomicInteger(0);
    private static final AtomicLong FLOW = new AtomicLong(0);

    public static int getActiveChannels() {
        return ACTIVE_CHANNELS.get();
    }

    public static long getFlow() {
        return FLOW.get();
    }

    public static void activeChannel() {
        ACTIVE_CHANNELS.incrementAndGet();
    }

    public static void inactiveChannel() {
        ACTIVE_CHANNELS.decrementAndGet();
    }

    public static void flow(long flow) {
        FLOW.addAndGet(flow);
    }

    static {
        new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    // ignored.
                }
                log.info("Active channels: {}", getActiveChannels());
                log.info("Flow: {}", getFlow());
            }
        }, "Statistic-Thread").start();
    }
}
