package edu.vt.ece.hw4.backoff;

import static edu.vt.ece.hw4.locks.BackoffLock.count;

public class PolynomialBackoff implements Backoff{
    static double a;
    static double e;
    public void backoff() throws InterruptedException {
        a = count;
        e =Math.exp(1);

        //System.out.println("a:"+count);
        int delay = (int) Math.pow(a,e);
        System.out.println("delay:"+delay);
        Thread.sleep(delay);
    }
}