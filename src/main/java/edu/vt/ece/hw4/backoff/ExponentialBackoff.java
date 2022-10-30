package edu.vt.ece.hw4.backoff;



import static edu.vt.ece.hw4.locks.BackoffLock.count;

public class ExponentialBackoff implements Backoff {
    static double a;

    @Override
    public void backoff() throws InterruptedException {

        a = count;
        int delay = (int) Math.pow(2,a);
        Thread.sleep(delay);
    }

}
