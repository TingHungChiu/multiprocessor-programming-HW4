package edu.vt.ece.hw4.locks;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SpinSleepLock implements Lock {
    private int maxspin;
    private AtomicInteger count;
    private AtomicBoolean state = new AtomicBoolean(false);

    public SpinSleepLock(int maxSpin) {
        maxspin = maxSpin;
        count = new AtomicInteger(0);
    }

    @Override
    public void lock() {
        count.getAndIncrement();
        int x = count.get();
        if (x >maxspin) {
                unlock();
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
        }
        while (true) {
            while (state.get()) {};  // ece.vt.edu.spin
            if (!state.getAndSet(true))
                return;
        }
    }

    @Override
    public void unlock() {
        state.set(false);
        count.decrementAndGet();
    }

}