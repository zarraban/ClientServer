package org.example.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionCounter {
    private final static AtomicInteger counter = new AtomicInteger(1);


    public synchronized Integer getDetails() {
        return counter.getAndIncrement();
    }

}
