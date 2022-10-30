package edu.vt.ece.hw4.backoff;

import static edu.vt.ece.hw4.locks.BackoffLock.count;

public class FixedBackoff implements Backoff {

    @Override
    public void backoff() throws InterruptedException {

        //System.out.println("a:"+count);
        int delay = 30;
        System.out.println("delay:" + delay);
        Thread.sleep(delay);
    }
}
