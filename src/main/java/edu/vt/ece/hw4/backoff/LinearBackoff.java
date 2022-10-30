package edu.vt.ece.hw4.backoff;

import static edu.vt.ece.hw4.locks.BackoffLock.count;

public class LinearBackoff implements Backoff{
    static double a;
    public void backoff() throws InterruptedException {
        a = count;

        int delay = (int)a;
        Thread.sleep(delay);
    }
}
