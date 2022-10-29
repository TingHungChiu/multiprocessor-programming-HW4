package edu.vt.ece.hw4.backoff;

import static edu.vt.ece.hw4.locks.BackoffLock.count;

public class LinearBackoff implements Backoff{
    static double a;
    public void backoff() throws InterruptedException {
        a = count;

        //System.out.println("a:"+count);
        int delay = (int)a;
        System.out.println("delay:"+delay);
        Thread.sleep(delay);
    }
}
