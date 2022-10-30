package edu.vt.ece.hw4.backoff;

import static edu.vt.ece.hw4.locks.BackoffLock.count;

public class FixedBackoff implements Backoff {

    @Override
    public void backoff() throws InterruptedException {


        int delay = 30;
        Thread.sleep(delay);
    }
}
