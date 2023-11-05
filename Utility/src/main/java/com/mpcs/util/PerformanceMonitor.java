package com.mpcs.util;

public class PerformanceMonitor {

    private long start;

    public PerformanceMonitor() {
    }


    public void start() {
        start = System.currentTimeMillis();
    }

    public long getTime() {
        return System.currentTimeMillis() - start;
    }

}
