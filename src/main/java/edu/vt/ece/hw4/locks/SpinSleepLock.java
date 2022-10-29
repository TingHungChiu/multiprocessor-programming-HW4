package edu.vt.ece.hw4.locks;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SpinSleepLock implements Lock {
    private int maxspin;
    private AtomicInteger count = new AtomicInteger(0);
    private AtomicBoolean state = new AtomicBoolean(false);

    public SpinSleepLock(int maxSpin) {
        maxspin = maxSpin;
        /*count.set(0);*/
    }

    @Override
    public void lock() {
            if(count.getAndIncrement()>maxspin)
            {
                try {
                    wait();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (state.get()) {};  // ece.vt.edu.spin
            if (!state.getAndSet(true))
                return;
    }

    @Override
    public void unlock() {
        state.set(false);
        count.decrementAndGet();
    }

}